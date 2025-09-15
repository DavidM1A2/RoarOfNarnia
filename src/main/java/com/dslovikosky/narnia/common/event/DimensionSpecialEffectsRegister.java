package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.constants.ModRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

public class DimensionSpecialEffectsRegister {
    @SubscribeEvent
    public void onRegisterDimensionSpecialEffectsEvent(final RegisterDimensionSpecialEffectsEvent event) {
        event.register(ModRenderers.CHARN_SKY_RENDERER_ID, ModRenderers.CHARN_SKY_RENDERER);
    }
}
