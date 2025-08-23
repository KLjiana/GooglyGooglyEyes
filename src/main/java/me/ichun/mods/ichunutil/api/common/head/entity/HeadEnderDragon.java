package me.ichun.mods.ichunutil.api.common.head.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class HeadEnderDragon extends HeadInfo<EnderDragon> {
    
    @Override
    public float getHeadYaw(EnderDragon living, PoseStack stack, float partialTick, int head, int eye) {
        return 0F;
    }

    @Override
    public float getHeadPitch(EnderDragon living, PoseStack stack, float partialTick, int head, int eye) {
        return 0F;
    }

    @Override
    public float getHeadRoll(EnderDragon living, PoseStack stack, float partialTick, int head, int eye) {
        return 0F;
    }
//
//    @Override
//    public float getHeadYaw(EnderDragon living, float partialTick, int head, int eye) {
//        return 0;
//    }
//
//    @Override
//    public float getHeadPitch(EnderDragon living, float partialTick, int head, int eye) {
//        return 0;
//    }
//
//    @Override
//    public float getHeadRoll(EnderDragon living, float partialTick, int head, int eye) {
//        return 0;
//    }
}