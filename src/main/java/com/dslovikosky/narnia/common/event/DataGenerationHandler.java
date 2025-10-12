package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.event.datagenproviders.ModBlockTagsProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModEnglishLanguageProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModItemTagsProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModLootTableProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModModelProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModParticleDescriptionProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModRecipeProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModSoundDefinitionsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class DataGenerationHandler {
    @SubscribeEvent
    public void onGatherDataEvent(final GatherDataEvent.Client event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ModEnglishLanguageProvider(output));
        generator.addProvider(true, new ModSoundDefinitionsProvider(output));
        final ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(output, lookupProvider);
        generator.addProvider(true, modBlockTagsProvider);
        generator.addProvider(true, new ModItemTagsProvider(output, lookupProvider));
        generator.addProvider(true, new ModLootTableProvider(output, lookupProvider));
        generator.addProvider(true, new ModRecipeProvider.Runner(output, lookupProvider));
        generator.addProvider(true, new ModModelProvider(output));
        generator.addProvider(true, new ModParticleDescriptionProvider(output));
    }
}
