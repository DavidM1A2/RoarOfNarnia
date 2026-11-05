package com.dslovikosky.narnia.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.neoforged.neoforge.client.submit.RenderPhaseKeys;

/**
 * Submits spell geometry into the after-terrain render phase.
 * <p>
 * SubmitNodeCollector#submitCustomGeometry picks a phase from the render type's blend mode alone, which lands
 * translucent geometry in the translucent phase - and that runs before translucent terrain, so water drawn
 * afterwards covers any spell effect inside it. The after-terrain phase runs once translucent terrain is done,
 * which is the same thing the old late renderer was reaching for, except the geometry still depth-tests against
 * the world and so stays correctly hidden behind solid blocks.
 */
public final class SpellGeometrySubmitter {
    private SpellGeometrySubmitter() {
    }

    public static void submitAfterTerrain(
            final SubmitNodeCollector submitNodeCollector,
            final PoseStack poseStack,
            final RenderType renderType,
            final SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer
    ) {
        submitNodeCollector.submitSpecial(
                RenderPhaseKeys.AFTER_TERRAIN,
                new CustomFeatureRenderer.Submit(poseStack.last().copy(), renderType, customGeometryRenderer)
        );
    }
}
