package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItemTags;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(final PackOutput pOutput, final CompletableFuture<HolderLookup.Provider> pLookupProvider) {
        super(pOutput, pLookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        tag(ItemTags.LOGS).addTag(ModItemTags.WORLD_WOOD);

        tag(ItemTags.BOATS).add(ModItems.WORLD_WOOD_BOAT.key());
        tag(ItemTags.CHEST_BOATS).add(ModItems.WORLD_WOOD_CHEST_BOAT.key());
        tag(ItemTags.SIGNS).add(ModItems.WORLD_WOOD_STANDING_SIGN.key());
        tag(ItemTags.HANGING_SIGNS).add(ModItems.WORLD_WOOD_HANGING_SIGN.key());

        tag(ModItemTags.WORLD_WOOD).add(blockItemKey(ModBlocks.WORLD_WOOD));
        tag(ModItemTags.WORLD_WOOD).add(blockItemKey(ModBlocks.WORLD_WOOD_LOG));
        tag(ModItemTags.WORLD_WOOD).add(blockItemKey(ModBlocks.STRIPPED_WORLD_WOOD));
        tag(ModItemTags.WORLD_WOOD).add(blockItemKey(ModBlocks.STRIPPED_WORLD_WOOD_LOG));
        tag(ItemTags.PLANKS).add(blockItemKey(ModBlocks.WORLD_WOOD_PLANKS));
        tag(ItemTags.WOODEN_BUTTONS).add(blockItemKey(ModBlocks.WORLD_WOOD_BUTTON));
        tag(ItemTags.WOODEN_DOORS).add(blockItemKey(ModBlocks.WORLD_WOOD_DOOR));
        tag(ItemTags.WOODEN_FENCES).add(blockItemKey(ModBlocks.WORLD_WOOD_FENCE));
        tag(ItemTags.FENCE_GATES).add(blockItemKey(ModBlocks.WORLD_WOOD_FENCE_GATE));
        tag(ItemTags.LEAVES).add(blockItemKey(ModBlocks.WORLD_WOOD_LEAVES));
        tag(ItemTags.SAPLINGS).add(blockItemKey(ModBlocks.WORLD_WOOD_SAPLING));
        tag(BlockItemTags.SLABS.item()).add(blockItemKey(ModBlocks.WORLD_WOOD_SLAB));
        tag(ItemTags.WOODEN_STAIRS).add(blockItemKey(ModBlocks.WORLD_WOOD_STAIR));
        tag(ItemTags.WOODEN_TRAPDOORS).add(blockItemKey(ModBlocks.WORLD_WOOD_TRAP_DOOR));
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(blockItemKey(ModBlocks.WORLD_WOOD_PRESSURE_PLATE));

        tag(ModItemTags.REPAIRS_ANCIENT_ARMOR).add(ModItems.ANCIENT_METAL.key());
        tag(ModItemTags.ANCIENT_TOOL_MATERIALS).add(ModItems.ANCIENT_METAL.key());
    }

    // Block items are registered in bulk via registerSimpleBlockItem, so they share the block's registry name
    private static ResourceKey<Item> blockItemKey(final DeferredBlock<?> block) {
        return ResourceKey.create(Registries.ITEM, block.getId());
    }
}
