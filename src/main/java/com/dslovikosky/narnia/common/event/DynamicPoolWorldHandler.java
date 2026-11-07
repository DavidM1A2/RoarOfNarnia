package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.world.dimension.DynamicPoolWorldManager;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class DynamicPoolWorldHandler {
    private static final int UNLOAD_CHECK_INTERVAL_TICKS = 20;

    @SubscribeEvent
    public void onServerStartingEvent(final ServerStartingEvent event) {
        // Deliberately not ServerAboutToStartEvent, which fires before the levels are loaded and so has no overworld to
        // derive level data from. This still runs before anyone can connect, which is the part that matters.
        DynamicPoolWorldManager.restoreVisitedWorlds(event.getServer());
    }

    @SubscribeEvent
    public void onServerTickEvent(final ServerTickEvent.Post event) {
        final MinecraftServer server = event.getServer();
        if (server.getTickCount() % UNLOAD_CHECK_INTERVAL_TICKS == 0) {
            // Post, not Pre: unloading removes from the map that the level tick loop iterates
            DynamicPoolWorldManager.unloadEmptyWorlds(server, UNLOAD_CHECK_INTERVAL_TICKS);
        }
    }

    @SubscribeEvent
    public void onServerStoppedEvent(final ServerStoppedEvent event) {
        DynamicPoolWorldManager.clear();
    }
}
