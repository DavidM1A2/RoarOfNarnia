package com.dslovikosky.narnia.client.entity.ring_box;

import com.dslovikosky.narnia.common.block.entity.RingBoxBlockEntity;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RingBoxItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final RingBoxBlockEntity DEFAULT_RING_BOX = new RingBoxBlockEntity(BlockPos.ZERO, ModBlocks.RING_BOX.get().defaultBlockState());
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

    public RingBoxItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(final ItemStack itemStack, final ItemDisplayContext displayContext, final PoseStack poseStack, final MultiBufferSource bufferSource, final int packedLight, final int packedOverlay) {
        blockEntityRenderDispatcher.getRenderer(DEFAULT_RING_BOX)
                .render(DEFAULT_RING_BOX, 0f, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
