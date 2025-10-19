package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.constants.ModRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.concurrent.CompletableFuture;

public class ClientReloadHandler {
    @SubscribeEvent
    public void onAddClientReloadListenersEvent(final AddClientReloadListenersEvent event) {
        event.addListener(ModRenderers.DROWSY_VIGNETTE_ID, (sharedState, backgroundExecutor, barrier, gameExecutor) ->
                CompletableFuture.runAsync(ModRenderers.DROWSY_VIGNETTE::initialize, gameExecutor).thenCompose(barrier::wait));
    }
}
