package com.dslovikosky.narnia.client.entity;

import com.dslovikosky.narnia.client.constants.ModRenderPipelines;
import com.dslovikosky.narnia.client.renderer.CustomLateEntityRenderer;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellChainEntity;
import com.dslovikosky.narnia.common.utils.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpellChainRenderer extends EntityRenderer<SpellChainEntity, SpellChainRenderState> implements CustomLateEntityRenderer<SpellChainRenderState> {
    private static final double FORK_WIDTH = 0.03;
    private static final double JITTER_WIDTH = 0.05;
    private static final int SPRITE_COUNT = 2;
    private static final double MAX_JITTER_OFFSET_RATIO = 1.0 / 10.0;
    private static final double MAX_FORK_OFFSET_RATIO = 1.0 / 3.0;
    private static final double APPROXIMATE_BLOCKS_PER_JITTER = 2;

    private static final Vec3 BASE_RENDER_DIRECTION = new Vec3(1.0, 0.0, 0.0);

    // The texture used by the model
    private static final ResourceLocation SPELL_CHAIN_TEXTURE = Constants.modLocation("textures/entity/spell/chain.png");

    private static final RenderType RENDER_TYPE = RenderType.create(
            Constants.modLocation("spell_chain").toString(),
            1536,
            true,
            true,
            ModRenderPipelines.SPELL_ENTITY,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(SPELL_CHAIN_TEXTURE, false))
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false));

    public SpellChainRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Vec3 getRenderOffset(SpellChainRenderState renderState) {
        return Vec3.ZERO;
    }

    @Override
    protected AABB getBoundingBoxForCulling(final SpellChainEntity spellChainEntity) {
        return spellChainEntity.getBoundingBoxForCulling();
    }

    @Override
    public SpellChainRenderState createRenderState() {
        return new SpellChainRenderState();
    }

    @Override
    public void extractRenderState(SpellChainEntity entity, SpellChainRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.setStartPos(entity.position());
        reusedState.setEndPos(entity.getEndPos());
        // Seed the RNG object with the spell object's UUID, so it looks different each time. Change
        // the seed every 2 ticks, so the lightning changes shape rapidly
        reusedState.setRandom(new Random(entity.getUUID().getMostSignificantBits() + (long) reusedState.ageInTicks / 2));
    }

    @Override
    public void submit(SpellChainRenderState renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, CameraRenderState cameraRenderState) {
        final Vec3 startPos = renderState.getStartPos();
        final Vec3 endPos = renderState.getEndPos();
        final Random random = renderState.getRandom();
        if (startPos.equals(endPos)) {
            return;
        }

        // How to make good looking lightning: https://developer.download.nvidia.com/SDK/10/direct3d/Source/Lightning/doc/lightning_doc.pdf
        final VertexConsumer buffer = multiBufferSource.getBuffer(RENDER_TYPE);

        final double distance = startPos.distanceTo(endPos);
        final double numJitters = distance / APPROXIMATE_BLOCKS_PER_JITTER;
        final int numJitterSubdivisions = (int) Math.ceil(MathUtils.log(2.0, numJitters));
        final List<Vec3> jitterVertices = new ArrayList<>();
        jitterVertices.add(startPos);
        computeJitters(startPos, endPos, random, jitterVertices, numJitterSubdivisions);
        jitterVertices.add(endPos);
        final Map<Vec3, List<Vec3>> forks = computeForks(random, jitterVertices);

        poseStack.pushPose();
        for (int i = 1; i < jitterVertices.size(); i++) {
            final Vec3 fromJitterVertex = jitterVertices.get(i - 1);
            final Vec3 toJitterVertex = jitterVertices.get(i);

            // Draw the fork first if it exists for this vertex
            final List<Vec3> forkVertices = forks.get(fromJitterVertex);
            if (forkVertices != null) {
                poseStack.pushPose();
                for (int j = 1; j < forkVertices.size(); j++) {
                    final Vec3 fromForkVertex = forkVertices.get(j - 1);
                    final Vec3 toForkVertex = forkVertices.get(j);
                    drawSegment(poseStack, buffer, fromForkVertex, toForkVertex, FORK_WIDTH);
                }
                poseStack.popPose();
            }

            // Then draw the jitter
            drawSegment(poseStack, buffer, fromJitterVertex, toJitterVertex, JITTER_WIDTH);
        }
        poseStack.popPose();
    }

    /**
     * Algorithm computes random jitters to split the main segment path. It recursively computes jitters on subcomponents and returns
     * its output in the "jitter vertices" list.
     */
    private void computeJitters(final Vec3 startVertex, final Vec3 endVertex, final Random random, final List<Vec3> jitterVertices, final int iterationsLeft) {
        // Base case, nothing left to do
        if (iterationsLeft <= 0) {
            return;
        }

        // Compute the "up" and "down" vectors relative to the current jitter's direction
        final Vec3 forwardBackwardDirection = endVertex.subtract(startVertex).normalize();
        final Tuple<Vec3, Vec3> orthogonalVectors = MathUtils.getOrthogonalVectors(forwardBackwardDirection);
        final Vec3 leftRightDirection = orthogonalVectors.getA();
        final Vec3 upDownDirection = orthogonalVectors.getB();

        final double distance = startVertex.distanceTo(endVertex);
        // Compute how "far" the jitter is allowed to be away from the center of the chain
        final double maxJitterOffset = distance * MAX_JITTER_OFFSET_RATIO;
        // Take the midpoint of the start and end vertices
        final Vec3 jitterVertex = startVertex.add(endVertex)
                .scale(0.5)
                // Add the random jitter
                .add(leftRightDirection.scale((random.nextDouble() - 0.5) * maxJitterOffset * 2))
                .add(upDownDirection.scale((random.nextDouble() - 0.5) * maxJitterOffset * 2));

        // Compute the left segment jitters, add the center jitter vertex, then the right segment jitters.
        // This ensures the jitter vertices are in the correct order.
        computeJitters(startVertex, jitterVertex, random, jitterVertices, iterationsLeft - 1);
        jitterVertices.add(jitterVertex);
        computeJitters(jitterVertex, endVertex, random, jitterVertices, iterationsLeft - 1);
    }

    private Map<Vec3, List<Vec3>> computeForks(final Random random, final List<Vec3> jitterVertices) {
        if (jitterVertices.size() <= 2) {
            return Collections.emptyMap();
        }

        final Vec3 firstVertex = jitterVertices.getFirst();
        final Vec3 lastVertex = jitterVertices.getLast();
        final Vec3 forwardBackwardDirection = lastVertex.subtract(firstVertex).normalize();
        final Tuple<Vec3, Vec3> orthogonalVectors = MathUtils.getOrthogonalVectors(forwardBackwardDirection);
        final Vec3 leftRightDirection = orthogonalVectors.getA();
        final Vec3 upDownDirection = orthogonalVectors.getB();

        final Map<Vec3, List<Vec3>> forks = new HashMap<>();

        // Can't fork the first or last vertex, so use 1 to size-1
        for (int i = 1; i < jitterVertices.size() - 1; i++) {
            final Vec3 jitterVertex = jitterVertices.get(i);
            final double maxForkDistance = jitterVertex.distanceTo(lastVertex);

            // 40% chance to make a fork at the jitter vertex
            if (random.nextDouble() < 0.4) {
                // Fork length takes between 70% and 100% of the max fork distance possible
                final double forkDistance = (random.nextDouble() * 0.3 + 0.7) * maxForkDistance;
                final double numJittersInFork = forkDistance / APPROXIMATE_BLOCKS_PER_JITTER;
                final int numJitterSubdivisionsInFork = (int) Math.ceil(MathUtils.log(2.0, numJittersInFork));
                // Compute how "far" the jitter is allowed to be away from the center of the chain
                final double maxForkOffset = forkDistance * MAX_FORK_OFFSET_RATIO;
                final Vec3 forkEndVertex = jitterVertex.add(forwardBackwardDirection.scale(forkDistance))
                        // Add a random amount to the fork's end position
                        .add(leftRightDirection.scale((random.nextDouble() - 0.5) * maxForkOffset * 2))
                        .add(upDownDirection.scale((random.nextDouble() - 0.5) * maxForkOffset * 2));

                final List<Vec3> forkVertices = new ArrayList<>();
                computeJitters(jitterVertex, forkEndVertex, random, forkVertices, numJitterSubdivisionsInFork);
                forks.put(jitterVertex, forkVertices);
            }
        }

        return forks;
    }

    private void drawSegment(final PoseStack matrixStack, final VertexConsumer buffer, final Vec3 fromVertex, final Vec3 toVertex, final double width) {
        final double segmentLength = fromVertex.distanceTo(toVertex);

        final Vec3 direction = toVertex.subtract(fromVertex).normalize();
        final Quaternionf rotation = MathUtils.computeRotationTo(BASE_RENDER_DIRECTION, direction);

        matrixStack.pushPose();
        matrixStack.mulPose(rotation);

        final PoseStack.Pose pose = matrixStack.last();
        final Matrix4f rotationMatrix = pose.pose();
        final float rotationPerSprite = 180f / SPRITE_COUNT;
        for (int i = 0; i < SPRITE_COUNT; i++) {
            drawVertex(rotationMatrix, buffer, 0.0, -width, 0.0, 0f, 0f);
            drawVertex(rotationMatrix, buffer, segmentLength, -width, 0.0, (float) segmentLength, 0f);
            drawVertex(rotationMatrix, buffer, segmentLength, width, 0.0, (float) segmentLength, 1f);
            drawVertex(rotationMatrix, buffer, 0.0, width, 0.0, 0f, 1f);
            matrixStack.mulPose(Axis.XP.rotationDegrees(rotationPerSprite));
        }

        matrixStack.popPose();

        final Vec3 relativeSegmentEnd = direction.scale(segmentLength);
        matrixStack.translate(relativeSegmentEnd.x(), relativeSegmentEnd.y(), relativeSegmentEnd.z());
    }

    private void drawVertex(
            final Matrix4f rotationMatrix,
            final VertexConsumer buffer,
            final double x,
            final double y,
            final double z,
            final float u,
            final float v
    ) {
        buffer
                .addVertex(rotationMatrix, (float) x, (float) y, (float) z)
                .setColor(255, 255, 0, 255)
                .setUv(u, v);
    }
}
