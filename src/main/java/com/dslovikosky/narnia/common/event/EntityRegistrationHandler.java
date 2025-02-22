package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.entity.digory.DigoryRenderer;
import com.dslovikosky.narnia.client.entity.human_child.HumanChildModel;
import com.dslovikosky.narnia.client.entity.lion.LionModel;
import com.dslovikosky.narnia.client.entity.lion.LionRenderer;
import com.dslovikosky.narnia.client.entity.polly.PollyRenderer;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.entity.human_child.HumanChildEntity;
import com.dslovikosky.narnia.common.entity.lion.LionEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class EntityRegistrationHandler {
    @SubscribeEvent
    public void onEntityAttributeCreationEvent(final EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.DIGORY.get(), HumanChildEntity.createAttributes().build());
        event.put(ModEntityTypes.POLLY.get(), HumanChildEntity.createAttributes().build());
        event.put(ModEntityTypes.LION.get(), LionEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HumanChildModel.LAYER_LOCATION, HumanChildModel::createBodyLayer);
        event.registerLayerDefinition(LionModel.LAYER_LOCATION, LionModel::createBodyLayer);
    }

    @SubscribeEvent
    public void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.DIGORY.get(), DigoryRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.POLLY.get(), PollyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.LION.get(), LionRenderer::new);
    }
}
