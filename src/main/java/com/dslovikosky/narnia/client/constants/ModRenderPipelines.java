package com.dslovikosky.narnia.client.constants;

import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.List;

public class ModRenderPipelines {
    // 26.2 replaced the pipeline's loose sampler/uniform declarations with bind group layouts.
    // 26.3 folded withSampler into withUniform, with the sampler spelled as a uniform type.
    private static final BindGroupLayout DROWSY_VIGNETTE_BINDINGS = BindGroupLayout.builder()
            .withUniform("In", UniformType.COMBINED_IMAGE_SAMPLER)
            .withUniform("TimeUniform", UniformType.UNIFORM_BUFFER)
            .build();

    public static final RenderPipeline DROWSY_VIGNETTE = RenderPipeline.builder()
            .withLocation(Constants.modLocation("pipeline/drowsy_vignette"))
            .withVertexShader(Constants.modLocation("post/drowsy_vignette"))
            .withFragmentShader(Constants.modLocation("post/drowsy_vignette"))
            .withBindGroupLayout(DROWSY_VIGNETTE_BINDINGS)
            .withVertexBinding(0, DefaultVertexFormat.POSITION)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            // 26.3 requires the pipeline's colour targets to match the render pass's attachments. The vignette
            // overwrites the main target outright rather than blending into it, so the unblended default fits.
            .withColorTargetState(ColorTargetState.DEFAULT)
            // No depth state at all, because 26.3 demands a depth attachment from any pipeline that declares
            // one and this pass has none. The old ALWAYS_PASS without depth writes was already a no-op, and
            // this matches vanilla's TRACY_BLIT, the other full screen blit into the main target.
            .build();

    // Mirrors how vanilla now assembles its own core/position_tex_color pipelines, which pull in the
    // globals uniform alongside the matrices. 26.2 inverted the depth comparison, so what used to be
    // LESS_THAN_OR_EQUAL is now GREATER_THAN_OR_EQUAL. 26.3 split MATRICES_PROJECTION into separate
    // projection and dynamic transform layouts; this binds them in the order vanilla's END_SKY does,
    // since that is the other core/position_tex_color pipeline built the same way.
    public static final RenderPipeline SPELL_ENTITY = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            .withLocation(Constants.modLocation("pipeline/spell_entity"))
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withShaderDefine("ALPHA_CUTOUT", 0.0F)
            .withShaderDefine("NO_OVERLAY")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withCull(false)
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .build();

    public static final RenderPipeline TRANSLUCENT_PARTICLE_NO_CULL = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
            .withLocation(Constants.modLocation("pipeline/translucent_particle_no_cull"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .build();

    public static final RenderPipeline TRANSLUCENT_PARTICLE_NO_DEPTH = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
            .withLocation(Constants.modLocation("pipeline/translucent_particle_no_depth"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .build();

    public static final List<RenderPipeline> LIST = List.of(DROWSY_VIGNETTE, SPELL_ENTITY, TRANSLUCENT_PARTICLE_NO_CULL, TRANSLUCENT_PARTICLE_NO_DEPTH);
}
