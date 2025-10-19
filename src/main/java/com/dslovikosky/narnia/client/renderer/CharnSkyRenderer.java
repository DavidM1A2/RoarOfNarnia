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
    private AbstractTexture charnSunTexture;

    public CharnSkyRenderer() {
        super(SkyType.OVERWORLD, false, false);
        this.charnSunBuffer = buildCharnSunQuad();
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.charnSunTexture = getTexture(CHARN_SUN_TEXTURE);
    }

    @Override
    public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4f modelViewMatrix, Runnable setupFog) {
        if (this.charnSunTexture != null) {
            final Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
            matrix4fStack.pushMatrix();
            matrix4fStack.rotate(Axis.YP.rotationDegrees(-90.0F));
            matrix4fStack.rotate(Axis.XP.rotationDegrees(72.0F));
            matrix4fStack.translate(0.0F, 100.0F, 0.0F);
            matrix4fStack.scale(50.0F, 1.0F, 50.0F);

            final GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
                    .writeTransform(matrix4fStack, new Vector4f(1.0F, 1.0F, 1.0F, 1.0f), new Vector3f(), new Matrix4f(), 0.0F);
            final GpuTextureView colorTextureView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
            final GpuTextureView depthTextureView = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
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

        return true;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    private GpuBuffer buildCharnSunQuad() {
        GpuBuffer gpubuffer;
        try (ByteBufferBuilder bytebufferbuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
            BufferBuilder bufferbuilder = new BufferBuilder(bytebufferbuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = new Matrix4f();
            bufferbuilder.addVertex(matrix4f, -1.0F, 0.0F, -1.0F).setUv(0.0F, 0.0F);
            bufferbuilder.addVertex(matrix4f, 1.0F, 0.0F, -1.0F).setUv(1.0F, 0.0F);
            bufferbuilder.addVertex(matrix4f, 1.0F, 0.0F, 1.0F).setUv(1.0F, 1.0F);
            bufferbuilder.addVertex(matrix4f, -1.0F, 0.0F, 1.0F).setUv(0.0F, 1.0F);

            try (MeshData meshdata = bufferbuilder.buildOrThrow()) {
                gpubuffer = RenderSystem.getDevice().createBuffer(() -> "Charn Sun Quad", 40, meshdata.vertexBuffer());
            }
        }

        return gpubuffer;
    }

    private AbstractTexture getTexture(ResourceLocation location) {
        final TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        final AbstractTexture abstractTexture = texturemanager.getTexture(location);
        abstractTexture.setUseMipmaps(false);
        return abstractTexture;
    }
}
