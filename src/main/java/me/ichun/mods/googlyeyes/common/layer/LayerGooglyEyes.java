package me.ichun.mods.googlyeyes.common.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.ichun.mods.googlyeyes.common.GooglyEyes;
import me.ichun.mods.googlyeyes.common.model.ModelGooglyEye;
import me.ichun.mods.googlyeyes.common.tracker.GooglyTracker;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import me.ichun.mods.ichunutil.common.head.HeadHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class LayerGooglyEyes<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEX_GOOGLY_EYE = new ResourceLocation("googlyeyes", "textures/model/modelgooglyeye.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEX_GOOGLY_EYE);
    private static final RenderType RENDER_TYPE_EYES = RenderType.eyes(TEX_GOOGLY_EYE);
    private final ModelGooglyEye modelGooglyEye;

    public LayerGooglyEyes(RenderLayerParent<T, M> renderer) {
        super(renderer); // nonnull, we'll just pass the player renderer
        this.modelGooglyEye = new ModelGooglyEye();
    }

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource bufferIn, int packedLightIn, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        HeadInfo parentHelper = HeadHandler.getHelper(living.getClass());
        if (parentHelper != null) {
            EntityRenderer<?> render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(living);
            if (!(render instanceof LivingEntityRenderer renderer)) {
                return;
            }

            if (!parentHelper.setup(living, renderer)) {
                return;
            }

            GooglyTracker tracker = GooglyEyes.eventHandler.getGooglyTracker(living, parentHelper);
            tracker.setLastUpdateRequest();
            if (!tracker.shouldRender()) {
                return;
            }
            tracker.requireUpdate();

            int headCount = parentHelper.getHeadCount(living);

            for (int headIndex = 0; headIndex < headCount; headIndex++) {
                HeadInfo helper = parentHelper.getHeadInfo(living, headIndex);

                if (helper.noFaceInfo) {
                    continue;
                }

                helper.setHeadModel(living, renderer);
                if (helper.headModel == null) {
                    continue;
                }

                stack.pushPose();

                helper.correctPosition(living, stack, partialTicks);

                int eyeCount = helper.getEyeCount(living);

                for (int eyeIndex = 0; eyeIndex < eyeCount; eyeIndex++) {
                    if (living.isInvisible() && helper.affectedByInvisibility(living, eyeIndex)) {
                        continue;
                    }

                    float eyeScale = helper.getEyeScale(living, stack, partialTicks, eyeIndex);

                    if (eyeScale <= 0F) {
                        continue;
                    }

                    stack.pushPose();

                    // thepatcat: Creatures only get googly eyes in adulthood. It's science.
                    helper.preChildEntHeadRenderCalls(living, stack, renderer);

                    float[] joint = helper.getHeadJointOffset(living, stack, partialTicks, headIndex);
                    stack.translate(-joint[0], -joint[1], -joint[2]);

                    stack.mulPose(Axis.ZP.rotationDegrees(helper.getHeadRoll(living, stack, partialTicks, headIndex, eyeIndex)));
                    stack.mulPose(Axis.YP.rotationDegrees(helper.getHeadYaw(living, stack, partialTicks, headIndex, eyeIndex)));
                    stack.mulPose(Axis.XP.rotationDegrees(helper.getHeadPitch(living, stack, partialTicks, headIndex, eyeIndex)));

                    //真的有用吗
//                    helper.postHeadTranslation(living, stack, partialTicks);

                    float[] eyes = helper.getEyeOffsetFromJoint(living, stack, partialTicks, eyeIndex);
                    stack.translate(-(eyes[0] + helper.getEyeSideOffset(living, stack, partialTicks, eyeIndex)), -eyes[1], -eyes[2]);

                    stack.mulPose(Axis.YP.rotationDegrees(helper.getEyeRotation(living, stack, partialTicks, eyeIndex)));
                    stack.mulPose(Axis.XP.rotationDegrees(helper.getEyeTopRotation(living, stack, partialTicks, eyeIndex)));

                    stack.scale(eyeScale, eyeScale, eyeScale * 0.4F);

                    //rendering the eyes
                    VertexConsumer buffer = bufferIn.getBuffer(RENDER_TYPE);

                    int overlay = LivingEntityRenderer.getOverlayCoords(living, 0.0F);

                    float[] corneaColours = helper.getCorneaColours(living, stack, partialTicks, eyeIndex);
                    modelGooglyEye.renderCornea(stack, buffer, packedLightIn, overlay, corneaColours[0], corneaColours[1], corneaColours[2], 1F);

                    float[] irisColours = helper.getIrisColours(living, stack, partialTicks, eyeIndex);

                    float irisScale = helper.getIrisScale(living, stack, partialTicks, eyeIndex);
                    stack.pushPose();
                    stack.scale(irisScale, irisScale, 1F);
                    modelGooglyEye.moveIris(tracker.eyes[headIndex][eyeIndex].prevDeltaX + (tracker.eyes[headIndex][eyeIndex].deltaX - tracker.eyes[headIndex][eyeIndex].prevDeltaX) * partialTicks, tracker.eyes[headIndex][eyeIndex].prevDeltaY + (tracker.eyes[headIndex][eyeIndex].deltaY - tracker.eyes[headIndex][eyeIndex].prevDeltaY) * partialTicks, irisScale);
                    modelGooglyEye.renderIris(stack, buffer, packedLightIn, overlay, irisColours[0], irisColours[1], irisColours[2], 1F);
                    stack.popPose();

                    if (helper.doesEyeGlow(living, eyeIndex)) {
                        buffer = bufferIn.getBuffer(RENDER_TYPE_EYES);
                        modelGooglyEye.renderCornea(stack, buffer, packedLightIn, overlay, corneaColours[0], corneaColours[1], corneaColours[2], 1F);

                        stack.pushPose();
                        stack.scale(irisScale, irisScale, 1F);
                        modelGooglyEye.renderIris(stack, buffer, packedLightIn, overlay, irisColours[0], irisColours[1], irisColours[2], 1F);
                        stack.popPose();
                    }
                    //end rendering the eyes

                    stack.popPose();
                }

                stack.popPose();
            }
        }
    }
}
