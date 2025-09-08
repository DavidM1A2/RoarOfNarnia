package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModSoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ModSoundDefinitionsProvider(final PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.PAGE_TURN.get(), SoundDefinition.definition()
                .subtitle("sound.narnia.ui.page_turn")
                .replace(false)
                .with(SoundDefinition.Sound.sound(Constants.modLocation("ui/page_turn"), SoundDefinition.SoundType.SOUND).stream(false)));
    }
}
