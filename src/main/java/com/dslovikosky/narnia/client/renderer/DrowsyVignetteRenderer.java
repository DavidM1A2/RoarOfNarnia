package com.dslovikosky.narnia.client.renderer;

import com.dslovikosky.narnia.client.constants.ModRenderPipelines;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModMobEffects;
import com.dslovikosky.narnia.common.event.WoodBetweenTheWorldsHandler;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;

public class DrowsyVignetteRenderer {
    private static final ContextKey<Float> DROWSY_VIGNETTE_STRENGTH = new ContextKey<>(Constants.modLocation("drowsy_vignette_strength"));
    private static final float[] FULLSCREEN_TRIANGLE = {
            // Bottom left 90-deg angle corner
            -1f, -1f, 0f,
            // Bottom right 45-deg corner. Use 3 to overshoot so that the triangle covers the entire screen
            3f, -1f, 0f,
            // Top left 45-deg corner. Use 3 to overshoot so that the triangle covers the entire screen
            -1f, 3f, 0f
    };
    private static final int FULLSCREEN_VERTEX_BYTES = DefaultVertexFormat.POSITION.getVertexSize() * FULLSCREEN_TRIANGLE.length / 3;
    private static final int UBO_SIZE = new Std140SizeCalculator()
            .putVec4() // timeStrengthVec (vec4)
            .get();

    private MappableRingBuffer uniformBufferObject;
    private GpuBuffer vertexBufferObject;

    public void initialize() {
        final int uboUsage = GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE;
        uniformBufferObject = new MappableRingBuffer(() -> "Drowsy Vignette UBO", uboUsage, UBO_SIZE);

        final int vboUsage = GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE;
        final GpuDevice device = RenderSystem.getDevice();
        vertexBufferObject = device.createBuffer(() -> "Drowsy Vignette VBO", vboUsage, FULLSCREEN_VERTEX_BYTES);
        try (GpuBufferSlice.MappedView view = vertexBufferObject.map(false, true)) {
            view.data().asFloatBuffer().put(FULLSCREEN_TRIANGLE);
        }
    }

    public void extract(final DeltaTracker deltaTracker, final LevelRenderState renderState) {
        if (uniformBufferObject == null) {
            renderState.setRenderData(DROWSY_VIGNETTE_STRENGTH, null);
            return;
        }

        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayer player = minecraft.player;
        if (player == null) {
            renderState.setRenderData(DROWSY_VIGNETTE_STRENGTH, null);
            return;
        }

        final MobEffectInstance drowsyEffect = player.getEffect(ModMobEffects.DROWSY);
        if (drowsyEffect == null) {
            renderState.setRenderData(DROWSY_VIGNETTE_STRENGTH, null);
            return;
        }

        final float ticks = player.getData(ModAttachmentTypes.TICKS_IN_WOOD_BETWEEN_THE_WORLDS) + deltaTracker.getGameTimeDeltaTicks();
        renderState.setRenderData(DROWSY_VIGNETTE_STRENGTH, Math.clamp(ticks / (float) (WoodBetweenTheWorldsHandler.TICKS_PER_DROWSY_LEVEL * WoodBetweenTheWorldsHandler.MAX_DROWSY_LEVEL), 0, 1));
    }

    public void render(final RenderTarget renderTarget, final LevelRenderState levelRenderState) {
        final Float strength = levelRenderState.getRenderData(DROWSY_VIGNETTE_STRENGTH);
        if (strength == null) {
            return;
        }

        // Move ring to next slot
        uniformBufferObject.rotate();

        final CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

        // Write vec4(timeStrength, 0,0,0) in std140 layout. 26.3 refuses to use a buffer that is still mapped,
        // so the mapping has to be closed before the render pass below reads the uniform out of it.
        try (GpuBufferSlice.MappedView view = uniformBufferObject.currentBuffer().map(false, true)) {
            Std140Builder.intoBuffer(view.data()).putVec4(strength, 0f, 0f, 0f);
        }

        // Get the main color texture view (the current frame)
        final GpuTextureView colorView = renderTarget.getColorTextureView();

        // Create a render pass and bind the uniform and the color sampler
        try (RenderPass pass = encoder.createRenderPass(() -> "drowsy_vignette_pass", colorView, Optional.empty())) {
            // Setup the pipeline, uniform, sampler, and vertex buffer. 26.3 takes a compiled pipeline
            // rather than the pipeline itself, and folded bindTexture into setUniform.
            pass.setPipeline(RenderSystem.getCompiledPipeline(ModRenderPipelines.DROWSY_VIGNETTE));
            pass.setUniform("TimeUniform", uniformBufferObject.currentBuffer().slice(0, UBO_SIZE));
            pass.setUniform("In", colorView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            pass.setVertexBuffer(0, vertexBufferObject.slice());

            // Draw 3 vertices (one large triangle that covers the entire screen)
            pass.draw(3, 1, 0, 0);
        }
    }
}
