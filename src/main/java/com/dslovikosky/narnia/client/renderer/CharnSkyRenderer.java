package com.dslovikosky.narnia.client.renderer;

import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.OptionalDouble;

@ParametersAreNonnullByDefault
public class CharnSkyRenderer implements CustomSkyboxRenderer, ResourceManagerReloadListener {
    private static final Identifier CHARN_SUN_TEXTURE = Constants.modLocation("textures/environment/charn_sun.png");

    private final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
    private final GpuBuffer charnSunBuffer;
    private final GpuBuffer topSkyboxBuffer;
    private final GpuBuffer bottomSkyboxBuffer;
    private final GpuBuffer sunriseCircleBuffer;

    private AbstractTexture charnSunTexture;

    public CharnSkyRenderer() {
        this.charnSunBuffer = buildCharnSunQuad();
        this.sunriseCircleBuffer = buildSunriseFan();

        // Use 10 vertices to make a giant hemisphere. buildSkyDisc() uses one center vertex and 9 around the center.
        try (final ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(10 * DefaultVertexFormat.POSITION.getVertexSize())) {
            final BufferBuilder topSkyboxBufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
            this.buildSkyDisc(topSkyboxBufferBuilder, 16.0F);
            try (MeshData topSkyboxMeshData = topSkyboxBufferBuilder.buildOrThrow()) {
                this.topSkyboxBuffer = RenderSystem.getDevice().createBuffer(() -> "Top skybox vertex buffer", GpuBuffer.USAGE_VERTEX, topSkyboxMeshData.vertexBuffer());
            }

            final BufferBuilder bottomSkyboxBufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
            this.buildSkyDisc(bottomSkyboxBufferBuilder, -16.0F);
            try (MeshData bottomSkyMeshData = bottomSkyboxBufferBuilder.buildOrThrow()) {
                this.bottomSkyboxBuffer = RenderSystem.getDevice().createBuffer(() -> "Bottom skybox vertex buffer", GpuBuffer.USAGE_VERTEX, bottomSkyMeshData.vertexBuffer());
            }
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.charnSunTexture = getTexture(CHARN_SUN_TEXTURE);
    }

    private AbstractTexture getTexture(Identifier location) {
        final TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        // As of 1.21.11 mipmapping is a property of the texture's sampler, which already defaults to no mipmaps
        return texturemanager.getTexture(location);
    }

    // 26.3 hands the sky its fog as a prepared uniform slice rather than a callback that sets fog up
    @Override
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, GpuBufferSlice skyFog) {
        RenderSystem.setShaderFog(skyFog);
        if (this.charnSunTexture != null) {
            final Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
            final GpuTextureView colorTextureView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
            final GpuTextureView depthTextureView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();

            renderSkyDiscUpper(skyRenderState, matrix4fStack, colorTextureView, depthTextureView);
            renderSun(matrix4fStack, colorTextureView, depthTextureView);
            renderSunset(skyRenderState, matrix4fStack, colorTextureView, depthTextureView);
            renderSkyDiscLower(matrix4fStack, colorTextureView, depthTextureView);
        }

        return true;
    }

    private void renderSkyDiscUpper(final SkyRenderState skyRenderState, final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        // 26.3 carries the sky colour as a float vector rather than a packed int
        final GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(skyRenderState.skyColor, 1.0F), new Vector3f(), new Matrix4f());

