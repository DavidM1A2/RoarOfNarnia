package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.schematic.SchematicManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

public class SchematicHandler {
    @SubscribeEvent
    public void onAddReloadListenerEvent(final AddServerReloadListenersEvent event) {
        event.addListener(Constants.modLocation("schematic_manager"), new SchematicManager(event.getRegistryAccess()));
    }
}
