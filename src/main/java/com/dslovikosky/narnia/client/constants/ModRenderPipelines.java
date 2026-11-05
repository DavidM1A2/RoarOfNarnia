package com.dslovikosky.narnia.client.constants;

import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.List;

public class ModRenderPipelines {
    // 26.2 replaced the pipeline's loose sampler/uniform declarations with bind group layouts
    private static final BindGroupLayout DROWSY_VIGNETTE_BINDINGS = BindGroupLayout.builder()
            .withSampler("In")
            .withUniform("TimeUniform", UniformType.UNIFORM_BUFFER)
            .build();

    public static final RenderPipeline DROWSY_VIGNETTE = RenderPipeline.builder()
            .withLocation(Constants.modLocation("pipeline/drowsy_vignette"))
            .withVertexShader(Constants.modLocation("post/drowsy_vignette"))
            .withFragmentShader(Constants.modLocation("post/drowsy_vignette"))
            .withBindGroupLayout(DROWSY_VIGNETTE_BINDINGS)
            .withVertexBinding(0, DefaultVertexFormat.POSITION)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
            .build();

    // Mirrors how vanilla now assembles its own core/position_tex_color pipelines, which pull in the
    // globals uniform alongside the matrices. 26.2 inverted the depth comparison, so what used to be
    // LESS_THAN_OR_EQUAL is now GREATER_THAN_OR_EQUAL.
    public static final RenderPipeline SPELL_ENTITY = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
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
