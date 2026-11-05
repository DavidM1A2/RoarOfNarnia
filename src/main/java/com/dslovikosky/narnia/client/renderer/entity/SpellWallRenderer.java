package com.dslovikosky.narnia.client.renderer.entity;

import com.dslovikosky.narnia.client.constants.ModRenderPipelines;
import com.dslovikosky.narnia.client.renderer.SpellGeometrySubmitter;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellWallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Color;

@ParametersAreNonnullByDefault
public class SpellWallRenderer extends EntityRenderer<SpellWallEntity, SpellWallRenderState> {
    // The texture used by the model
    private static final Identifier SPELL_CONE_TEXTURE = Constants.modLocation("textures/entity/spell/wall.png");
    // 1.21.11 moved line width into the vertex format; 1.0 matches the previous global default
    private static final float BORDER_LINE_WIDTH = 1.0F;
    private static final RenderType WALL_RENDER_TYPE = RenderType.create(
            Constants.modLocation("spell_wall").toString(),
            RenderSetup.builder(ModRenderPipelines.SPELL_ENTITY)
                    .affectsCrumbling()
                    .sortOnUpload()
                    .withTexture("Sampler0", SPELL_CONE_TEXTURE)
                    .useOverlay()
                    .createRenderSetup());
    private static final RenderType WALL_BORDER_RENDER_TYPE = RenderType.create(
            Constants.modLocation("spell_wall_corner_border").toString(),
            RenderSetup.builder(RenderPipelines.LINES)
                    .affectsCrumbling()
                    .sortOnUpload()
                    .useOverlay()
                    .createRenderSetup());

    public SpellWallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Vec3 getRenderOffset(SpellWallRenderState renderState) {
        return Vec3.ZERO;
    }

    @Override
    protected AABB getBoundingBoxForCulling(final SpellWallEntity spellWallEntity) {
        return spellWallEntity.getBoundingBoxForCulling();
    }

    @Override
    public SpellWallRenderState createRenderState() {
        return new SpellWallRenderState();
    }

    @Override
    public void extractRenderState(SpellWallEntity entity, SpellWallRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.setColor(entity.getColor());
        reusedState.setWidth(entity.getWidth());
        reusedState.setHeight(entity.getHeight());
        reusedState.setLifespanTicks(entity.getLifespanTicks());
    }

    @Override
    public void submit(SpellWallRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);

        final Vec3 width = renderState.getWidth();
        final Vec3 height = renderState.getHeight();
        final Color color = renderState.getColor();
        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        final float lifespan = renderState.getLifespanTicks();
        if (lifespan <= 0) {
            return;
        }
        final float ticksRemaining = renderState.ageInTicks;
        final float percentRemaining = Mth.clamp(ticksRemaining / lifespan, 0f, 1f);
        final int innerAlpha = Math.round(Mth.lerp(percentRemaining, 140f, 0f));
        final int outerAlpha = Math.round(Mth.lerp(percentRemaining, 255f, 0f));

        final double halfWidthX = width.x / 2;
        final double halfWidthY = width.y / 2;
        final double halfWidthZ = width.z / 2;

        final double halfHeightX = height.x / 2;
        final double halfHeightY = height.y / 2;
        final double halfHeightZ = height.z / 2;

        final float uTileCount = (float) Math.floor(width.length());
        final float vTileCount = (float) Math.floor(height.length());

        SpellGeometrySubmitter.submitAfterTerrain(submitNodeCollector, poseStack, WALL_RENDER_TYPE, (pose, wallBuffer) -> {
            final Matrix4f rotationMatrix = pose.pose();
            drawQuadVertex(rotationMatrix, pose, wallBuffer, -halfWidthX + halfHeightX, -halfWidthY + halfHeightY, -halfWidthZ + halfHeightZ, 0f, 0f, red, green, blue, innerAlpha);
            drawQuadVertex(rotationMatrix, pose, wallBuffer, halfWidthX + halfHeightX, halfWidthY + halfHeightY, halfWidthZ + halfHeightZ, uTileCount, 0f, red, green, blue, innerAlpha);
            drawQuadVertex(rotationMatrix, pose, wallBuffer, halfWidthX - halfHeightX, halfWidthY - halfHeightY, halfWidthZ - halfHeightZ, uTileCount, vTileCount, red, green, blue, innerAlpha);
            drawQuadVertex(rotationMatrix, pose, wallBuffer, -halfWidthX - halfHeightX, -halfWidthY - halfHeightY, -halfWidthZ - halfHeightZ, 0f, vTileCount, red, green, blue, innerAlpha);
        });

        // RenderPipelines.LINES draws independent segments, so each edge is emitted as its own vertex pair
        final Vec3 topLeft = new Vec3(-halfWidthX + halfHeightX, -halfWidthY + halfHeightY, -halfWidthZ + halfHeightZ);
        final Vec3 topRight = new Vec3(halfWidthX + halfHeightX, halfWidthY + halfHeightY, halfWidthZ + halfHeightZ);
        final Vec3 bottomRight = new Vec3(halfWidthX - halfHeightX, halfWidthY - halfHeightY, halfWidthZ - halfHeightZ);
        final Vec3 bottomLeft = new Vec3(-halfWidthX - halfHeightX, -halfWidthY - halfHeightY, -halfWidthZ - halfHeightZ);
        SpellGeometrySubmitter.submitAfterTerrain(submitNodeCollector, poseStack, WALL_BORDER_RENDER_TYPE, (pose, borderBuffer) -> {
            final Matrix4f rotationMatrix = pose.pose();
            drawBorderEdge(rotationMatrix, pose, borderBuffer, topLeft, topRight, red, green, blue, outerAlpha);
            drawBorderEdge(rotationMatrix, pose, borderBuffer, topRight, bottomRight, red, green, blue, outerAlpha);
            drawBorderEdge(rotationMatrix, pose, borderBuffer, bottomRight, bottomLeft, red, green, blue, outerAlpha);
        });
    }

    private void drawBorderEdge(
            final Matrix4f rotationMatrix,
            final PoseStack.Pose pose,
            final VertexConsumer buffer,
            final Vec3 from,
            final Vec3 to,
            final int r,
            final int g,
            final int b,
            final int a
    ) {
        drawBorderVertex(rotationMatrix, pose, buffer, from.x(), from.y(), from.z(), r, g, b, a);
        drawBorderVertex(rotationMatrix, pose, buffer, to.x(), to.y(), to.z(), r, g, b, a);
    }

    private void drawQuadVertex(
            final Matrix4f rotationMatrix,
            final PoseStack.Pose pose,
            final VertexConsumer buffer,
            final double x,
            final double y,
            final double z,
            final float u,
            final float v,
            final int r,
            final int g,
            final int b,
            final int a
    ) {
        buffer
                .addVertex(rotationMatrix, (float) x, (float) y, (float) z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightCoordsUtil.FULL_BRIGHT)
                .setNormal(pose, 0f, 0f, 1f);
    }

    private void drawBorderVertex(
            final Matrix4f rotationMatrix,
            final PoseStack.Pose pose,
            final VertexConsumer buffer,
            final double x,
            final double y,
            final double z,
            final int r,
            final int g,
            final int b,
            final int a
    ) {
        buffer
                .addVertex(rotationMatrix, (float) x, (float) y, (float) z)
                .setColor(r, g, b, a)
                .setNormal(pose, 0f, 0f, 1f)
                .setLineWidth(BORDER_LINE_WIDTH);
    }
}
