package com.dslovikosky.narnia.client.constants;

import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;

import java.util.List;

public class ModRenderPipelines {
    public static final RenderPipeline DROWSY_VIGNETTE = RenderPipeline.builder()
            .withLocation(Constants.modLocation("pipeline/drowsy_vignette"))
            .withVertexShader(Constants.modLocation("post/drowsy_vignette"))
            .withFragmentShader(Constants.modLocation("post/drowsy_vignette"))
            .withSampler("In")
            .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLES)
            .withUniform("TimeUniform", UniformType.UNIFORM_BUFFER)
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
            .build();

    public static final RenderPipeline SPELL_ENTITY = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withLocation(Constants.modLocation("pipeline/spell_entity"))
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withShaderDefine("ALPHA_CUTOUT", 0.0F)
            .withShaderDefine("NO_OVERLAY")
            .withSampler("Sampler0")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .withCull(false)
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
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
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .build();

    public static final List<RenderPipeline> LIST = List.of(DROWSY_VIGNETTE, SPELL_ENTITY, TRANSLUCENT_PARTICLE_NO_CULL, TRANSLUCENT_PARTICLE_NO_DEPTH);
}
