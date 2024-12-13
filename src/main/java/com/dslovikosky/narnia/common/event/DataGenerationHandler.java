package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.event.datagenproviders.ModEnglishLanguageProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DataGenerationHandler {
    @SubscribeEvent
    public void onGatherDataEvent(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModEnglishLanguageProvider(output));
    }
}
