package me.ichun.mods.ichunutil.api.common.head.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HeadMagmaCube extends HeadInfo<MagmaCube> {
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getEyeScale(MagmaCube living, PoseStack stack, float partialTick, int eye) {
        float squish = living.oSquish + (living.squish - living.oSquish) * partialTick;
        if (squish <= 0F) {
            return eyeScale;
        } else {
            return eyeScale + squish * 1.5F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float[] getHeadJointOffset(MagmaCube living, PoseStack stack, float partialTick, int head) {
        float squish = living.oSquish + (living.squish - living.oSquish) * partialTick;
        if (squish <= 0F) {
            return super.getHeadJointOffset(living, stack, partialTick, head);
        } else {
            return new float[]{0F, -(0 - squish * 2.5F) / 16F, 0F};
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getHeadArmorScale(MagmaCube living, PoseStack stack, float partialTick, int head) {
        float squish = living.oSquish + (living.squish - living.oSquish) * partialTick;
        if (squish > 0F) {
            stack.scale(1F, 1F + squish * 4F, 1F);
        }

        return super.getHeadArmorScale(living, stack, partialTick, head);
    }
}
