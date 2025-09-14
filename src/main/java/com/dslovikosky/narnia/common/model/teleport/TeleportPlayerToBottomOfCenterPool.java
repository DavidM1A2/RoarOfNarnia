package com.dslovikosky.narnia.common.model.teleport;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record TeleportPlayerToBottomOfCenterPool(ServerLevel level, ChunkPos chunkPos) implements TeleportTransition.PostTeleportTransition {
    private static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onTransition(final Entity entity) {
        final LevelChunk centerChunk = level.getChunk(chunkPos.x, chunkPos.z);

        double waterXSum = 0;
        double waterZSum = 0;
        int waterCount = 0;
        for (int x = chunkPos.getMinBlockX(); x < chunkPos.getMaxBlockX(); x++) {
            for (int z = chunkPos.getMinBlockZ(); z < chunkPos.getMaxBlockZ(); z++) {
                final BlockState blockState = centerChunk.getBlockState(new BlockPos(x, 30, z));
                if (blockState.is(Blocks.WATER)) {
                    waterXSum += x;
                    waterZSum += z;
                    waterCount++;
                }
            }
        }

        Vec3 playerSpawnSpot = null;
        if (waterCount != 0) {
            final double waterXCenter = waterXSum / waterCount;
            final double waterZCenter = waterZSum / waterCount;
            for (int y = 30; y > 0; y--) {
                final BlockState blockState = centerChunk.getBlockState(new BlockPos((int) waterXCenter, y, (int) waterZCenter));
                if (!blockState.is(Blocks.WATER)) {
                    playerSpawnSpot = new Vec3(waterXCenter, y + 2, waterZCenter);
                    break;
                }
            }
        }

        if (playerSpawnSpot == null) {
            LOG.error("Wood between the worlds had an invalid {} chunk, spawning the player at 8, 30, 8", chunkPos);
            playerSpawnSpot = new Vec3(8, 32, 8);
        }

        if (entity instanceof ServerPlayer) {
            ((ServerPlayer) entity).connection.teleport(playerSpawnSpot.x(), playerSpawnSpot.y(), playerSpawnSpot.z(), 0f, 0f);
        } else {
            entity.setPos(playerSpawnSpot);
        }
    }
}
