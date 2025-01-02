package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.gui.layer.SceneGoalLayer;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public class GuiLayerRegister {
    @SubscribeEvent
    public void onRegisterGuiLayersEvent(final RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "scene_goal"), new SceneGoalLayer());
    }
}
