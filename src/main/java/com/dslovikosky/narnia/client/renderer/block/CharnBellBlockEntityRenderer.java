package com.dslovikosky.narnia.client.renderer.block;

import com.dslovikosky.narnia.common.block.CharnBellBlock;
import com.dslovikosky.narnia.common.block_entity.CharnBellBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CharnBellBlockEntityRenderer implements BlockEntityRenderer<CharnBellBlockEntity, CharnBellBlockEntityRenderState> {
    private static final ResourceLocation TEXTURE = Constants.modLocation("textures/block_entity/charn_bell.png");
    private static final CharnBellBlockEntityModel MODEL = new CharnBellBlockEntityModel(RenderType::entityCutout);

    @Override
    public CharnBellBlockEntityRenderState createRenderState() {
        return new CharnBellBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(final CharnBellBlockEntity blockEntity, final CharnBellBlockEntityRenderState renderState, final float partialTick, final Vec3 cameraPosition, @Nullable final ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.hitNorth = blockEntity.isHitNorth();
        renderState.lastHitTime = blockEntity.getLastHitTime();
    }

    @Override
    public void submit(final CharnBellBlockEntityRenderState renderState, final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final CameraRenderState cameraRenderState) {
        nodeCollector.submitCustomGeometry(poseStack, MODEL.renderType(TEXTURE), (pose, consumer) -> {
            final PoseStack stack = new PoseStack();
            stack.pushPose();

            final PoseStack.Pose newPose = stack.last();
            newPose.pose().mul(pose.pose());
            newPose.normal().mul(pose.normal());

            stack.translate(0.5, -0.5, 0.5);
            final Direction direction = renderState.blockState.getValue(CharnBellBlock.FACING);
            final float extraRotation = direction == Direction.EAST || direction == Direction.WEST ? 180 : 0;
            stack.rotateAround(Axis.YP.rotationDegrees(direction.toYRot() + extraRotation), 0, 1, 0);
            stack.rotateAround(Axis.ZP.rotationDegrees(180), 0, 1, 0);

            MODEL.setupAnim(renderState);
            MODEL.renderToBuffer(stack, consumer, renderState.lightCoords, OverlayTexture.NO_OVERLAY, ARGB.color(255, 255, 255));
        });
    }
}
