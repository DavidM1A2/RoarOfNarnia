package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.entity.SpellAOERenderer;
import com.dslovikosky.narnia.client.entity.SpellChainRenderer;
import com.dslovikosky.narnia.client.entity.WorldWoodBoatRenderer;
import com.dslovikosky.narnia.client.entity.WorldWoodChestBoatRenderer;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import net.minecraft.client.model.BoatModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class EntityRegistrationHandler {
    @SubscribeEvent
    public void onEntityAttributeCreationEvent(final EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(WorldWoodBoatRenderer.LAYER_LOCATION, BoatModel::createBoatModel);
        event.registerLayerDefinition(WorldWoodChestBoatRenderer.LAYER_LOCATION, BoatModel::createChestBoatModel);
    }

    @SubscribeEvent
    public void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.WORLD_WOOD_BOAT.get(), WorldWoodBoatRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WORLD_WOOD_CHEST_BOAT.get(), WorldWoodChestBoatRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SPELL_AOE.get(), SpellAOERenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SPELL_CHAIN.get(), SpellChainRenderer::new);
    }
}
