package me.ichun.mods.googlyeyes.common.core;

import me.ichun.mods.googlyeyes.common.GooglyEyes;
import me.ichun.mods.googlyeyes.common.layer.LayerGooglyEyes;
import me.ichun.mods.googlyeyes.common.model.ModelRendererDragonHook;
import me.ichun.mods.googlyeyes.common.tracker.GooglyTracker;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class EventHandler {
    protected WeakHashMap<LivingEntity, GooglyTracker> trackers = new WeakHashMap<>();

    @SubscribeEvent
    public void onWorldTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (Minecraft.getInstance().level != null && !Minecraft.getInstance().isPaused()) {
                Iterator<Map.Entry<LivingEntity, GooglyTracker>> ite = trackers.entrySet().iterator();
                while (ite.hasNext()) {
                    Map.Entry<LivingEntity, GooglyTracker> e = ite.next();
                    GooglyTracker tracker = e.getValue();
                    if (GooglyEyes.eventHandlerClient.ticks - tracker.lastUpdateRequest > 10) {
                        ite.remove();
                    } else {
                        tracker.update();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            Iterator<Map.Entry<LivingEntity, GooglyTracker>> ite = trackers.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<LivingEntity, GooglyTracker> e = ite.next();
                GooglyTracker tracker = e.getValue();
                if (tracker.parent.level() == event.getLevel()) {
                    ite.remove();
                }
            }
        }
    }

    public GooglyTracker getGooglyTracker(LivingEntity living, HeadInfo<?> helper) {
        GooglyTracker tracker = trackers.get(living);
        if (tracker == null) {
            tracker = new GooglyTracker(living, helper);
            trackers.put(living, tracker);
        }
        return tracker;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addLayers() {
        LayerGooglyEyes layerGooglyEyes = new LayerGooglyEyes();

        HashSet<LivingEntityRenderer> addedRenderers = new HashSet<>();

        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        if (!(GooglyEyes.config.disabledGoogly.contains("minecraft:player") || GooglyEyes.config.disabledGoogly.contains("player"))) {
            Map<String, EntityRenderer<? extends Player>> skinMap = renderManager.getSkinMap();
            for (Map.Entry<String, EntityRenderer<? extends Player>> entry : skinMap.entrySet()) {
                PlayerRenderer playerRenderer = (PlayerRenderer) entry.getValue();
                playerRenderer.addLayer(layerGooglyEyes);
                addedRenderers.add(playerRenderer);
            }
        }
        renderManager.renderers.forEach((entityType, entityRenderer) -> {
            if (addedRenderers.contains(entityRenderer)) {
                return;
            }

            ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
            for (String s : GooglyEyes.config.disabledGoogly) {
                ResourceLocation disabled = new ResourceLocation(s);
                if (disabled.equals(rl)) {
                    return;
                }
            }

            if (entityRenderer instanceof LivingEntityRenderer renderer) {
                renderer.addLayer(layerGooglyEyes);
            } else if (entityRenderer instanceof EnderDragonRenderer dragonRenderer) {
                dragonRenderer.model.head.children.put("googlyEye", new ModelRendererDragonHook(dragonRenderer.model));
            }
        });
    }
}
