package com.dslovikosky.narnia.client.renderer.block.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public abstract class BlockBenchModel {
    private final ModelPart root;

    public BlockBenchModel() {
        this.root = getLayerDefinition().bakeRoot();
    }

    protected abstract LayerDefinition getLayerDefinition();

    public void renderToBuffer(final PoseStack poseStack, final VertexConsumer vertexConsumer, final int packedLight, final int packedOverlay, final int color) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
