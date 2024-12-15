package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlockTags;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        tag(BlockTags.LOGS).addTag(ModBlockTags.WORLD_WOOD);
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD.get());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.WORLD_WOOD_LOG.get());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD.get());
        tag(ModBlockTags.WORLD_WOOD).add(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD_LOG.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.STRIPPED_WORLD_WOOD.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.WORLD_WOOD_PLANKS.get());
        tag(BlockTags.PLANKS).add(ModBlocks.WORLD_WOOD_PLANKS.get());
        tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.WORLD_WOOD_BUTTON.get());
        tag(BlockTags.WOODEN_DOORS).add(ModBlocks.WORLD_WOOD_DOOR.get());
    }
}
