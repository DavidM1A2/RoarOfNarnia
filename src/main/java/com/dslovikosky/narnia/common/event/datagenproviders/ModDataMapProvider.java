package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Transformable;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(final HolderLookup.Provider provider) {
        final var transformables = builder(NeoForgeDataMaps.TRANSFORMABLES);
        addStripping(transformables, ModBlocks.WORLD_WOOD, ModBlocks.STRIPPED_WORLD_WOOD);
        addStripping(transformables, ModBlocks.WORLD_WOOD_LOG, ModBlocks.STRIPPED_WORLD_WOOD_LOG);
    }

    private void addStripping(final Builder<Transformable, Block> builder, final DeferredBlock<?> original, final DeferredBlock<?> stripped) {
        builder.add(original.key(), Transformable.stripping(original.get(), stripped.get()), false);
    }
}
