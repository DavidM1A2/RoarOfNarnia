package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class SpellSolarHandler {
    private static final int SOLAR_TICK_INTERVAL = 20;

    // Nighttime is about 14,000 ticks. That means we will fill our cap 14000 / 20 [tick interval] * 0.02 = 14 times
    private static final double VITAE_GAIN_PERCENT_PER_INTERVAL = 0.03;

    // Decay 25 vitae per interval = 200/25 = 8 seconds to decay all vitae
    private static final double VITAE_DECAY_PER_INTERVAL = 25.0;

    public static double getMaxVitae(final Level level) {
        return 200.0;
    }

    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();
        final Level level = player.level();
        if (!level.isClientSide()) {
            if (player.tickCount % SOLAR_TICK_INTERVAL == 0 && player.isAlive()) {
                tickSolarVitae(player);
            }
        }
    }

    private void tickSolarVitae(final Player player) {
        final double oldSolarVitae = player.getData(ModAttachmentTypes.SOLAR_VITAE);
        final Level level = player.level();
        final double newSolarVitae;
        if (isDay(level)) {
            final double vitaeCap = getMaxVitae(player.level());
            newSolarVitae = Math.min(oldSolarVitae + vitaeCap * VITAE_GAIN_PERCENT_PER_INTERVAL, vitaeCap);
        } else {
            // Decay vitae during the day
            newSolarVitae = Math.max(oldSolarVitae - VITAE_DECAY_PER_INTERVAL, 0.0);
        }

        if (oldSolarVitae != newSolarVitae) {
            player.setData(ModAttachmentTypes.SOLAR_VITAE, newSolarVitae);
        }
    }

    private boolean isDay(final Level level) {
        return !level.dimensionType().hasFixedTime() && level.getSkyDarken() < 4;
    }
}
