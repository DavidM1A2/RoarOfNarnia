package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.model.schematic.SchematicManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class SchematicHandler {
    @SubscribeEvent
    public void onAddReloadListenerEvent(final AddReloadListenerEvent event) {
        event.addListener(new SchematicManager(event.getRegistryAccess()));
    }
}
