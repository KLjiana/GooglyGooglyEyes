package me.ichun.mods.ichunutil.api.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;

public interface ILayerManager<T extends Entity, M extends EntityModel<T>> {
    boolean addLayer(RenderLayer<T, M> layer);
}
