package me.ichun.mods.ichunutil.api.common.head.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HeadGhast extends HeadInfo<Ghast> {
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getEyeScale(Ghast living, PoseStack stack, float partialTick, int eye) {
        if (living.isCharging()) {
            return eyeScale;
        }
        return 0F;
    }
}
