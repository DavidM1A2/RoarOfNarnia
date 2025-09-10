package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModGuiLayers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public class GuiLayerRegister {
    @SubscribeEvent
    public void onRegisterGuiLayersEvent(final RegisterGuiLayersEvent event) {
        event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "drowsy_vignette"), ModGuiLayers.DROWSY_VIGNETTE);
    }
}
