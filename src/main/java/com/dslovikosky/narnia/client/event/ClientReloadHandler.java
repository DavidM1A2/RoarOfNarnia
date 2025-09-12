package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.constants.ModRenderers;
import com.dslovikosky.narnia.common.constants.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.concurrent.CompletableFuture;

public class ClientReloadHandler {
    @SubscribeEvent
    public void onAddClientReloadListenersEvent(final AddClientReloadListenersEvent event) {
        event.addListener(Constants.modLocation("drowsy_vignette"), (barrier, manager, backgroundExecutor, gameExecutor) ->
                CompletableFuture.runAsync(ModRenderers.DROWSY_VIGNETTE::initialize, gameExecutor).thenCompose(barrier::wait));
    }
}
