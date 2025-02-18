package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.entity.base.ItemBlockEntityRenderer;
import com.dslovikosky.narnia.client.entity.ring_box.RingBoxBlockRenderer;
import com.dslovikosky.narnia.client.entity.ring_box.RingBoxItemRenderer;
import com.dslovikosky.narnia.client.entity.ring_box.RingBoxModel;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public class BlockEntityRendererRegister {
    @SubscribeEvent
    public void onRegisterClientExtensionsEvent(final RegisterClientExtensionsEvent event) {
        event.registerItem(new ItemBlockEntityRenderer(new RingBoxItemRenderer()), ModBlocks.RING_BOX.asItem());
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RingBoxModel.LAYER_LOCATION, RingBoxModel::createBodyLayer);
    }

    @SubscribeEvent
    public void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.RING_BOX.get(), RingBoxBlockRenderer::new);
    }
}
