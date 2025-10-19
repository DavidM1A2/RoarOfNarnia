package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModKeyMappings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class KeyMappingRegister {
    @SubscribeEvent
    public void onRegisterKeyMappingsEvent(final RegisterKeyMappingsEvent event) {
        event.registerCategory(ModKeyMappings.NARNIA);
        ModKeyMappings.KEY_MAPPINGS.forEach(event::register);
    }
}