        try (final RenderPass renderpass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Charn Sky disc", colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty())) {
            renderpass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.SKY));
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.setVertexBuffer(0, this.topSkyboxBuffer.slice());
            renderpass.draw(10, 1, 0, 0);
        }
    }

    private void renderSun(final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        matrix4fStack.pushMatrix();
        matrix4fStack.rotate(Axis.YP.rotationDegrees(-90.0F));
        matrix4fStack.rotate(Axis.XP.rotationDegrees(72.0F));
        matrix4fStack.translate(0.0F, 100.0F, 0.0F);
        matrix4fStack.scale(50.0F, 1.0F, 50.0F);

        final GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f(), new Matrix4f());
        final GpuBuffer gpubuffer = this.quadIndices.getBuffer(6);

        try (final RenderPass renderpass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Charn Sky Sun", colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty())) {
            renderpass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.CELESTIAL));
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.setUniform("Sampler0", this.charnSunTexture.getTextureView(), this.charnSunTexture.getSampler());
            renderpass.setVertexBuffer(0, this.charnSunBuffer.slice());
            renderpass.setIndexBuffer(gpubuffer, this.quadIndices.type());
            renderpass.drawIndexed(6, 1, 0, 0, 0);
        }

        matrix4fStack.popMatrix();
    }

    private void renderSunset(final SkyRenderState skyRenderState, final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        matrix4fStack.pushMatrix();
        // 26.3 carries the sunrise colour as a float vector rather than a packed int, alpha in w
        final float alpha = skyRenderState.sunriseAndSunsetColor.w();
        if (alpha > 0.001F) {
            matrix4fStack.rotate(Axis.XP.rotationDegrees(90.0F));
            matrix4fStack.rotate(Axis.ZP.rotationDegrees(90.0F));
            matrix4fStack.scale(1.0F, 1.0F, alpha);
            GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms()
                    .writeTransform(matrix4fStack, new Vector4f(skyRenderState.sunriseAndSunsetColor), new Vector3f(), new Matrix4f());

            try (RenderPass renderpass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Sunrise sunset", colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty())) {
                renderpass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.SUNRISE_SUNSET));
                RenderSystem.bindDefaultUniforms(renderpass);
                renderpass.setUniform("DynamicTransforms", gpuBufferSlice);
                renderpass.setVertexBuffer(0, this.sunriseCircleBuffer.slice());
                renderpass.draw(18, 1, 0, 0);
            }
        }
        matrix4fStack.popMatrix();
    }

    private void renderSkyDiscLower(final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        matrix4fStack.pushMatrix();
        matrix4fStack.translate(0.0F, 12F, 0.0F);
        GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(0.0F, 0.0F, 0.0F, 1.0F), new Vector3f(), new Matrix4f());

        try (RenderPass renderpass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky dark", colorTextureView, Optional.empty(), depthTextureView, OptionalDouble.empty())) {
            renderpass.setPipeline(RenderSystem.getCompiledPipeline(RenderPipelines.SKY));
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.setVertexBuffer(0, this.bottomSkyboxBuffer.slice());
            renderpass.draw(10, 1, 0, 0);
        }
        matrix4fStack.popMatrix();
    }

    // 1.21.11 replaced DimensionSpecialEffects with the environment attribute system. The always-on red
    // sunrise/sunset is now a static SUNRISE_SUNSET_COLOR attribute on the dimension type (see
    // ModDatapackObjectProvider); the old fog overrides were verbatim copies of vanilla's overworld
    // behaviour, which the attribute system now applies on its own.

    private GpuBuffer buildCharnSunQuad() {
        // 4 vertices to make the sun
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = new Matrix4f();
            bufferBuilder.addVertex(matrix4f, -1.0F, 0.0F, -1.0F).setUv(0.0F, 0.0F);
            bufferBuilder.addVertex(matrix4f, 1.0F, 0.0F, -1.0F).setUv(1.0F, 0.0F);
            bufferBuilder.addVertex(matrix4f, 1.0F, 0.0F, 1.0F).setUv(1.0F, 1.0F);
            bufferBuilder.addVertex(matrix4f, -1.0F, 0.0F, 1.0F).setUv(0.0F, 1.0F);

            try (final MeshData meshData = bufferBuilder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Charn Sun Quad", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, meshData.vertexBuffer());
            }
        }
    }

    private GpuBuffer buildSunriseFan() {
        final float fanWidth = 120f;
        final float fanHeight = 140f;

        // 18 vertices to make the sunrise fan. One at the center, and then 17 around it in the for loop below
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(18 * DefaultVertexFormat.POSITION_COLOR.getVertexSize())) {
            final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            int white = ARGB.white(1.0F);
            int black = ARGB.white(0.0F);

            bufferBuilder.addVertex(0.0F, 100.0F, 0.0F).setColor(white);
            for (int i = 0; i <= 16; i++) {
                float distance = i * (float) (Math.PI * 2) / 16.0F;
                float sin = Mth.sin(distance);
                float cos = Mth.cos(distance);
                bufferBuilder.addVertex(sin * fanWidth, cos * fanWidth, -cos * fanHeight).setColor(black);
            }

            try (MeshData meshdata = bufferBuilder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Sunrise fan", GpuBuffer.USAGE_VERTEX, meshdata.vertexBuffer());
            }
        }
    }

    private void buildSkyDisc(VertexConsumer buffer, float y) {
        final float skyDiscSize = 512;

        float f = Math.signum(y) * skyDiscSize;
        // Exactly 10 vertices. One center, and 9 around it.
        buffer.addVertex(0.0F, y, 0.0F);

        for (int i = -180; i <= 180; i += 45) {
            buffer.addVertex(f * Mth.cos(i * (float) (Math.PI / 180.0)), y, skyDiscSize * Mth.sin(i * (float) (Math.PI / 180.0)));
        }
    }
}
