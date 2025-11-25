package com.dslovikosky.narnia.client.renderer.block.base;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class BlockBenchModel<S> extends Model<S> {
    public BlockBenchModel(final LayerDefinition layerDefinition, final Function<ResourceLocation, RenderType> renderType) {
        super(layerDefinition.bakeRoot(), renderType);
    }
}
