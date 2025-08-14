package me.ichun.mods.ichunutil.api.common.head.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HeadShulker extends HeadInfo<Shulker>
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getEyeScale(Shulker living, PoseStack stack, float partialTick, int eye)
    {
        if(living.getClientPeekAmount(partialTick) <= 0F)
        {
            return 0F;
        }
        return eyeScale;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float[] getHeadJointOffset(Shulker living, PoseStack stack, float partialTick, int head)
    {
        switch (living.getAttachFace())
        {
            case DOWN:
            default:
                break;
            case EAST:
                stack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                stack.mulPose(Axis.XP.rotationDegrees(90.0F));
                stack.translate(1.0F, -1.0F, 0.0F);
                stack.mulPose(Axis.YP.rotationDegrees(180.0F));
                break;
            case WEST:
                stack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                stack.mulPose(Axis.XP.rotationDegrees(90.0F));
                stack.translate(-1.0F, -1.0F, 0.0F);
                stack.mulPose(Axis.YP.rotationDegrees(180.0F));
                break;
            case NORTH:
                stack.mulPose(Axis.XP.rotationDegrees(90.0F));
                stack.translate(0.0F, -1.0F, -1.0F);
                break;
            case SOUTH:
                stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                stack.mulPose(Axis.XP.rotationDegrees(90.0F));
                stack.translate(0.0F, -1.0F, 1.0F);
                break;
            case UP:
                stack.mulPose(Axis.XP.rotationDegrees(180.0F));
                stack.translate(0.0F, -2.0F, 0.0F);
        }
        return super.getHeadJointOffset(living, stack, partialTick, head);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getHeadPitch(Shulker living, PoseStack stack, float partialTick, int head, int eye)
    {
        return 0F;
    }

    @Override
    public float getHeadPitch(Shulker living, float partialTick, int head, int eye)
    {
        return 0F;
    }
}
