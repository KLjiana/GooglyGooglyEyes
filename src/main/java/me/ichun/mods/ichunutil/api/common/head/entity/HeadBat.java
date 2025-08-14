package me.ichun.mods.ichunutil.api.common.head.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HeadBat extends HeadInfo<Bat>
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public float getHeadYaw(Bat living, PoseStack stack, float partialTick, int head, int eye)
    {
        if(living.isResting())
        {
            return -super.getHeadYaw(living, stack, partialTick, head, eye);
        }
        else
        {
            return super.getHeadYaw(living, stack, partialTick, head, eye);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getHeadYaw(Bat living, float partialTick, int head, int eye)
    {
        if(living.isResting())
        {
            return -super.getHeadYaw(living, partialTick, head, eye);
        }
        else
        {
            return super.getHeadYaw(living, partialTick, head, eye);
        }
    }
}
