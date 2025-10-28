package com.dslovikosky.narnia.client.entity;

import com.dslovikosky.narnia.client.renderer.CustomLateEntityRenderer;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellProjectileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.Color;
import java.util.Random;

public class SpellProjectileRenderer extends EntityRenderer<SpellProjectileEntity, SpellProjectileRenderState> implements CustomLateEntityRenderer<SpellProjectileRenderState> {
    private static final int IN_PLANE_ROTATION_SPEED = 3;
    private static final int PLANE_ROTATION_SPEED = 2;

    // The texture used by the model
    private static final ResourceLocation SPELL_PROJECTILE_TEXTURE = Constants.modLocation("textures/entity/spell/projectile.png");

    public SpellProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Vec3 getRenderOffset(SpellProjectileRenderState renderState) {
        return Vec3.ZERO;
    }

    @Override
    public SpellProjectileRenderState createRenderState() {
        return new SpellProjectileRenderState();
    }

    @Override
    public void extractRenderState(SpellProjectileEntity entity, SpellProjectileRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.setRandom(new Random(entity.getId()));
        reusedState.setColor(entity.getColor());
    }

    @Override
    public void submit(SpellProjectileRenderState renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        poseStack.translate(0.0, renderState.boundingBoxHeight / 2, 0.0);

        final VertexConsumer buffer = multiBufferSource.getBuffer(RenderType.ENTITY_TRANSLUCENT.apply(SPELL_PROJECTILE_TEXTURE, false));

        final float tickCount = renderState.ageInTicks + renderState.partialTick;
        final Color color = renderState.getColor();
        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        setupBaseRotations(poseStack, renderState.getRandom(), tickCount);

        // Setup in-plane rotation
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(tickCount * IN_PLANE_ROTATION_SPEED));
        // Rotate the plane
        poseStack.mulPose(Axis.XP.rotationDegrees(90f));
        drawOneSprite(poseStack, buffer, red, green, blue);
        poseStack.popPose();

        // Setup in-plane rotation
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(tickCount * IN_PLANE_ROTATION_SPEED));
        // Rotate the plane
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
        drawOneSprite(poseStack, buffer, red, green, blue);
        poseStack.popPose();

        // Setup in-plane rotation
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(tickCount * IN_PLANE_ROTATION_SPEED));
        // Rotate the plane
        poseStack.mulPose(Axis.ZP.rotationDegrees(90f));
        drawOneSprite(poseStack, buffer, red, green, blue);
        poseStack.popPose();

        poseStack.popPose();
    }

    private void setupBaseRotations(final PoseStack matrixStack, final Random random, final float tickCount) {
        final float xRotation = (tickCount + ((float) random.nextGaussian() + 1)) * PLANE_ROTATION_SPEED;
        final float yRotation = (tickCount + ((float) random.nextGaussian() + 1)) * PLANE_ROTATION_SPEED;
        final float zRotation = (tickCount + ((float) random.nextGaussian() + 1)) * PLANE_ROTATION_SPEED;

        matrixStack.mulPose((random.nextBoolean() ? Axis.XP : Axis.XN).rotationDegrees(xRotation));
        matrixStack.mulPose((random.nextBoolean() ? Axis.YP : Axis.YN).rotationDegrees(yRotation));
        matrixStack.mulPose((random.nextBoolean() ? Axis.ZP : Axis.ZN).rotationDegrees(zRotation));
    }

    private void drawOneSprite(final PoseStack matrixStack, final VertexConsumer buffer, final int red, final int green, final int blue) {
        final PoseStack.Pose pose = matrixStack.last();
        final Matrix4f rotationMatrix = pose.pose();

        drawVertex(rotationMatrix, pose, buffer, -1, -1, 0, 0f, 0f, red, green, blue);
        drawVertex(rotationMatrix, pose, buffer, 1, -1, 0, 1f, 0f, red, green, blue);
        drawVertex(rotationMatrix, pose, buffer, 1, 1, 0, 1f, 1f, red, green, blue);
        drawVertex(rotationMatrix, pose, buffer, -1, 1, 0, 0f, 1f, red, green, blue);
    }

    private void drawVertex(
            final Matrix4f rotationMatrix,
            final PoseStack.Pose pose,
            final VertexConsumer buffer,
            final double x,
            final double y,
            final double z,
            final float u,
            final float v,
            int r,
            int g,
            int b
    ) {
        buffer
                .addVertex(rotationMatrix, (float) x, (float) y, (float) z)
                .setColor(r, g, b, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setUv2(15, 15)
                .setNormal(pose, 0f, 0f, 1f);
    }
}
