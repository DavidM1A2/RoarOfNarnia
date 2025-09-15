package com.dslovikosky.narnia.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CharnSkyRenderer extends DimensionSpecialEffects {
    private static final ResourceLocation SUN_TEXTURE = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");
    private static final RenderType SUN_RENDER_TYPE = RenderType.celestial(SUN_TEXTURE);
    private static final int TINT_COLOR = ARGB.color(255, 255, 50, 50); // bright red sun
    private static final float SUN_SIZE = 30f;
    private static final float SUN_Y = 100f;

    public CharnSkyRenderer() {
        super(SkyType.OVERWORLD, false, false);
    }

    @Override
    public boolean renderSky(final ClientLevel level, final int ticks, final float partialTick, final Matrix4f modelViewMatrix, final Camera camera, final Runnable setupFog) {
        final PoseStack pose = new PoseStack();

        pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
        pose.mulPose(Axis.XP.rotationDegrees(90.0F));

        final MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        final VertexConsumer vertexConsumer = bufferSource.getBuffer(SUN_RENDER_TYPE);
        final Matrix4f matrix = pose.last().pose();

        vertexConsumer.addVertex(matrix, -SUN_SIZE, SUN_Y, -SUN_SIZE).setUv(0f, 0f).setColor(TINT_COLOR);
        vertexConsumer.addVertex(matrix, SUN_SIZE, SUN_Y, -SUN_SIZE).setUv(1f, 0f).setColor(TINT_COLOR);
        vertexConsumer.addVertex(matrix, SUN_SIZE, SUN_Y, SUN_SIZE).setUv(1f, 1f).setColor(TINT_COLOR);
        vertexConsumer.addVertex(matrix, -SUN_SIZE, SUN_Y, SUN_SIZE).setUv(0f, 1f).setColor(TINT_COLOR);

        bufferSource.endBatch(SUN_RENDER_TYPE);

        return true;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }
}
