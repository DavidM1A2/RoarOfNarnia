package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.model.attachment_type.ThermalData;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;

public class SpellThermalHandler {
    private static final int THERMAL_TICK_INTERVAL = 20;

    // Max of +/- 5 vitae per interval because of biome
    private static final double BIOME_VITAE_MODIFIER_PER_INTERVAL = 3.0;

    // Max of +/- 15 vitae per interval because of depth
    private static final double DEPTH_VITAE_MODIFIER_PER_INTERVAL = 15.0;

    // Max of +/- 15 vitae per interval because of heat
    private static final double HEAT_VITAE_MODIFIER_PER_INTERVAL = 15.0;
    private static final int HEAT_SAMPLE_DISTANCE = 4;
    private static final int HEAT_SAMPLE_COUNT = 7;

    // Max/Min heat
    private static final double MIN_HEAT = -2.0;
    private static final double MAX_HEAT = 2.0;

    // How much heat each block emits. Default is 0, and ranges from -1 to 1
    private static final Map<Block, Double> HEAT_BLOCK_EMISSION = ImmutableMap.<Block, Double>builder()
            // Warm blocks
            .put(Blocks.BLAST_FURNACE, 0.2)
            .put(Blocks.FURNACE, 0.1)
            .put(Blocks.CAMPFIRE, 0.8)
            .put(Blocks.FIRE, 0.8)
            .put(Blocks.LAVA, 1.0)
            .put(Blocks.MAGMA_BLOCK, 0.4)
            .put(Blocks.LANTERN, 0.2)
            .put(Blocks.JACK_O_LANTERN, 0.05)
            .put(Blocks.SOUL_LANTERN, 0.05)
            .put(Blocks.TORCH, 0.5)
            .put(Blocks.WALL_TORCH, 0.5)
            .put(Blocks.SOUL_TORCH, 0.1)
            .put(Blocks.SOUL_WALL_TORCH, 0.1)
            .put(Blocks.REDSTONE_TORCH, 0.1)
            .put(Blocks.REDSTONE_WALL_TORCH, 0.1)
            // Cold blocks
            .put(Blocks.FROSTED_ICE, -0.8)
            .put(Blocks.ICE, -0.8)
            .put(Blocks.PACKED_ICE, -0.9)
            .put(Blocks.BLUE_ICE, -1.0)
            .put(Blocks.WATER, -0.3)
            .put(Blocks.SNOW_BLOCK, -0.5)
            .put(Blocks.SNOW, -0.4)
            .build();

    public static double getMaxVitae(final Level level) {
        return 500.0;
    }

    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent.Pre event) {
        final Player player = event.getEntity();
        final Level level = player.level();
        if (!level.isClientSide()) {
            if (player.tickCount % THERMAL_TICK_INTERVAL == 0 && player.isAlive()) {
                tickThermalVitae(player);
            }
        }
    }

    private void tickThermalVitae(final Player player) {
        final ThermalData oldThermalData = player.getData(ModAttachmentTypes.THERMAL_DATA);
        final ThermalData newThermalData = new ThermalData(oldThermalData.getVitae(), oldThermalData.getHeat());

        tickBiome(player, newThermalData);
        tickDepth(player, newThermalData);
        tickNearbyBlocks(player, newThermalData);
        newThermalData.setVitae(Mth.clamp(newThermalData.getVitae(), 0.0, getMaxVitae(player.level())));
        newThermalData.setHeat(Math.clamp(newThermalData.getHeat(), MIN_HEAT, MAX_HEAT));

        if (!oldThermalData.equals(newThermalData)) {
            player.setData(ModAttachmentTypes.THERMAL_DATA, newThermalData);
        }
    }

    private void tickBiome(final Player player, final ThermalData thermalData) {
        final Level level = player.level();
        final Biome biome = level.getBiome(player.blockPosition()).value();
        if (biome.coldEnoughToSnow(player.blockPosition(), level.getSeaLevel())) {
            thermalData.setVitae(thermalData.getVitae() - BIOME_VITAE_MODIFIER_PER_INTERVAL);
        }
        if (biome.warmEnoughToRain(player.blockPosition(), level.getSeaLevel())) {
            thermalData.setVitae(thermalData.getVitae() + BIOME_VITAE_MODIFIER_PER_INTERVAL);
        }
    }

    private void tickDepth(final Player player, final ThermalData thermalData) {
        final Level level = player.level();
        final double minY = level.getMinY();
        final double maxY = level.getMaxY();
        final double zeroToOneY = (player.position().y - minY) / (maxY - minY);
        final double negativeToPositiveOne = zeroToOneY * 2.0 - 1.0;
        final double changeMultiplier = Math.copySign(Math.pow(Math.abs(negativeToPositiveOne), 3.0), negativeToPositiveOne);
        thermalData.setVitae(thermalData.getVitae() + changeMultiplier * DEPTH_VITAE_MODIFIER_PER_INTERVAL);
    }

    private void tickNearbyBlocks(final Player player, final ThermalData thermalData) {
        final Level level = player.level();

        double currentHeatEstimate = Double.NEGATIVE_INFINITY;
        // Sample nearby blocks and get their heat values
        for (int i = 0; i < HEAT_SAMPLE_COUNT; i++) {
            final BlockPos samplePos = player.blockPosition().offset(
                    player.getRandom().nextInt(HEAT_SAMPLE_DISTANCE * 2) - HEAT_SAMPLE_DISTANCE,
                    player.getRandom().nextInt(HEAT_SAMPLE_DISTANCE * 2) - HEAT_SAMPLE_DISTANCE,
                    player.getRandom().nextInt(HEAT_SAMPLE_DISTANCE * 2) - HEAT_SAMPLE_DISTANCE
            );

            final Double emissionValue = HEAT_BLOCK_EMISSION.get(level.getBlockState(samplePos).getBlock());
            if (emissionValue == null) {
                continue;
            }

            // Hot things beat cold things. Take the warmest block around to set the heat level
            currentHeatEstimate = Math.max(currentHeatEstimate, emissionValue);
        }

        // If no blocks around us were hot or cold, move towards equilibrium (0.0)
        if (currentHeatEstimate == Double.NEGATIVE_INFINITY) {
            currentHeatEstimate = 0.0;
        }

        // Heat will slowly change over time. Take 70% of the old value and 30% of the new value as the new "heat" value
        thermalData.setHeat(thermalData.getHeat() * 0.7 + currentHeatEstimate * 0.3);
        thermalData.setVitae(thermalData.getVitae() + thermalData.getHeat() * HEAT_VITAE_MODIFIER_PER_INTERVAL);
    }
}
