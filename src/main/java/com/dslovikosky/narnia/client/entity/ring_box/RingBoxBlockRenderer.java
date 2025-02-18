package com.dslovikosky.narnia.client.entity.ring_box;

import com.dslovikosky.narnia.common.block.entity.RingBoxBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RingBoxBlockRenderer implements BlockEntityRenderer<RingBoxBlockEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/ring_box.png");
    private final RingBoxModel model;

    public RingBoxBlockRenderer(final BlockEntityRendererProvider.Context context) {
        model = new RingBoxModel(context.bakeLayer(RingBoxModel.LAYER_LOCATION));
    }

    @Override
    public void render(final RingBoxBlockEntity ringBoxBlockEntity, float partialTicks, final PoseStack poseStack, final MultiBufferSource bufferSource, final int packedLight, final int packedOverlay) {
        model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(TEXTURE_LOCATION)), packedLight, packedOverlay);
    }
}
