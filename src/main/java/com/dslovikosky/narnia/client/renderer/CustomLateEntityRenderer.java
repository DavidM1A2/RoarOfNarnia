package com.dslovikosky.narnia.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;

public interface CustomLateEntityRenderer<R extends EntityRenderState> {
    void submit(R renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, CameraRenderState cameraRenderState);
}
