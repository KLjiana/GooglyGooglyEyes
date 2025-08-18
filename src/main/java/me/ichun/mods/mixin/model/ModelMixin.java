package me.ichun.mods.mixin.model;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import me.ichun.mods.ichunutil.api.client.IEntityModelGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {AxolotlModel.class,
        BeeModel.class,
        BoatModel.class,
        ChickenModel.class,
        EnderDragonRenderer.DragonModel.class,
        ElytraModel.class,
        FoxModel.class,
        HoglinModel.class,
        HorseModel.class,
        LlamaModel.class,
        OcelotModel.class,
        RabbitModel.class,
        RaftModel.class,
        ShulkerModel.class,
        TadpoleModel.class,
        WolfModel.class
})
public class ModelMixin implements IEntityModelGetter {
    @Unique
    private ModelPart googlyGooglyEyes$root;

    @Override
    public ModelPart getRoot() {
        return googlyGooglyEyes$root;
    }

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", at = @At("TAIL"))
    public void setRoot(ModelPart root, CallbackInfo ci){
        this.googlyGooglyEyes$root = root;
    }
}
