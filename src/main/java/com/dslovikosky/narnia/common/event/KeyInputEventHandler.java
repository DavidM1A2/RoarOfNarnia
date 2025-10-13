package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.client.gui.screen.PowerSourceSelectionScreen;
import com.dslovikosky.narnia.common.constants.ModKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class KeyInputEventHandler {
    private boolean wasPowerSourceSelectorKeyDown = false;

    @SubscribeEvent
    public void onKeyInputEvent(final ClientTickEvent.Post event) {
        // This gets fired in the main menu, or when we have an inventory open. In either case return
        final Player player = Minecraft.getInstance().player;
        if (player == null || Minecraft.getInstance().screen != null) {
            return;
        }

        // Process input
        final boolean isPowerSourceSelectorKeyDown = ModKeyMappings.POWER_SOURCE_SELECTOR.isDown();
        if (isPowerSourceSelectorKeyDown && !wasPowerSourceSelectorKeyDown) {
            Minecraft.getInstance().setScreen(new PowerSourceSelectionScreen());
        }
        wasPowerSourceSelectorKeyDown = isPowerSourceSelectorKeyDown;
    }
}
