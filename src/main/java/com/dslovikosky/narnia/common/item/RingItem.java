package com.dslovikosky.narnia.common.item;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.event.WoodBetweenTheWorldsHandler;
import com.dslovikosky.narnia.common.model.attachment_type.PreRingTeleportData;
import com.dslovikosky.narnia.common.model.attachment_type.PreRingTeleportEntry;
import com.dslovikosky.narnia.common.model.teleport.TeleportPlayerToBottomOfCenterPool;
import com.dslovikosky.narnia.common.model.teleport.TeleportPlayerToPreTeleportPosition;
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
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@ParametersAreNonnullByDefault
public class RingItem extends Item {
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
                                .orElse(new PreRingTeleportEntry(Vec3.upFromBottomCenterOf(overworld.getSharedSpawnPos(), 1), 0f, 0f));
                        entity.teleport(new TeleportTransition(overworld, entity, new TeleportPlayerToPreTeleportPosition(entry.position(), entry.yaw(), entry.pitch())));
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
            entity.teleport(new TeleportTransition(woodBetweenTheWorlds, entity, new TeleportPlayerToBottomOfCenterPool(woodBetweenTheWorlds, entryChunkPos)));
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
