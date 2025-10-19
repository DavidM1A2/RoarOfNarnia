package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.event.WoodBetweenTheWorldsHandler;
import com.dslovikosky.narnia.common.model.attachment_type.PreRingTeleportData;
import com.dslovikosky.narnia.common.model.attachment_type.PreRingTeleportEntry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@ParametersAreNonnullByDefault
public class RingItem extends Item {
    private static final Logger LOG = LogUtils.getLogger();

    private final Type type;

    public RingItem(final Type type) {
        super(new Properties()
                .stacksTo(1)
                .fireResistant()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, String.format("%s_ring", type.name().toLowerCase(Locale.ROOT))))));
        this.type = type;
    }

    /**
     * This method is server-side only now. Remove @Override, change ServerLevel->Level and call this in a player-tick handler to access it on client & server
     */
    public void inventoryTick(final ItemStack itemStack, final Level level, final Entity entity, final @Nullable EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND) {
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
                        final ChunkPos chunkPos = entity.chunkPosition();
                        final ResourceKey<Level> returnDimension =
                                WoodBetweenTheWorldsHandler.POOL_LEVEL_MAP.getOrDefault(chunkPos, Level.OVERWORLD);
                        final PreRingTeleportData preRingTeleportData = entity.getData(ModAttachmentTypes.PRE_RING_TELEPORT_DATA);
                        final ServerLevel overworld = level.getServer().getLevel(returnDimension);
                        final PreRingTeleportEntry entry = preRingTeleportData.get(returnDimension)
                                .orElse(new PreRingTeleportEntry(Vec3.upFromBottomCenterOf(overworld.getRespawnData().pos(), 1), 0f, 0f));
                        entity.teleport(new TeleportTransition(overworld, entry.position(), Vec3.ZERO, entry.yaw(), entry.pitch(), TeleportTransition.DO_NOTHING));
                    }
                }
            }
        }
    }

    private void tickHeldYellowRing(final Level level, final Entity entity) {
        if (ModDimensions.WOOD_BETWEEN_THE_WORLDS != level.dimension() && !level.isClientSide()) {
            final Vec3 position = entity.position();
            final ResourceKey<Level> dimension = entity.level().dimension();

            final PreRingTeleportData preRingTeleportData = entity.getData(ModAttachmentTypes.PRE_RING_TELEPORT_DATA);
            preRingTeleportData.set(dimension, new PreRingTeleportEntry(position, entity.getYRot(), entity.getXRot()));
            entity.setData(ModAttachmentTypes.PRE_RING_TELEPORT_DATA, preRingTeleportData);

            final ServerLevel woodBetweenTheWorlds = level.getServer().getLevel(ModDimensions.WOOD_BETWEEN_THE_WORLDS);
            final ChunkPos entryChunkPos = WoodBetweenTheWorldsHandler.POOL_LEVEL_MAP.inverse().getOrDefault(dimension, ChunkPos.ZERO);

            final LevelChunk centerChunk = woodBetweenTheWorlds.getChunk(entryChunkPos.x, entryChunkPos.z);

            double waterXSum = 0;
            double waterZSum = 0;
            int waterCount = 0;
            for (int x = entryChunkPos.getMinBlockX(); x < entryChunkPos.getMaxBlockX(); x++) {
                for (int z = entryChunkPos.getMinBlockZ(); z < entryChunkPos.getMaxBlockZ(); z++) {
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
                LOG.error("Wood between the worlds had an invalid {} chunk, spawning the player at 8, 30, 8", entryChunkPos);
                playerSpawnSpot = new Vec3(8, 32, 8);
            }

            entity.teleport(new TeleportTransition(woodBetweenTheWorlds, playerSpawnSpot, Vec3.ZERO, 0f, 0f, TeleportTransition.DO_NOTHING));
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
}
