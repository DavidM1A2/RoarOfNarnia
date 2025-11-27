package com.dslovikosky.narnia.client.renderer.block;

import com.dslovikosky.narnia.common.block.CharnBellBlock;
import com.dslovikosky.narnia.common.block.CharnImageHallStatueBlock;
import com.dslovikosky.narnia.common.block_entity.CharnImageHallStatueBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.CharnStatueType;
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
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CharnImageHallStatueBlockEntityRenderer implements BlockEntityRenderer<CharnImageHallStatueBlockEntity, CharnImageHallStatueBlockEntityRenderState> {
    private static final Map<CharnStatueType, ResourceLocation> TEXTURES = Map.of(
            CharnStatueType.JADIS, Constants.modLocation("textures/entity/jadis.png"),
            CharnStatueType.OTHER_1, Constants.modLocation("textures/entity/other_1.png")
    );
    private static final CharnImageHallStatueBlockEntityModel MODEL = new CharnImageHallStatueBlockEntityModel(RenderType::entityCutout);

    @Override
    public CharnImageHallStatueBlockEntityRenderState createRenderState() {
        return new CharnImageHallStatueBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(final CharnImageHallStatueBlockEntity blockEntity, final CharnImageHallStatueBlockEntityRenderState renderState, final float partialTick, final Vec3 cameraPosition, @Nullable final ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
    }

    @Override
    public void submit(final CharnImageHallStatueBlockEntityRenderState renderState, final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final CameraRenderState cameraRenderState) {
        nodeCollector.submitCustomGeometry(poseStack, MODEL.renderType(TEXTURES.get(renderState.blockState.getValue(CharnImageHallStatueBlock.TYPE))), (pose, consumer) -> {
            final PoseStack stack = new PoseStack();
            stack.pushPose();

            final PoseStack.Pose newPose = stack.last();
            newPose.pose().mul(pose.pose());
            newPose.normal().mul(pose.normal());

            stack.translate(0.5, -0.5, 0.5);
            final Direction direction = renderState.blockState.getValue(CharnBellBlock.FACING).getOpposite();
            final float extraRotation = direction == Direction.EAST || direction == Direction.WEST ? 180 : 0;
            stack.rotateAround(Axis.YP.rotationDegrees(direction.toYRot() + extraRotation), 0, 1, 0);
            stack.rotateAround(Axis.ZP.rotationDegrees(180), 0, 1, 0);

            MODEL.setupAnim(renderState);
            MODEL.renderToBuffer(stack, consumer, renderState.lightCoords, OverlayTexture.NO_OVERLAY, ARGB.color(255, 255, 255));
        });
    }
}
