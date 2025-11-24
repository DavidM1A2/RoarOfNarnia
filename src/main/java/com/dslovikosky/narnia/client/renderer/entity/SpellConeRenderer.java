package com.dslovikosky.narnia.client.renderer.entity;

import com.dslovikosky.narnia.client.constants.ModRenderPipelines;
import com.dslovikosky.narnia.client.renderer.CustomLateEntityRenderer;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellConeEntity;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpellConeRenderer extends EntityRenderer<SpellConeEntity, SpellConeRenderState> implements CustomLateEntityRenderer<SpellConeRenderState> {
    private static final Vec3 UP_VECTOR = new Vec3(0.0, 1.0, 0.0);
    private static final Vec3 LEFT_VECTOR = new Vec3(1.0, 0.0, 0.0);
    // The texture used by the model
    private static final ResourceLocation SPELL_CONE_TEXTURE = Constants.modLocation("textures/entity/spell/cone.png");
    private static final RenderType CONE_RENDER_TYPE = RenderType.create(
            Constants.modLocation("spell_cone").toString(),
            1536,
            true,
            true,
            ModRenderPipelines.SPELL_ENTITY,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(SPELL_CONE_TEXTURE, false))
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false));
    private static final RenderType CONE_BORDER_RENDER_TYPE = RenderType.create(
            Constants.modLocation("spell_cone_corner_border").toString(),
            1536,
            true,
            true,
            RenderPipelines.LINES,
            RenderType.CompositeState.builder()
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false));

    public SpellConeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Vec3 getRenderOffset(SpellConeRenderState renderState) {
        return Vec3.ZERO;
    }

    @Override
    protected AABB getBoundingBoxForCulling(final SpellConeEntity spellConeEntity) {
        return spellConeEntity.getBoundingBoxForCulling();
    }

    @Override
    public SpellConeRenderState createRenderState() {
        return new SpellConeRenderState();
    }

    @Override
    public void extractRenderState(SpellConeEntity entity, SpellConeRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.setRadius(entity.getRadius());
        reusedState.setColor(entity.getColor());
        reusedState.setLifespanTicks(entity.getLifespanTicks());
        reusedState.setLength(entity.getLength());
        reusedState.setDirection(entity.getConeDirection());
    }

    @Override
    public void submit(SpellConeRenderState renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, CameraRenderState cameraRenderState) {
        final Vec3 direction = renderState.getDirection();
        final double length = renderState.getLength();
        final double radius = renderState.getRadius();
        final Color color = renderState.getColor();
        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        final int lifespan = renderState.getLifespanTicks();
        if (lifespan <= 0) {
            return;
        }
        final float ticksRemaining = renderState.ageInTicks;
        final float percentRemaining = Mth.clamp(ticksRemaining / lifespan, 0f, 1f);
        final int coneAlpha = Math.round(Mth.lerp(percentRemaining, 120f, 0f));
        final int borderAlpha = Math.round(Mth.lerp(percentRemaining, 255f, 0f));

        // Must be a multiple of two, so that drawing quads works
        final int numTriangles = 2 * ((int) Math.round(10 + radius) / 2);
        final double angleIncrement = Math.toRadians(360.0 / numTriangles);
        final Vec3 baseCenter = direction.scale(length);
        Vec3 baseLeftRightDir = UP_VECTOR.cross(direction);
        // This means we're looking straight up or down
        if (baseLeftRightDir.length() < 0.00001) {
            baseLeftRightDir = LEFT_VECTOR;
        }
        final Vec3 baseUpDownDir = baseLeftRightDir.cross(direction);

        final Matrix4f rotationMatrix = poseStack.last().pose();
        final PoseStack.Pose pose = poseStack.last();

        Vec3 finalBaseLeftRightDir = baseLeftRightDir;
        final List<Vec3> baseVertices = IntStream.range(0, numTriangles).boxed().map(it -> {
            final double rotation = angleIncrement * it;
            return baseCenter.add(finalBaseLeftRightDir.scale(Math.cos(rotation)).add(baseUpDownDir.scale(Math.sin(rotation))).scale(radius));
        }).collect(Collectors.toList());
        // Duplicate the starting vertex, so the loop below will add a final quad ending on the starting vertex
        baseVertices.add(baseVertices.get(0));

        // Draw quads from the point of the cone to three vertices on the base. This will create a sort of diamond shape
        final VertexConsumer coneBuffer = multiBufferSource.getBuffer(CONE_RENDER_TYPE);
        for (int i = 2; i <= numTriangles; i = i + 2) {
            final Vec3 baseVertexOne = baseVertices.get(i);
            final Vec3 baseVertexTwo = baseVertices.get(i - 1);
            final Vec3 baseVertexThree = baseVertices.get(i - 2);

            // Draw the "point" vertex of the cone first
            drawConeVertex(rotationMatrix, pose, coneBuffer, 0.0, 0.0, 0.0, 0f, 0f, red, green, blue, coneAlpha);
            drawConeVertex(rotationMatrix, pose, coneBuffer, baseVertexOne.x(), baseVertexOne.y(), baseVertexOne.z(), 1f, 0f, red, green, blue, coneAlpha);
            drawConeVertex(rotationMatrix, pose, coneBuffer, baseVertexTwo.x(), baseVertexTwo.y(), baseVertexTwo.z(), 1f, 1f, red, green, blue, coneAlpha);
            drawConeVertex(rotationMatrix, pose, coneBuffer, baseVertexThree.x(), baseVertexThree.y(), baseVertexThree.z(), 0f, 1f, red, green, blue, coneAlpha);
        }

        final VertexConsumer coneBorderBuffer = multiBufferSource.getBuffer(CONE_BORDER_RENDER_TYPE);
        for (int i = 0; i < numTriangles; i++) {
            final Vec3 baseVertex = baseVertices.get(i);
            // Draw a point from the tip of the cone to each vertex
            drawBorderVertex(rotationMatrix, pose, coneBorderBuffer, 0.0, 0.0, 0.0, red, green, blue, borderAlpha);
            drawBorderVertex(rotationMatrix, pose, coneBorderBuffer, baseVertex.x(), baseVertex.y(), baseVertex.z(), red, green, blue, borderAlpha);
        }
    }

    private void drawConeVertex(
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
