package me.ichun.mods.mixin.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import me.ichun.mods.ichunutil.api.client.IEntityModelGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin implements IEntityModelGetter {
    @Unique
    private ModelPart googlyGooglyEyes$root;
    @Override
    public ModelPart getRoot() {
        return googlyGooglyEyes$root;
    }

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V", at = @At("TAIL"))
    public void setRoot(ModelPart root, Function renderType, CallbackInfo ci){
        this.googlyGooglyEyes$root = root;
    }
}
