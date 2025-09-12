package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.constants.ModRenderPipelines;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

public class RenderPipelineRegister {
    @SubscribeEvent
    public void onRegisterRenderPipelinesEvent(final RegisterRenderPipelinesEvent event) {
        event.registerPipeline(ModRenderPipelines.DROWSY_VIGNETTE);
    }
}
