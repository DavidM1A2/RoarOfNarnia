package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.event.datagenproviders.ModBlockModelProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModBlockStateProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModBlockTagsProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModEnglishLanguageProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModItemModelProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModItemTagsProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModLootTableProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class DataGenerationHandler {
    @SubscribeEvent
    public void onGatherDataEvent(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new ModBlockModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModEnglishLanguageProvider(output));
        final ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), modBlockTagsProvider);
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, modBlockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
    }
}
