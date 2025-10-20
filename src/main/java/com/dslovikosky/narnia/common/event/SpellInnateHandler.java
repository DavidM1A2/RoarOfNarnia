package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;

public class SpellInnateHandler {
    private static final int INNATE_TICK_INTERVAL = 20;
    private static final double DEFAULT_VITAE_PER_INTERVAL = 1.0;
    private static final Map<ResourceKey<Level>, Double> VITAE_PER_INTERVAL_OVERRIDES = Map.of(
            ModDimensions.DARK_CITY_RUINS, 25.0,
            ModDimensions.WOOD_BETWEEN_THE_WORLDS, 0.0,
            Level.NETHER, 1.5,
            Level.END, 5.0
    );
    private static final double DEFAULT_MAX_VITAE = 20.0;
    private static final Map<ResourceKey<Level>, Double> MAX_VITAE_OVERRIDES = Map.of(
            ModDimensions.DARK_CITY_RUINS, 1000.0,
            ModDimensions.WOOD_BETWEEN_THE_WORLDS, 0.0,
            Level.NETHER, 25.0,
            Level.END, 100.0
    );

    private static double getVitaePerInterval(final Level level) {
        return VITAE_PER_INTERVAL_OVERRIDES.getOrDefault(level.dimension(), DEFAULT_VITAE_PER_INTERVAL);
    }

    public static double getMaxVitae(final Level level) {
        return MAX_VITAE_OVERRIDES.getOrDefault(level.dimension(), DEFAULT_MAX_VITAE);
    }

    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();
        final Level level = player.level();
        if (!level.isClientSide()) {
            if (player.tickCount % INNATE_TICK_INTERVAL == 0 && player.isAlive()) {
                final double vitaePerInterval = getVitaePerInterval(level);
                final double maxVitae = getMaxVitae(level);
                final double oldVitae = player.getData(ModAttachmentTypes.INNATE_VITAE);
                final double newVitae = Math.min((oldVitae + vitaePerInterval), maxVitae);
                if (oldVitae != newVitae) {
                    player.setData(ModAttachmentTypes.INNATE_VITAE, newVitae);
                }
            }
        }
    }
}
