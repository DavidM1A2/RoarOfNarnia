package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.model.NarniaGlobalData;
import com.dslovikosky.narnia.server.world.saved.NarniaGlobalSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

public class NarniaGlobalDataHandler {
    @SubscribeEvent
    public void onServerStartedEvent(final ServerStartedEvent event) {
        // Forces data to be attached to the world and initialized
        NarniaGlobalSavedData.get(event.getServer());
    }

    @SubscribeEvent
    public void onPlayerLoggedInEvent(final PlayerEvent.PlayerLoggedInEvent event) {
        final Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            NarniaGlobalData.getInstance(player.level()).syncTo(serverPlayer);
        }
    }
}
