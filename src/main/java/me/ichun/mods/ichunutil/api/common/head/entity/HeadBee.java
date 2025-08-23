package me.ichun.mods.ichunutil.api.common.head.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.ichun.mods.ichunutil.api.common.head.HeadInfo;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HeadBee extends HeadInfo<Bee>
{
    public float[] irisColourAngry = new float[] { 228F / 255F , 0F / 255F, 24F / 255F };
    public float[] pupilColourAngry = new float[] { 241F / 255F , 242F / 255F, 224F / 255F };

    @OnlyIn(Dist.CLIENT)
    @Override
    public void preChildEntHeadRenderCalls(Bee living, PoseStack stack, EntityRenderer<Bee> render, EntityModel model)
    {
        if(living.isBaby()) //I don't like this if statement any more than you do.
        {
            float modelScale = 0.0625F;
            if(model instanceof AgeableListModel<?>)
            {
                AgeableListModel<?> ageableModel = (AgeableListModel<?>)model;
                float f = 1.0F / ageableModel.babyHeadScale;
                stack.scale(f, f, f);
                stack.translate(0.0F, ageableModel.babyYHeadOffset * modelScale, 0.0F);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float[] getCorneaColours(Bee living, PoseStack stack, float partialTick, int eye)
    {
        if(living.isAngry()) //func_233678_J__
        {
            return irisColourAngry;
        }
        return corneaColour;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float[] getIrisColours(Bee living, PoseStack stack, float partialTick, int eye)
    {
        if(living.isAngry()) //func_233678_J__
        {
            return pupilColourAngry;
        }
        return irisColour;
    }
}
