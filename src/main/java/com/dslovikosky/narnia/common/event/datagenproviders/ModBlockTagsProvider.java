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
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD.key());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD_LOG.key());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD.key());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD_LOG.key());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD.key());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD_LOG.key());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.STRIPPED_WORLD_WOOD.key());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.STRIPPED_WORLD_WOOD_LOG.key());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD_PLANKS.key());
        tag(BlockTags.PLANKS).add(ModBlocks.WORLD_WOOD_PLANKS.key());
        tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.WORLD_WOOD_BUTTON.key());
        tag(BlockTags.WOODEN_DOORS).add(ModBlocks.WORLD_WOOD_DOOR.key());
        tag(BlockTags.WOODEN_FENCES).add(ModBlocks.WORLD_WOOD_FENCE.key());
        tag(BlockTags.FENCE_GATES).add(ModBlocks.WORLD_WOOD_FENCE_GATE.key());
        tag(BlockTags.LEAVES).add(ModBlocks.WORLD_WOOD_LEAVES.key());
        tag(BlockItemTags.SAPLINGS.block()).add(ModBlocks.WORLD_WOOD_SAPLING.key());
        tag(BlockTags.WOODEN_SLABS).add(ModBlocks.WORLD_WOOD_SLAB.key());
        tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.WORLD_WOOD_STAIR.key());
        tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.WORLD_WOOD_TRAP_DOOR.key());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.key());
        tag(BlockTags.STANDING_SIGNS).add(ModBlocks.WORLD_WOOD_STANDING_SIGN.key());
        tag(BlockTags.WALL_SIGNS).add(ModBlocks.WORLD_WOOD_WALL_SIGN.key());
        tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.key());
        tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.key());

        tag(ModBlockTags.GRASS_LIKE).addAll(Stream.of(BlockItemIds.GRASS_BLOCK, BlockItemIds.MYCELIUM, BlockItemIds.PODZOL, BlockItemIds.CRIMSON_NYLIUM, BlockItemIds.WARPED_NYLIUM, BlockItemIds.MOSS_BLOCK, BlockItemIds.ROOTED_DIRT).map(BlockItemId::block));

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_COBBLESTONE.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SMOOTH_STONE.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB.key());
        tag(BlockTags.SLABS).add(ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_WINDOW.key());
        tag(BlockTags.DOORS).add(ModBlocks.DARK_CITY_DOOR.key());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICKS.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_STAIRS.key());
        tag(BlockTags.STAIRS).add(ModBlocks.DARK_CITY_STONE_BRICK_STAIRS.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_SLAB.key());
        tag(BlockTags.SLABS).add(ModBlocks.DARK_CITY_STONE_BRICK_SLAB.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_WALL.key());
        tag(BlockTags.WALLS).add(ModBlocks.DARK_CITY_STONE_BRICK_WALL.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.MOSSY_DARK_CITY_STONE_BRICKS.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CHISELED_DARK_CITY_STONE_BRICKS.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_STONE_BRICK_TRAPDOOR.key());
        tag(BlockTags.TRAPDOORS).add(ModBlocks.DARK_CITY_STONE_BRICK_TRAPDOOR.key());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SLATE.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SLATE_SLAB.key());
        tag(BlockTags.SLABS).add(ModBlocks.DARK_CITY_SLATE_SLAB.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DARK_CITY_SLATE_STAIRS.key());
        tag(BlockTags.STAIRS).add(ModBlocks.DARK_CITY_SLATE_STAIRS.key());
    }
}
