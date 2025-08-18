package me.ichun.mods.googlyeyes.common.core;

import me.ichun.mods.googlyeyes.common.GooglyEyes;
import me.ichun.mods.googlyeyes.common.layer.LayerGooglyEyes;
import me.ichun.mods.googlyeyes.common.tracker.GooglyTracker;
import me.ichun.mods.ichunutil.api.client.ILayerManager;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import me.ichun.mods.ichunutil.common.head.HeadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class EventHandler {
    protected WeakHashMap<LivingEntity, GooglyTracker> trackers = new WeakHashMap<>();
    public int ticks;

    @SubscribeEvent
    public void onWorldTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticks++;
            if (Minecraft.getInstance().level != null && !Minecraft.getInstance().isPaused()) {
                Iterator<Map.Entry<LivingEntity, GooglyTracker>> ite = trackers.entrySet().iterator();
                while (ite.hasNext()) {
                    Map.Entry<LivingEntity, GooglyTracker> e = ite.next();
                    GooglyTracker tracker = e.getValue();
                    if (ticks - tracker.lastUpdateRequest > 10) {
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

    @SubscribeEvent
    public void reloadCommand(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("googlyeyes").then(Commands.literal("reload").executes(commandContext -> {
            HeadHandler.loadHeadInfos();
            return 0;
        })));
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
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        LayerGooglyEyes layerGooglyEyes = new LayerGooglyEyes(event.getSkin("default"));
        Set<EntityRenderer> addedRenderers = new HashSet<>();

        EntityRenderDispatcher renderManager = event.getContext().getEntityRenderDispatcher();
        if (!(ModConfigClient.disabledGoogly.get().contains("minecraft:player") || ModConfigClient.disabledGoogly.get().contains("player"))) {
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
            for (String s : ModConfigClient.disabledGoogly.get()) {
                ResourceLocation disabled = new ResourceLocation(s);
                if (disabled.equals(rl)) {
                    return;
                }
            }

            if (entityRenderer instanceof LivingEntityRenderer renderer) {
                renderer.addLayer(layerGooglyEyes);
            } else if (entityRenderer instanceof ILayerManager iLayerManager) {
                iLayerManager.addLayer(layerGooglyEyes);
            }
        });
    }
}
