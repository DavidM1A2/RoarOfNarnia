package com.dslovikosky.narnia.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.neoforged.neoforge.client.submit.RenderPhaseKeys;

/**
 * Submits spell geometry on both sides of translucent terrain, so that water neither hides it nor clips it.
 * <p>
 * Spell pipelines test depth without writing it, and water writes depth when it draws, so no single phase
 * renders a spell correctly around a water surface. Submitting only before translucent terrain lets water paint
 * over anything further away than the surface, including an effect on the far side of a pond. Submitting only
 * after translucent terrain discards every fragment further away than the water, clipping the effect flat along
 * the plane. Running both covers the whole effect - the far side comes from the earlier pass and the near side
 * from the later one.
 * <p>
 * This is what the pre-26.2 renderer did by drawing each spell entity once on the normal entity path and again
 * from the after-weather event. Geometry that no water sits in front of is drawn twice and so blends twice,
 * which reads as a slightly more solid effect; that was true of the old renderer too.
 */
public final class SpellGeometrySubmitter {
    private SpellGeometrySubmitter() {
    }

    public static void submitThroughWater(
            final SubmitNodeCollector submitNodeCollector,
            final PoseStack poseStack,
            final RenderType renderType,
            final SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer
    ) {
        // Before translucent terrain, which covers whatever ends up behind a water surface
        submitNodeCollector.submitSpecial(
                RenderPhaseKeys.TRANSLUCENT_CUSTOM_GEOMETRY,
                new CustomFeatureRenderer.Submit(poseStack.last().copy(), renderType, customGeometryRenderer)
        );
        // After translucent terrain, which covers whatever ends up in front of it
        submitNodeCollector.submitSpecial(
                RenderPhaseKeys.AFTER_TERRAIN,
                new CustomFeatureRenderer.Submit(poseStack.last().copy(), renderType, customGeometryRenderer)
        );
    }
}
