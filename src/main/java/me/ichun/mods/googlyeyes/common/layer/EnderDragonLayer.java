package me.ichun.mods.googlyeyes.common.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.ichun.mods.googlyeyes.common.GooglyEyes;
import me.ichun.mods.googlyeyes.common.model.ModelGooglyEye;
import me.ichun.mods.googlyeyes.common.tracker.GooglyTracker;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import me.ichun.mods.ichunutil.api.common.head.HeadInfoDelegate;
import me.ichun.mods.ichunutil.common.head.HeadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.jetbrains.annotations.NotNull;


public class EnderDragonLayer extends LayerGooglyEyes<EnderDragon, EnderDragonRenderer.DragonModel> {
    private static final ResourceLocation TEX_GOOGLY_EYE = new ResourceLocation("googlyeyes", "textures/model/modelgooglyeye.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEX_GOOGLY_EYE);
    private static final RenderType RENDER_TYPE_EYES = RenderType.eyes(TEX_GOOGLY_EYE);
    private static final RenderType RENDER_TYPE_RESET = RenderType.eyes(new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png"));
    private final ModelGooglyEye modelGooglyEye;

    public EnderDragonRenderer.DragonModel parentModel;
    public int renderCount;
    public float lastPartialTick;

    public EnderDragonLayer(RenderLayerParent renderer, EnderDragonRenderer.DragonModel model) {
        super(renderer);
        this.modelGooglyEye = new ModelGooglyEye();

        parentModel = model;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource bufferInUnused, int packedLightIn, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(living instanceof EnderDragon dragon)) {
            return;
        }

        if (lastPartialTick != partialTicks) // new render
        {
            lastPartialTick = partialTicks;
            renderCount = 0;
        }
        renderCount++;

        boolean render  = renderCount == 3;
        if (renderCount == 2 && !(dragon.deathTime > 0)) {
            render = true;
        }

        if (!render) {
            return;
        }

        HeadInfo helper = HeadHandler.getHelper(dragon.getClass());
        if (helper == null || helper.noFaceInfo || helper instanceof HeadInfoDelegate) //Dragons are special, do not allow HeadInfoDelegate.
        {
            return;
        }

        helper.headModel = parentModel.head;

        GooglyTracker tracker = GooglyEyes.eventHandler.getGooglyTracker(dragon, helper);
        tracker.setLastUpdateRequest();
        if (!tracker.shouldRender()) {
            return;
        }
        tracker.requireUpdate();


        int headCount = helper.getHeadCount(living);

        for (int headIndex = 0; headIndex < headCount; headIndex++) {
            stack.pushPose();

            helper.correctPosition(living, stack, lastPartialTick);

            int eyeCount = helper.getEyeCount(living);

            for (int i = 0; i < eyeCount; i++) {
                float eyeScale = helper.getEyeScale(living, stack, lastPartialTick, i);

                if (eyeScale <= 0F) {
                    continue;
                }

                stack.pushPose();

                float[] eyes = helper.getEyeOffsetFromJoint(living, stack, lastPartialTick, i);
                stack.translate(-(eyes[0] + helper.getEyeSideOffset(living, stack, lastPartialTick, i)), -eyes[1], -eyes[2]);

                stack.mulPose(Axis.YP.rotationDegrees(helper.getEyeRotation(living, stack, lastPartialTick, i)));
                stack.mulPose(Axis.XP.rotationDegrees(helper.getEyeTopRotation(living, stack, lastPartialTick, i)));

                stack.scale(eyeScale, eyeScale, eyeScale * 0.5F);

                MultiBufferSource.BufferSource bufferIn = Minecraft.getInstance().renderBuffers().bufferSource();

                VertexConsumer buffer = bufferIn.getBuffer(RENDER_TYPE);

                int overlay = LivingEntityRenderer.getOverlayCoords(living, 0.0F);

                float[] corneaColours = helper.getCorneaColours(living, stack, lastPartialTick, i);
                modelGooglyEye.renderCornea(stack, buffer, packedLightIn, overlay, corneaColours[0], corneaColours[1], corneaColours[2], 1F);

                float[] irisColours = helper.getIrisColours(living, stack, lastPartialTick, i);

                float irisScale = helper.getIrisScale(living, stack, lastPartialTick, i);
                stack.pushPose();
                stack.scale(irisScale, irisScale, 1F);
                modelGooglyEye.moveIris(tracker.eyes[0][i].prevDeltaX + (tracker.eyes[0][i].deltaX - tracker.eyes[0][i].prevDeltaX) * lastPartialTick, tracker.eyes[0][i].prevDeltaY + (tracker.eyes[0][i].deltaY - tracker.eyes[0][i].prevDeltaY) * lastPartialTick, irisScale);
                modelGooglyEye.renderIris(stack, buffer, packedLightIn, overlay, irisColours[0], irisColours[1], irisColours[2], 1F);
                stack.popPose();

                if (helper.doesEyeGlow(living, i)) {
                    buffer = bufferIn.getBuffer(RENDER_TYPE_EYES);
                    modelGooglyEye.renderCornea(stack, buffer, packedLightIn, overlay, corneaColours[0], corneaColours[1], corneaColours[2], 1F);

                    stack.pushPose();
                    stack.scale(irisScale, irisScale, 1F);
                    modelGooglyEye.renderIris(stack, buffer, packedLightIn, overlay, irisColours[0], irisColours[1], irisColours[2], 1F);
                    stack.popPose();
                }

                bufferIn.getBuffer(RENDER_TYPE_RESET);
                stack.popPose();
            }

            stack.popPose();
        }
    }
}
