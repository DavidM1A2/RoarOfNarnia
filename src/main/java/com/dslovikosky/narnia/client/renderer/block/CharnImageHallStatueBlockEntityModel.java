package com.dslovikosky.narnia.client.renderer.block;

import com.dslovikosky.narnia.client.renderer.block.base.BlockBenchModel;
import com.dslovikosky.narnia.client.renderer.entity.JadisModel;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CharnImageHallStatueBlockEntityModel extends BlockBenchModel<CharnImageHallStatueBlockEntityRenderState> {
    private final KeyframeAnimation sit;

    public CharnImageHallStatueBlockEntityModel(final Function<ResourceLocation, RenderType> renderType) {
        super(getLayerDefinition(), renderType);
        this.sit = JadisModel.SIT.get().bake(root);
    }

    private static LayerDefinition getLayerDefinition() {
        return JadisModel.createBodyLayer();
    }

    @Override
    public void setupAnim(CharnImageHallStatueBlockEntityRenderState renderState) {
        super.setupAnim(renderState);
        sit.apply(0, 1f);
    }
}