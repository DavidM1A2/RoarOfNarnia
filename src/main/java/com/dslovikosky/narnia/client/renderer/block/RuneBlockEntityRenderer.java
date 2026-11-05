package com.dslovikosky.narnia.client.renderer.block;

import com.dslovikosky.narnia.common.block.RuneBlock;
import com.dslovikosky.narnia.common.block_entity.RuneBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RuneBlockEntityRenderer implements BlockEntityRenderer<RuneBlockEntity, RuneBlockEntityRenderState> {
    @Override
    public RuneBlockEntityRenderState createRenderState() {
        return new RuneBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(final RuneBlockEntity blockEntity, final RuneBlockEntityRenderState renderState, final float partialTick, final Vec3 cameraPosition, @Nullable final ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        final Level level = blockEntity.getLevel();
        renderState.tickCount = level == null ? 0 : level.getGameTime() + partialTick;
        renderState.color = blockEntity.getBlockState().getBlock() instanceof RuneBlock runeBlock ? runeBlock.getColor() : null;
    }

    @Override
    public void submit(final RuneBlockEntityRenderState renderState, final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final CameraRenderState cameraRenderState) {
        final RuneBlock.Color color = renderState.color;
        if (color == null) {
            return;
        }
        final Identifier texture = Constants.modLocation("textures/block_entity/" + color.getName() + "_rune.png");

        poseStack.pushPose();
        poseStack.translate(0.0, 0.5, 0.0);
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(renderState.tickCount * 4 % 360));
        poseStack.rotate(Axis.XP.rotationDegrees(90));
        poseStack.translate(-0.5, 0.0, -0.5);
        nodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture), (pose, consumer) -> {
            drawQuadVertex(pose.pose(), pose, consumer, 0.0, 0.0, 0.0, 0, 0);
            drawQuadVertex(pose.pose(), pose, consumer, 1.0, 0.0, 0.0, 1, 0);
            drawQuadVertex(pose.pose(), pose, consumer, 1.0, 0.0, 1.0, 1, 1);
            drawQuadVertex(pose.pose(), pose, consumer, 0.0, 0.0, 1.0, 0, 1);
        });
        poseStack.popPose();
    }

    private void drawQuadVertex(
            final Matrix4f rotationMatrix,
            final PoseStack.Pose pose,
            final VertexConsumer buffer,
            final double x,
            final double y,
            final double z,
            final float u,
            final float v
    ) {
        buffer
                .addVertex(rotationMatrix, (float) x, (float) y, (float) z)
                .setColor(1.0f, 1.0f, 1.0f, 1.0f)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightCoordsUtil.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
    }
}
