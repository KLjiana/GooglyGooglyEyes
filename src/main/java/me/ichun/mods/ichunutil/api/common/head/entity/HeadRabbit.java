package me.ichun.mods.ichunutil.api.common.head.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HeadRabbit extends HeadInfo<Rabbit>
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void preChildEntHeadRenderCalls(Rabbit living, PoseStack stack, EntityRenderer<Rabbit> render, EntityModel model)
    {
        float scale = 0.0625F;
        if(living.isBaby())
        {
            stack.scale(0.56666666F, 0.56666666F, 0.56666666F);
            stack.translate(0.0F, 22.0F * scale, 2.0F * scale);
        }
        else
        {
            stack.scale(0.6F, 0.6F, 0.6F);
            stack.translate(0.0F, 16.0F * scale, 0.0F);
        }
    }
}
