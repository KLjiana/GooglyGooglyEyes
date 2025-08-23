package me.ichun.mods.mixin;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import me.ichun.mods.ichunutil.api.client.ILayerManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnderDragonRenderer.class)
public class EnderDragonRendererMixin implements RenderLayerParent<EnderDragon, EnderDragonRenderer.DragonModel>, ILayerManager<EnderDragon, EnderDragonRenderer.DragonModel> {
    @Unique protected final List<RenderLayer<EnderDragon, EnderDragonRenderer.DragonModel>> layers = Lists.newArrayList();

    @Shadow @Final public EnderDragonRenderer.DragonModel model;

    @Shadow @Final private static ResourceLocation DRAGON_LOCATION;

    @Override
    public EnderDragonRenderer.@NotNull DragonModel getModel() {
        return model;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EnderDragon entity) {
        return DRAGON_LOCATION;
    }

    @Override
    public boolean addLayer(RenderLayer layer) {
        return layers.add(layer);
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V",
                    ordinal = 1
            )
    )
    public void renderLayer(EnderDragon entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!entity.isSpectator()) {
            for(var renderlayer : this.layers) {
                renderlayer.render(poseStack, buffer, packedLight, entity, 0, 0, partialTicks, 0, 0, 0);
            }
        }
    }
}
