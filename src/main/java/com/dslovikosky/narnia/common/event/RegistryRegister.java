package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class RegistryRegister {
    @SubscribeEvent
    public void onNewRegistryEvent(NewRegistryEvent event) {
        ModRegistries.REGISTRIES.forEach(event::register);
    }
}
