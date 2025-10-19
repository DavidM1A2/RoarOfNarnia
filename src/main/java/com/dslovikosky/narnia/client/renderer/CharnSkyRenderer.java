package com.dslovikosky.narnia.client.renderer;

import com.dslovikosky.narnia.common.constants.Constants;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CharnSkyRenderer extends DimensionSpecialEffects implements ResourceManagerReloadListener {
    private static final ResourceLocation CHARN_SUN_TEXTURE = Constants.modLocation("textures/environment/charn_sun.png");

    private final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
    private final GpuBuffer charnSunBuffer;
    private final GpuBuffer topSkyboxBuffer;
    private final GpuBuffer bottomSkyboxBuffer;
    private final GpuBuffer sunriseCircleBuffer;

    private AbstractTexture charnSunTexture;

    public CharnSkyRenderer() {
        super(SkyType.OVERWORLD, false, false);
        this.charnSunBuffer = buildCharnSunQuad();
        this.sunriseCircleBuffer = buildSunriseFan();

        // Use 10 vertices to make a giant hemisphere. buildSkyDisc() uses one center vertex and 9 around the center.
        try (final ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(10 * DefaultVertexFormat.POSITION.getVertexSize())) {
            final BufferBuilder topSkyboxBufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
            this.buildSkyDisc(topSkyboxBufferBuilder, 16.0F);
            try (MeshData topSkyboxMeshData = topSkyboxBufferBuilder.buildOrThrow()) {
                this.topSkyboxBuffer = RenderSystem.getDevice().createBuffer(() -> "Top skybox vertex buffer", GpuBuffer.USAGE_VERTEX, topSkyboxMeshData.vertexBuffer());
            }

            final BufferBuilder bottomSkyboxBufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
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

    private AbstractTexture getTexture(ResourceLocation location) {
        final TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        final AbstractTexture abstractTexture = texturemanager.getTexture(location);
        abstractTexture.setUseMipmaps(false);
        return abstractTexture;
    }

    @Override
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4f modelViewMatrix, Runnable setupFog) {
        setupFog.run();
        if (this.charnSunTexture != null) {
            final Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
            final GpuTextureView colorTextureView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
            final GpuTextureView depthTextureView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();

            renderSkyDiscUpper(skyRenderState, matrix4fStack, colorTextureView, depthTextureView);
            renderSun(matrix4fStack, colorTextureView, depthTextureView);
            renderSunset(skyRenderState, matrix4fStack, colorTextureView, depthTextureView);
            renderSkyDiscLower(matrix4fStack, colorTextureView, depthTextureView);
        }

        return true;
    }

    private void renderSkyDiscUpper(final SkyRenderState skyRenderState, final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        float red = ARGB.redFloat(skyRenderState.skyColor);
        float green = ARGB.greenFloat(skyRenderState.skyColor);
        float blue = ARGB.blueFloat(skyRenderState.skyColor);
        final GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(red, green, blue, 1.0F), new Vector3f(), new Matrix4f(), 0.0F);

        try (final RenderPass renderpass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Charn Sky disc", colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
            renderpass.setPipeline(RenderPipelines.SKY);
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.setVertexBuffer(0, this.topSkyboxBuffer);
            renderpass.draw(0, 10);
        }
    }

    private void renderSun(final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        matrix4fStack.pushMatrix();
        matrix4fStack.rotate(Axis.YP.rotationDegrees(-90.0F));
        matrix4fStack.rotate(Axis.XP.rotationDegrees(72.0F));
        matrix4fStack.translate(0.0F, 100.0F, 0.0F);
        matrix4fStack.scale(50.0F, 1.0F, 50.0F);

        final GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector3f(), new Matrix4f(), 0.0F);
        final GpuBuffer gpubuffer = this.quadIndices.getBuffer(6);

        try (final RenderPass renderpass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Charn Sky Sun", colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
            renderpass.setPipeline(RenderPipelines.CELESTIAL);
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.bindSampler("Sampler0", this.charnSunTexture.getTextureView());
            renderpass.setVertexBuffer(0, this.charnSunBuffer);
            renderpass.setIndexBuffer(gpubuffer, this.quadIndices.type());
            renderpass.drawIndexed(0, 0, 6, 1);
        }

        matrix4fStack.popMatrix();
    }

    private void renderSunset(final SkyRenderState skyRenderState, final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        matrix4fStack.pushMatrix();
        final float alpha = ARGB.alphaFloat(skyRenderState.sunriseAndSunsetColor);
        if (alpha > 0.001F) {
            final float red = ARGB.redFloat(skyRenderState.sunriseAndSunsetColor);
            final float green = ARGB.greenFloat(skyRenderState.sunriseAndSunsetColor);
            final float blue = ARGB.blueFloat(skyRenderState.sunriseAndSunsetColor);
            matrix4fStack.rotate(Axis.XP.rotationDegrees(90.0F));
            matrix4fStack.rotate(Axis.ZP.rotationDegrees(90.0F));
            matrix4fStack.scale(1.0F, 1.0F, alpha);
            GpuBufferSlice gpuBufferSlice = RenderSystem.getDynamicUniforms()
                    .writeTransform(matrix4fStack, new Vector4f(red, green, blue, alpha), new Vector3f(), new Matrix4f(), 0.0F);

            try (RenderPass renderpass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Sunrise sunset", colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
                renderpass.setPipeline(RenderPipelines.SUNRISE_SUNSET);
                RenderSystem.bindDefaultUniforms(renderpass);
                renderpass.setUniform("DynamicTransforms", gpuBufferSlice);
                renderpass.setVertexBuffer(0, this.sunriseCircleBuffer);
                renderpass.draw(0, 18);
            }
        }
        matrix4fStack.popMatrix();
    }

    private void renderSkyDiscLower(final Matrix4fStack matrix4fStack, final GpuTextureView colorTextureView, final GpuTextureView depthTextureView) {
        matrix4fStack.pushMatrix();
        matrix4fStack.translate(0.0F, 12F, 0.0F);
        GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(0.0F, 0.0F, 0.0F, 1.0F), new Vector3f(), new Matrix4f(), 0.0F);

        try (RenderPass renderpass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Sky dark", colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
            renderpass.setPipeline(RenderPipelines.SKY);
            RenderSystem.bindDefaultUniforms(renderpass);
            renderpass.setUniform("DynamicTransforms", gpubufferslice);
            renderpass.setVertexBuffer(0, this.bottomSkyboxBuffer);
            renderpass.draw(0, 10);
        }
        matrix4fStack.popMatrix();
    }

    @Override
    public boolean isSunriseOrSunset(float timeOfDay) {
        return true;
    }

    @Override
    public int getSunriseOrSunsetColor(float timeOfDay) {
        return ARGB.color(50, 230, 16, 16);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    private GpuBuffer buildCharnSunQuad() {
        // 4 vertices to make the sun
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
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
            final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
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
