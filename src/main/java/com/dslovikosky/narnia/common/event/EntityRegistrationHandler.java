package com.dslovikosky.narnia.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class EntityRegistrationHandler {
    @SubscribeEvent
    public void onEntityAttributeCreationEvent(final EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
    }
}
