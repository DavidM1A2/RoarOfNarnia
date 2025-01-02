package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.entity.digory.DigoryModel;
import com.dslovikosky.narnia.client.entity.digory.DigoryRenderer;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.entity.digory.DigoryEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class EntityRegistrationHandler {
    @SubscribeEvent
    public void onEntityAttributeCreationEvent(final EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.DIGORY.get(), DigoryEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DigoryModel.LAYER_LOCATION, DigoryModel::createBodyLayer);
    }

    @SubscribeEvent
    public void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.DIGORY.get(), DigoryRenderer::new);
    }
}
