package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ModSoundDefinitionsProvider(final PackOutput output, final ExistingFileHelper helper) {
        super(output, Constants.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.PAGE_TURN.get(), SoundDefinition.definition()
                .subtitle("sound.narnia.ui.page_turn")
                .replace(false)
                .with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "ui/page_turn"), SoundDefinition.SoundType.SOUND).stream(false)));
    }
}
