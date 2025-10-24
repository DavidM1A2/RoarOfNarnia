package com.dslovikosky.narnia.client.entity;

import com.dslovikosky.narnia.client.renderer.CustomLateEntityRenderer;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellAOEEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
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
public class SpellAOERenderer extends EntityRenderer<SpellAOEEntity, SpellAOERenderState> implements CustomLateEntityRenderer<SpellAOERenderState> {
    private static final int TEXTURE_HEIGHT = 128;
    private static final int SPRITE_HEIGHT = 16;
    private static final int SPRITE_COUNT = TEXTURE_HEIGHT / SPRITE_HEIGHT;

    // The texture used by the model
    private static final ResourceLocation SPELL_AOE_TEXTURE = Constants.modLocation("textures/entity/spell/aoe.png");

    public SpellAOERenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Vec3 getRenderOffset(SpellAOERenderState renderState) {
        return Vec3.ZERO;
    }

    @Override
    protected AABB getBoundingBoxForCulling(final SpellAOEEntity spellAOEEntity) {
        return spellAOEEntity.getBoundingBoxForCulling();
    }

    @Override
    public SpellAOERenderState createRenderState() {
        return new SpellAOERenderState();
    }

    @Override
    public void extractRenderState(SpellAOEEntity entity, SpellAOERenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.setRadius(entity.getRadius());
        reusedState.setColor(entity.getColor());
        reusedState.setLifespanTicks(entity.getLifespanTicks());
    }

    @Override
    public void submit(SpellAOERenderState renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, CameraRenderState cameraRenderState) {
        final int lifespanTicks = renderState.getLifespanTicks();
        if (lifespanTicks <= 0) {
            return;
        }
        final float ageInTicks = renderState.ageInTicks;
        final int alpha = Math.toIntExact(Math.round(Mth.lerp(Mth.clamp(ageInTicks / lifespanTicks, 0f, 1f), 100f, 0f)));

        final float textureVStep = (float) SPRITE_HEIGHT / TEXTURE_HEIGHT;
        final float step = ageInTicks % SPRITE_COUNT;
        final float startV = step * textureVStep;
        final float endV = (step + 1) * textureVStep;

        final float radius = renderState.getRadius();
        final Color color = renderState.getColor();
        final int red = color.getRed();
        final int green = color.getGreen();
        final int blue = color.getBlue();

        final int latitudes = (int) Math.ceil(7 + radius / 3);
        final int longitudes = (int) Math.ceil(7 + radius / 3);

        final VertexConsumer buffer = multiBufferSource.getBuffer(RenderType.ENTITY_TRANSLUCENT.apply(SPELL_AOE_TEXTURE, false));

        poseStack.pushPose();

        final PoseStack.Pose pose = poseStack.last();
        final Matrix4f rotationMatrix = pose.pose();

        // Algorithm from https://stackoverflow.com/questions/43412525/algorithm-to-draw-a-sphere-using-quadrilaterals
        for (int latitude = 1; latitude <= latitudes; latitude++) {
            final double lat0 = Math.PI * (((double) (latitude - 1) / latitudes) - 0.5f);
            final double z0 = radius * Math.sin(lat0);
            final double zr0 = Math.cos(lat0);

            final double lat1 = Math.PI * (((double) latitude / latitudes) - 0.5f);
            final double z1 = radius * Math.sin(lat1);
            final double zr1 = Math.cos(lat1);

            for (int longitude = 1; longitude <= longitudes; longitude++) {
                final double long0 = 2 * Math.PI * ((double) (longitude - 1) / longitudes);
                final double x0 = radius * Math.cos(long0);
                final double y0 = radius * Math.sin(long0);

                final double long1 = 2 * Math.PI * ((double) longitude / longitudes);
                final double x1 = radius * Math.cos(long1);
                final double y1 = radius * Math.sin(long1);

                drawVertex(rotationMatrix, pose, buffer, x0 * zr0, z0, y0 * zr0, 0f, startV, red, green, blue, alpha);
                drawVertex(rotationMatrix, pose, buffer, x1 * zr0, z0, y1 * zr0, 1f, startV, red, green, blue, alpha);
                drawVertex(rotationMatrix, pose, buffer, x1 * zr1, z1, y1 * zr1, 1f, endV, red, green, blue, alpha);
                drawVertex(rotationMatrix, pose, buffer, x0 * zr1, z1, y0 * zr1, 0f, endV, red, green, blue, alpha);
            }
        }

        poseStack.popPose();
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
}
