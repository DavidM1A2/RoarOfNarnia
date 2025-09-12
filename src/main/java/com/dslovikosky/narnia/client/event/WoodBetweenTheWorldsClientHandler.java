package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.constants.ModRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class WoodBetweenTheWorldsClientHandler {
    @SubscribeEvent
    public void onRenderLevelStageEvent(final RenderLevelStageEvent.AfterWeather event) {
        ModRenderers.DROWSY_VIGNETTE.render(event.getPartialTick());
    }
}
