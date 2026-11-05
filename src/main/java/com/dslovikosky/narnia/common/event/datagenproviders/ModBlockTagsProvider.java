package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlockTags;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        tag(BlockTags.LOGS).addTag(ModBlockTags.WORLD_WOOD);
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD.getKey());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD_LOG.getKey());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD.getKey());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD_LOG.getKey());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD.getKey());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD_LOG.getKey());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.STRIPPED_WORLD_WOOD.getKey());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.STRIPPED_WORLD_WOOD_LOG.getKey());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD_PLANKS.getKey());
        tag(BlockTags.PLANKS).add(ModBlocks.WORLD_WOOD_PLANKS.getKey());
        tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.WORLD_WOOD_BUTTON.getKey());
        tag(BlockTags.WOODEN_DOORS).add(ModBlocks.WORLD_WOOD_DOOR.getKey());
        tag(BlockTags.WOODEN_FENCES).add(ModBlocks.WORLD_WOOD_FENCE.getKey());
        tag(BlockTags.FENCE_GATES).add(ModBlocks.WORLD_WOOD_FENCE_GATE.getKey());
        tag(BlockTags.LEAVES).add(ModBlocks.WORLD_WOOD_LEAVES.getKey());
        tag(BlockItemTags.SAPLINGS.block()).add(ModBlocks.WORLD_WOOD_SAPLING.getKey());
        tag(BlockTags.WOODEN_SLABS).add(ModBlocks.WORLD_WOOD_SLAB.getKey());
        tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.WORLD_WOOD_STAIR.getKey());
        tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.WORLD_WOOD_TRAP_DOOR.getKey());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.getKey());
        tag(BlockTags.STANDING_SIGNS).add(ModBlocks.WORLD_WOOD_STANDING_SIGN.getKey());
        tag(BlockTags.WALL_SIGNS).add(ModBlocks.WORLD_WOOD_WALL_SIGN.getKey());
        tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.getKey());
        tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.getKey());

        tag(ModBlockTags.GRASS_LIKE).addAll(Stream.of(BlockItemIds.GRASS_BLOCK, BlockItemIds.MYCELIUM, BlockItemIds.PODZOL, BlockItemIds.CRIMSON_NYLIUM, BlockItemIds.WARPED_NYLIUM, BlockItemIds.MOSS_BLOCK, BlockItemIds.ROOTED_DIRT).map(BlockItemId::block));

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_COBBLESTONE.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SMOOTH_STONE.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB.getKey());
        tag(BlockTags.SLABS).add(ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_WINDOW.getKey());
        tag(BlockTags.DOORS).add(ModBlocks.DARK_CITY_DOOR.getKey());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICKS.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_STAIRS.getKey());
        tag(BlockTags.STAIRS).add(ModBlocks.DARK_CITY_STONE_BRICK_STAIRS.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_SLAB.getKey());
        tag(BlockTags.SLABS).add(ModBlocks.DARK_CITY_STONE_BRICK_SLAB.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_WALL.getKey());
        tag(BlockTags.WALLS).add(ModBlocks.DARK_CITY_STONE_BRICK_WALL.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.MOSSY_DARK_CITY_STONE_BRICKS.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CHISELED_DARK_CITY_STONE_BRICKS.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_TRAPDOOR.getKey());
        tag(BlockTags.TRAPDOORS).add(ModBlocks.DARK_CITY_STONE_BRICK_TRAPDOOR.getKey());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SLATE.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SLATE_SLAB.getKey());
        tag(BlockTags.SLABS).add(ModBlocks.DARK_CITY_SLATE_SLAB.getKey());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SLATE_STAIRS.getKey());
        tag(BlockTags.STAIRS).add(ModBlocks.DARK_CITY_SLATE_STAIRS.getKey());
    }
}
