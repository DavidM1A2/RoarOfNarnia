package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.renderer.CustomLateEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class ClientLateRenderHandler {
    @SubscribeEvent
    public void onRenderLevelStageEvent(final RenderLevelStageEvent.AfterWeather event) {
        final Minecraft minecraft = Minecraft.getInstance();
        final LevelRenderState levelRenderState = event.getLevelRenderState();
        final PoseStack poseStack = event.getPoseStack();
        final MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        final EntityRenderDispatcher renderDispatcher = minecraft.getEntityRenderDispatcher();
        final Vec3 cameraPos = event.getLevelRenderState().cameraRenderState.pos;

        poseStack.pushPose();
        poseStack.translate(cameraPos.scale(-1));
        for (final EntityRenderState entityRenderState : event.getLevelRenderState().entityRenderStates) {
            final EntityRenderer<?, ?> renderer = renderDispatcher.getRenderer(entityRenderState);
            if (renderer instanceof CustomLateEntityRenderer<?> customLateEntityRenderer) {
                final CustomLateEntityRenderer<EntityRenderState> typedRenderer = (CustomLateEntityRenderer<EntityRenderState>) customLateEntityRenderer;
                poseStack.pushPose();
                poseStack.translate(entityRenderState.x, entityRenderState.y, entityRenderState.z);
                typedRenderer.submit(entityRenderState, poseStack, bufferSource, levelRenderState.cameraRenderState);
                poseStack.popPose();
            }
        }
        poseStack.popPose();

        bufferSource.endBatch();
    }
}
