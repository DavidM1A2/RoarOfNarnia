package com.dslovikosky.narnia.common.model.chapter.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.constants.ModSchematics;
import com.dslovikosky.narnia.common.constants.ModStructureTypes;
import com.dslovikosky.narnia.common.model.chapter.Actor;
import com.dslovikosky.narnia.common.model.chapter.ChapterGoal;
import com.dslovikosky.narnia.common.model.chapter.GoalTickResult;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class FindUncleAndrewsHouseGoal implements ChapterGoal {
    private static final ResourceKey<Structure> UNCLE_ANDREWS_HOUSE = ResourceKey.create(Registries.STRUCTURE, ModStructureTypes.UNCLE_ANDREWS_HOUSE.getId());
    private static final Logger LOG = LogUtils.getLogger();

    @Override
    public boolean start(final Scene scene, final Level level) {
        if (level.isClientSide()) {
            return false;
        }

        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }

        final ServerLevel overworld = (ServerLevel) level;
        final ChunkGenerator generator = overworld.getChunkSource().getGenerator();
        final Holder.Reference<Structure> uncleAndrewsHouseReference = overworld.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getHolderOrThrow(UNCLE_ANDREWS_HOUSE);

        final Pair<BlockPos, Holder<Structure>> nearestHouse = generator.findNearestMapStructure(
                overworld, HolderSet.direct(uncleAndrewsHouseReference), BlockPos.ZERO, 100, false);
        if (nearestHouse == null) {
            return false;
        }

        final BlockPos nearestHousePos = nearestHouse.getFirst();
        final ChunkAccess chunk = level.getChunk(nearestHousePos);
        final StructureStart houseStart = chunk.getStartForStructure(nearestHouse.getSecond().value());

        if (houseStart == null || houseStart.getPieces().isEmpty()) {
            return false;
        }

        final Direction direction = houseStart.getPieces().getFirst().getOrientation();
        scene.set(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_LOCATION, getWorldPos(houseStart, direction,
                ModSchematics.UNCLE_ANDREWS_HOUSE.width() / 2, 0, ModSchematics.UNCLE_ANDREWS_HOUSE.length() / 2));
        scene.set(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_DIRECTION, direction);
        return true;
    }

    @Override
    public GoalTickResult tick(final Scene scene, final Level level) {
        final List<Actor> actors = scene.getActors();
        if (actors.isEmpty()) {
            return GoalTickResult.CONTINUE;
        }
        final List<Actor> playerActors = actors.stream().filter(Actor::isPlayerControlled).toList();
        if (playerActors.isEmpty()) {
            return GoalTickResult.CONTINUE;
        }
        final List<Entity> playerEntities = playerActors.stream()
                .map(actor -> actor.getEntity(level))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (playerEntities.isEmpty()) {
            return GoalTickResult.CONTINUE;
        }

        final BlockPos location = scene.getOrDefault(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_LOCATION, BlockPos.ZERO);
        if (playerEntities.stream().allMatch(playerEntity -> playerEntity.distanceToSqr(Vec3.atCenterOf(location)) < 25 * 25)) {
            return GoalTickResult.COMPLETED;
        }

        return GoalTickResult.CONTINUE;
    }

    @Override
    public void registerComponents(DataComponentMap.Builder builder) {
        builder.set(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_LOCATION, BlockPos.ZERO);
        builder.set(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_DIRECTION, Direction.NORTH);
    }

    @Override
    public Component getDescription(Scene scene, Level level) {
        final BlockPos location = scene.getOrDefault(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_LOCATION, BlockPos.ZERO);
        return Component.literal(String.format("Walk to Uncle Andrew's House at x = %s, z = %s", location.getX(), location.getZ()));
    }

    private BlockPos getWorldPos(final StructureStart start, final Direction direction, int offsetX, int offsetY, int offsetZ) {
        return new BlockPos(this.getWorldX(start, direction, offsetX, offsetZ), this.getWorldY(start, direction, offsetY), this.getWorldZ(start, direction, offsetX, offsetZ));
    }

    private int getWorldX(final StructureStart start, final Direction direction, final int pX, final int pZ) {
        return switch (direction) {
            case NORTH, SOUTH -> start.getBoundingBox().minX() + pX;
            case WEST -> start.getBoundingBox().maxX() - pZ;
            case EAST -> start.getBoundingBox().minX() + pZ;
            default -> pX;
        };
    }

    private int getWorldY(final StructureStart start, final Direction direction, final int pY) {
        return direction == null ? pY : pY + start.getBoundingBox().minY();
    }

    private int getWorldZ(final StructureStart start, final Direction direction, final int pX, final int pZ) {
        return switch (direction) {
            case NORTH -> start.getBoundingBox().maxZ() - pZ;
            case SOUTH -> start.getBoundingBox().minZ() + pZ;
            case WEST, EAST -> start.getBoundingBox().minZ() + pX;
            default -> pZ;
        };
    }
}
