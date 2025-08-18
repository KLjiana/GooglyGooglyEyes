package me.ichun.mods.mixin.model;

import me.ichun.mods.ichunutil.api.client.IEntityModelGetter;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuadrupedModel.class)
public class QuadrupedModelMixin implements IEntityModelGetter {
    @Unique
    private ModelPart googlyGooglyEyes$root;

    @Override
    public ModelPart getRoot() {
        return googlyGooglyEyes$root;
    }

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;ZFFFFI)V", at = @At("TAIL"))
    public void setRoot(ModelPart root, boolean scaleHead, float babyYHeadOffset, float babyZHeadOffset, float babyHeadScale, float babyBodyScale, int bodyYOffset, CallbackInfo ci) {
        this.googlyGooglyEyes$root = root;
    }
}
