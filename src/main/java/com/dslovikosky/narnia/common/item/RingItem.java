package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.model.PreTeleportLocation;
import com.dslovikosky.narnia.common.utils.TeleportPlayerToPreTeleportPosition;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RingItem extends Item {
    private final Type type;

    public RingItem(final Type type) {
        super(new Properties().stacksTo(1).fireResistant());
        this.type = type;
    }

    @Override
    public void inventoryTick(final ItemStack itemStack, final Level level, final Entity entity, final int slotId, final boolean isSelected) {
        if (!isSelected) {
            return;
        }

        if (type == Type.GREEN) {
            tickHeldGreenRing(level, entity);
        } else if (type == Type.YELLOW) {
            tickHeldYellowRing(level, entity);
        }
    }

    private void tickHeldGreenRing(final Level level, final Entity entity) {
        if (ModDimensions.WOOD_BETWEEN_THE_WORLDS == level.dimension()) {
            // Push entities in the wood between the worlds down
            if (entity.isInWater() || entity.isUnderWater()) {
                final double yMovement = entity.getDeltaMovement().y;
                if (yMovement > -0.5) {
                    entity.push(0.0, -0.1, 0.0);
                }
                if (entity.onGround()) {
                    if (!level.isClientSide()) {
                        final PreTeleportLocation preTeleportLocation = entity.getData(ModAttachmentTypes.PRE_YELLOW_RING_TELEPORT_LOCATION);
                        final ServerLevel overworld = level.getServer().getLevel(preTeleportLocation.level());
                        entity.changeDimension(new DimensionTransition(overworld, entity, new TeleportPlayerToPreTeleportPosition(preTeleportLocation)));
                    }
                }
            }
        }
    }

    private void tickHeldYellowRing(final Level level, final Entity entity) {
        if (ModDimensions.WOOD_BETWEEN_THE_WORLDS != level.dimension() && !level.isClientSide()) {
            entity.setData(ModAttachmentTypes.PRE_YELLOW_RING_TELEPORT_LOCATION, new PreTeleportLocation(entity.position().x(), entity.position().y(), entity.position().z(), entity.level().dimension()));
            final ServerLevel woodBetweenTheWorlds = level.getServer().getLevel(ModDimensions.WOOD_BETWEEN_THE_WORLDS);
            entity.changeDimension(new DimensionTransition(woodBetweenTheWorlds, entity, new TeleportPlayerToBottomOfCenterPool(woodBetweenTheWorlds)));
            return;
        }

        // Push entities in the wood between the worlds up
        if (ModDimensions.WOOD_BETWEEN_THE_WORLDS == level.dimension()) {
            if (entity.isInWater() || entity.isUnderWater()) {
                final double yMovement = entity.getDeltaMovement().y;
                if (yMovement < 0.5) {
                    entity.push(0.0, 0.1, 0.0);
                }
            }
        }
    }

    public enum Type {
        YELLOW, GREEN
    }

    private record TeleportPlayerToBottomOfCenterPool(ServerLevel level) implements DimensionTransition.PostDimensionTransition {
        private static final Logger LOG = LogUtils.getLogger();

        @Override
        public void onTransition(final Entity entity) {
            final LevelChunk centerChunk = level.getChunk(0, 0);

            double waterXSum = 0;
            double waterZSum = 0;
            int waterCount = 0;
            for (int x = 0; x < 15; x++) {
                for (int z = 0; z < 15; z++) {
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
                LOG.error("Wood between the worlds had an invalid 0,0 chunk, spawning the player at 8, 30, 8");
                playerSpawnSpot = new Vec3(8, 32, 8);
            }

            if (entity instanceof ServerPlayer) {
                ((ServerPlayer) entity).connection.teleport(playerSpawnSpot.x(), playerSpawnSpot.y(), playerSpawnSpot.z(), 0f, 0f);
            } else {
                entity.setPos(playerSpawnSpot);
            }
        }
    }
}
