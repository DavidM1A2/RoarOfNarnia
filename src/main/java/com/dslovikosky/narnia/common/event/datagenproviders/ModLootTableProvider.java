package com.dslovikosky.narnia.common.event.datagenproviders;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(final PackOutput pOutput, final CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, Set.of(), List.of(
                new SubProviderEntry(ModBlockLootTableSubProvider::new, LootContextParamSets.BLOCK)
        ), pRegistries);
    }
}
