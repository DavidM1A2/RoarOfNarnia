package com.dslovikosky.narnia.common.event;

import com.dslovikosky.narnia.common.event.datagenproviders.ModBlockTagsProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModDataMapProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModEnglishLanguageProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModItemTagsProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModModelProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModParticleDescriptionProvider;
import com.dslovikosky.narnia.common.event.datagenproviders.ModSoundDefinitionsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.PackGenerator;

public class DataGenerationHandler {
    @SubscribeEvent
    public void onGatherDataEvent(final GatherDataEvent.Client event) {
        final PackGenerator packGenerator = event.getDefaultPackGenerator();

        packGenerator.createProvider(ModEnglishLanguageProvider::new);
        packGenerator.createProvider(ModSoundDefinitionsProvider::new);
        packGenerator.createProvider(ModBlockTagsProvider::new);
        packGenerator.createProvider(ModItemTagsProvider::new);
        packGenerator.createProvider(ModModelProvider::new);
        packGenerator.createProvider(ModParticleDescriptionProvider::new);
        packGenerator.createProvider(ModDataMapProvider::new);
    }
}
