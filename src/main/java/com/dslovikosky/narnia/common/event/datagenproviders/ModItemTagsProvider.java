package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(final PackOutput pOutput, final CompletableFuture<HolderLookup.Provider> pLookupProvider, final CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable final ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        tag(ItemTags.LOGS).addTag(ModItemTags.WORLD_WOOD);

        tag(ModItemTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD.get().asItem());
        tag(ModItemTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD_LOG.get().asItem());
        tag(ModItemTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD.get().asItem());
        tag(ModItemTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get().asItem());
        tag(ItemTags.PLANKS).add(ModBlocks.WORLD_WOOD_PLANKS.get().asItem());
        tag(ItemTags.WOODEN_BUTTONS).add(ModBlocks.WORLD_WOOD_BUTTON.get().asItem());
        tag(ItemTags.WOODEN_DOORS).add(ModBlocks.WORLD_WOOD_DOOR.get().asItem());
        tag(ItemTags.WOODEN_FENCES).add(ModBlocks.WORLD_WOOD_FENCE.get().asItem());
        tag(ItemTags.FENCE_GATES).add(ModBlocks.WORLD_WOOD_FENCE_GATE.get().asItem());
        tag(ItemTags.LEAVES).add(ModBlocks.WORLD_WOOD_LEAVES.get().asItem());
        tag(ItemTags.SAPLINGS).add(ModBlocks.WORLD_WOOD_SAPLING.get().asItem());
    }
}
