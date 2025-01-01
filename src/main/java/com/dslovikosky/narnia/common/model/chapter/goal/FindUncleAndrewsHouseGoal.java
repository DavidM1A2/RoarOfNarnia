package com.dslovikosky.narnia.common.model.chapter.goal;

import com.dslovikosky.narnia.common.constants.ModDataComponentTypes;
import com.dslovikosky.narnia.common.constants.ModStructureTypes;
import com.dslovikosky.narnia.common.model.chapter.ChapterGoal;
import com.dslovikosky.narnia.common.model.chapter.GoalTickResult;
import com.dslovikosky.narnia.common.model.chapter.Scene;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.slf4j.Logger;

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
        if (nearestHouse != null) {
            scene.set(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_LOCATION, nearestHouse.getFirst());
            return true;
        }
        return false;
    }

    @Override
    public GoalTickResult tick(final Scene scene, final Level level) {
        LOG.info("Ticking finding uncle andrews house {}", scene.get(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_LOCATION));
        return GoalTickResult.COMPLETED;
    }

    @Override
    public void registerComponents(DataComponentMap.Builder builder) {
        builder.set(ModDataComponentTypes.UNCLE_ANDREWS_HOUSE_LOCATION, BlockPos.ZERO);
    }
}
