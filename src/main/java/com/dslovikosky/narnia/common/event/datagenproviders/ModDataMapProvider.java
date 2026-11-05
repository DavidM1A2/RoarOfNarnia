package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Transformable;

import java.util.concurrent.CompletableFuture;

/**
 * 26.3 made axe stripping data driven. The block's getToolModifiedState override is gone, and the log to
 * stripped-log mapping now lives in the neoforge transformables data map, which appends to the vanilla
 * minecraft:axe block transformer. Transformable#stripping copies block properties across, which keeps the
 * log's axis the way the old override did.
 */
public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(final HolderLookup.Provider provider) {
        final var transformables = builder(NeoForgeDataMaps.TRANSFORMABLES);
        addStripping(transformables, ModBlocks.WORLD_WOOD.get(), ModBlocks.STRIPPED_WORLD_WOOD.get());
        addStripping(transformables, ModBlocks.WORLD_WOOD_LOG.get(), ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
    }

    private void addStripping(final Builder<Transformable, Block> builder, final Block original, final Block stripped) {
        builder.add(original.builtInRegistryHolder(), Transformable.stripping(original, stripped), false);
    }
}
