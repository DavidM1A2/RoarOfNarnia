package com.dslovikosky.narnia.client.renderer.entity;

import com.dslovikosky.narnia.client.constants.ModRenderPipelines;
import com.dslovikosky.narnia.client.renderer.CustomLateEntityRenderer;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellWallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.Color;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpellWallRenderer extends EntityRenderer<SpellWallEntity, SpellWallRenderState> implements CustomLateEntityRenderer<SpellWallRenderState> {
    // The texture used by the model
    private static final ResourceLocation SPELL_CONE_TEXTURE = Constants.modLocation("textures/entity/spell/wall.png");
    private static final RenderType WALL_RENDER_TYPE = RenderType.create(
            Constants.modLocation("spell_wall").toString(),
            1536,
            true,
            true,
            ModRenderPipelines.SPELL_ENTITY,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(SPELL_CONE_TEXTURE, false))
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false));
    private static final RenderType WALL_BORDER_RENDER_TYPE = RenderType.create(
            Constants.modLocation("spell_wall_corner_border").toString(),
            1536,
            true,
            true,
            RenderPipelines.LINE_STRIP,
            RenderType.CompositeState.builder()
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false));

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
    public void submit(SpellWallRenderState renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, CameraRenderState cameraRenderState) {
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

        final Matrix4f rotationMatrix = poseStack.last().pose();
        final PoseStack.Pose pose = poseStack.last();

        final double halfWidthX = width.x / 2;
        final double halfWidthY = width.y / 2;
        final double halfWidthZ = width.z / 2;

        final double halfHeightX = height.x / 2;
        final double halfHeightY = height.y / 2;
        final double halfHeightZ = height.z / 2;

        final float uTileCount = (float) Math.floor(width.length());
        final float vTileCount = (float) Math.floor(height.length());

        final VertexConsumer wallBuffer = multiBufferSource.getBuffer(WALL_RENDER_TYPE);
        drawQuadVertex(rotationMatrix, pose, wallBuffer, -halfWidthX + halfHeightX, -halfWidthY + halfHeightY, -halfWidthZ + halfHeightZ, 0f, 0f, red, green, blue, innerAlpha);
        drawQuadVertex(rotationMatrix, pose, wallBuffer, halfWidthX + halfHeightX, halfWidthY + halfHeightY, halfWidthZ + halfHeightZ, uTileCount, 0f, red, green, blue, innerAlpha);
        drawQuadVertex(rotationMatrix, pose, wallBuffer, halfWidthX - halfHeightX, halfWidthY - halfHeightY, halfWidthZ - halfHeightZ, uTileCount, vTileCount, red, green, blue, innerAlpha);
        drawQuadVertex(rotationMatrix, pose, wallBuffer, -halfWidthX - halfHeightX, -halfWidthY - halfHeightY, -halfWidthZ - halfHeightZ, 0f, vTileCount, red, green, blue, innerAlpha);

        final VertexConsumer borderBuffer = multiBufferSource.getBuffer(WALL_BORDER_RENDER_TYPE);
        drawBorderVertex(rotationMatrix, pose, borderBuffer, -halfWidthX + halfHeightX, -halfWidthY + halfHeightY, -halfWidthZ + halfHeightZ, red, green, blue, outerAlpha);
        drawBorderVertex(rotationMatrix, pose, borderBuffer, halfWidthX + halfHeightX, halfWidthY + halfHeightY, halfWidthZ + halfHeightZ, red, green, blue, outerAlpha);
        drawBorderVertex(rotationMatrix, pose, borderBuffer, halfWidthX - halfHeightX, halfWidthY - halfHeightY, halfWidthZ - halfHeightZ, red, green, blue, outerAlpha);
        drawBorderVertex(rotationMatrix, pose, borderBuffer, -halfWidthX - halfHeightX, -halfWidthY - halfHeightY, -halfWidthZ - halfHeightZ, red, green, blue, outerAlpha);
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
                .setLight(LightTexture.FULL_BRIGHT)
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
                .setNormal(pose, 0f, 0f, 1f);
    }
}
