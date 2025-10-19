package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.constants.ModRenderers;
import com.dslovikosky.narnia.common.constants.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

public class DimensionSpecialEffectsRegister {
    @SubscribeEvent
    public void onRegisterDimensionSpecialEffectsEvent(final RegisterDimensionSpecialEffectsEvent event) {
        event.register(ModRenderers.CHARN_SKY_RENDERER_ID, ModRenderers.Client.CHARN_SKY_RENDERER);
    }

    @SubscribeEvent
    public void onAddReloadListenerEvent(final AddClientReloadListenersEvent event) {
        event.addListener(Constants.modLocation("charn_sky_renderer"), ModRenderers.Client.CHARN_SKY_RENDERER);
    }
}
