package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
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
    }
}
