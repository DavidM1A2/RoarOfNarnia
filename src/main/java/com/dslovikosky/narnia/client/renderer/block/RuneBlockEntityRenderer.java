package com.dslovikosky.narnia.client.renderer.block;

import com.dslovikosky.narnia.common.block.RuneBlock;
import com.dslovikosky.narnia.common.block_entity.RuneBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
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
    }

    @Override
    public void submit(final RuneBlockEntityRenderState renderState, final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final CameraRenderState cameraRenderState) {
        final Block block = renderState.blockState.getBlock();
        if (!(block instanceof RuneBlock runeBlock)) {
            return;
        }
        final RuneBlock.Color color = runeBlock.getColor();
        final ResourceLocation texture = Constants.modLocation("textures/block_entity/" + color.getName() + "_rune.png");

        poseStack.pushPose();
        poseStack.translate(0.0, 0.5, 0.0);
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.tickCount * 4 % 360));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.translate(-0.5, 0.0, -0.5);
        nodeCollector.submitCustomGeometry(poseStack, RenderType.entityCutoutNoCull(texture), (pose, consumer) -> {
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
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
    }
}
