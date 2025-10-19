package com.dslovikosky.narnia.client.constants;

import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

public class ModRenderPipelines {
    public static final RenderPipeline DROWSY_VIGNETTE = RenderPipeline.builder()
            .withLocation(Constants.modLocation("pipeline/drowsy_vignette"))
            .withVertexShader(Constants.modLocation("post/drowsy_vignette"))
            .withFragmentShader(Constants.modLocation("post/drowsy_vignette"))
            .withSampler("In")
            .withDepthWrite(false)
            .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLES)
            .withUniform("TimeUniform", UniformType.UNIFORM_BUFFER)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();
}
