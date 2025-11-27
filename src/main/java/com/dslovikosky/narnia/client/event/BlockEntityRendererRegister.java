package com.dslovikosky.narnia.client.event;

import com.dslovikosky.narnia.client.renderer.block.CharnBellBlockEntityRenderer;
import com.dslovikosky.narnia.client.renderer.block.CharnImageHallStatueBlockEntityRenderer;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public class BlockEntityRendererRegister {
    @SubscribeEvent
    public void onRegisterClientExtensionsEvent(final RegisterClientExtensionsEvent event) {
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CHARN_BELL.get(), context -> new CharnBellBlockEntityRenderer());
        event.registerBlockEntityRenderer(ModBlockEntities.CHARN_IMAGE_HALL_STATUE.get(), context -> new CharnImageHallStatueBlockEntityRenderer());
    }
}
