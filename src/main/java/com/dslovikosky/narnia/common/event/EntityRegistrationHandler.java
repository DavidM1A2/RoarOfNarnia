package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.renderer.entity.JadisModel;
import com.dslovikosky.narnia.client.renderer.entity.JadisRenderer;
import com.dslovikosky.narnia.client.renderer.entity.SpellAOERenderer;
import com.dslovikosky.narnia.client.renderer.entity.SpellChainRenderer;
import com.dslovikosky.narnia.client.renderer.entity.SpellConeRenderer;
import com.dslovikosky.narnia.client.renderer.entity.SpellProjectileRenderer;
import com.dslovikosky.narnia.client.renderer.entity.SpellWallRenderer;
import com.dslovikosky.narnia.client.renderer.entity.WorldWoodBoatRenderer;
import com.dslovikosky.narnia.client.renderer.entity.WorldWoodChestBoatRenderer;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.entity.JadisEntity;
import net.minecraft.client.model.BoatModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class EntityRegistrationHandler {
    @SubscribeEvent
    public void onEntityAttributeCreationEvent(final EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.JADIS.get(), JadisEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(WorldWoodBoatRenderer.LAYER_LOCATION, BoatModel::createBoatModel);
        event.registerLayerDefinition(WorldWoodChestBoatRenderer.LAYER_LOCATION, BoatModel::createChestBoatModel);
        event.registerLayerDefinition(JadisRenderer.LAYER_LOCATION, JadisModel::createBodyLayer);
    }

    @SubscribeEvent
    public void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.WORLD_WOOD_BOAT.get(), WorldWoodBoatRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WORLD_WOOD_CHEST_BOAT.get(), WorldWoodChestBoatRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.JADIS.get(), JadisRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SPELL_AOE.get(), SpellAOERenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SPELL_CHAIN.get(), SpellChainRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SPELL_CONE.get(), SpellConeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SPELL_WALL.get(), SpellWallRenderer::new);
    }
}
