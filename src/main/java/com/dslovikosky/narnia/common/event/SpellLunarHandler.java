package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;

public class SpellLunarHandler {
    // The maximum amount of vitae we can store in each moon phase
    private static final Map<Integer, Double> VITAE_CAP_BY_MOON_PHASE = Map.of(
            0, 20000.0, // Full (100%)
            1, 500.0, // Waning Gibbous (75%)
            2, 300.0, // Third Quarter (50%)
            3, 200.0, // Waning Crescent (25%)
            4, 100.0, // New Moon (0%)
            5, 200.0, // Waxing Crescent (25%)
            6, 300.0, // First Quarter (50%)
            7, 500.0 // Waxing Gibbous (75%)
    );

    private static final int LUNAR_TICK_INTERVAL = 20;

    // Nighttime is about 10,000 ticks. That means we will fill our cap 10000 / 20 [tick interval] * 0.01 = 5 times
    private static final double VITAE_GAIN_PERCENT_PER_INTERVAL = 0.01;

    // Decay 20 vitae per interval. After a full moon it will take a max of 2000 / 50 = 40 intervals = 40 seconds
    private static final double VITAE_DECAY_PER_INTERVAL = 50.0;

    public static double getMaxVitae(final Level level) {
        final int moonPhase = level.dimensionType().moonPhase(level.dayTime());
        return VITAE_CAP_BY_MOON_PHASE.getOrDefault(moonPhase, 0.0);
    }

    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();
        final Level level = player.level();
        if (!level.isClientSide()) {
            if (player.tickCount % LUNAR_TICK_INTERVAL == 0 && player.isAlive()) {
                tickLunarVitae(player);
            }
        }
    }

    private void tickLunarVitae(final Player player) {
        final double oldLunarVitae = player.getData(ModAttachmentTypes.LUNAR_VITAE);
        final Level level = player.level();
        final double newLunarVitae;
        if (isDay(level)) {
            // Decay vitae during the day
            newLunarVitae = Math.max(oldLunarVitae - VITAE_DECAY_PER_INTERVAL, 0.0);
        } else {
            final double currentVitaeCap = getMaxVitae(level);
            // Work towards our vitae cap at night
            if (oldLunarVitae > currentVitaeCap) {
                // Decay to the cap. This avoid players "cheesing" the power source by logging off after a full moon to preserve vitae
                newLunarVitae = Math.max(oldLunarVitae - VITAE_DECAY_PER_INTERVAL, currentVitaeCap);
            } else {
                // Grow to the cap
                newLunarVitae = Math.min(oldLunarVitae + currentVitaeCap * VITAE_GAIN_PERCENT_PER_INTERVAL, currentVitaeCap);
            }
        }

        if (oldLunarVitae != newLunarVitae) {
            player.setData(ModAttachmentTypes.LUNAR_VITAE, newLunarVitae);
        }
    }

    private boolean isDay(final Level level) {
        return !level.dimensionType().hasFixedTime() && level.getSkyDarken() < 4;
    }
}
