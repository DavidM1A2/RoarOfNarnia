package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.schematic.SchematicManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

public class SchematicHandler {
    @SubscribeEvent
    public void onAddReloadListenerEvent(final AddServerReloadListenersEvent event) {
        event.addListener(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "schematic_manager"), new SchematicManager(event.getRegistryAccess()));
    }
}
