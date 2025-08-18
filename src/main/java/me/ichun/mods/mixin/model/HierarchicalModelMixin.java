package me.ichun.mods.mixin.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import me.ichun.mods.ichunutil.api.client.IEntityModelGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HierarchicalModel.class)
public abstract class HierarchicalModelMixin implements IEntityModelGetter {
    @Shadow public abstract ModelPart root();

    @Override
    public ModelPart getRoot() {
        return this.root();
    }
}
