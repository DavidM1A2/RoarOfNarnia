package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.spell.component.effect.DigSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSpellEffects {
    public static final DeferredRegister<SpellEffect> SPELL_EFFECTS = DeferredRegister.create(ModRegistries.SPELL_EFFECTS_KEY, Constants.MOD_ID);

    public static final DeferredHolder<SpellEffect, DigSpellEffect> DIG = SPELL_EFFECTS.register("dig", DigSpellEffect::new);
}
