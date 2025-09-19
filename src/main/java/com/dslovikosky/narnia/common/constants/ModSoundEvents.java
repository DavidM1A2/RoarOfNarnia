package com.dslovikosky.narnia.common.constants;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Constants.MOD_ID);

    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> PAGE_TURN = SOUND_EVENTS.register("page_turn", SoundEvent::createVariableRangeEvent);

    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> WOOD_BETWEEN_THE_WORLDS = SOUND_EVENTS.register("wood_between_the_worlds", SoundEvent::createVariableRangeEvent);

    public static final DeferredHolder<SoundEvent, ? extends SoundEvent> SPELL_CAST = SOUND_EVENTS.register("spell_cast", SoundEvent::createVariableRangeEvent);
}
