package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModBlockLootTableSubProvider extends BlockLootSubProvider {
    private final Set<Block> blocksWithoutDrops = Set.of(ModBlocks.POSITIONAL_MARKER.get());
    private final Map<LeavesBlock, Block> leafToSapling = new HashMap<>();

    protected ModBlockLootTableSubProvider(final HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
        leafToSapling.put(ModBlocks.WORLD_WOOD_LEAVES.get(), ModBlocks.WORLD_WOOD_SAPLING.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.value()).toList();
    }

    @Override
    protected void generate() {
        ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).forEach(block -> {
            if (!blocksWithoutDrops.contains(block)) {
                if (block instanceof DoorBlock) {
                    add(block, createDoorTable(block));
                } else if (block instanceof LeavesBlock) {
                    add(block, createLeavesDrops(block, leafToSapling.get(block), NORMAL_LEAVES_SAPLING_CHANCES));
                } else if (block instanceof SlabBlock) {
                    add(block, createSlabItemTable(block));
                } else {
                    dropSelf(block);
                }
            }
        });
    }
}
