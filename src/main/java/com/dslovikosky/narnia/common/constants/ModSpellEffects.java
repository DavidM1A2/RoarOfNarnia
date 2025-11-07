package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.spell.component.effect.BurnSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.CharmSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.CleanseSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.DigSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.DisintegrateSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.ExplosionSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.ExtinguishSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.FeedSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSpellEffects {
    public static final DeferredRegister<SpellEffect> SPELL_EFFECTS = DeferredRegister.create(ModRegistries.SPELL_EFFECTS_KEY, Constants.MOD_ID);

    public static final DeferredHolder<SpellEffect, BurnSpellEffect> BURN = SPELL_EFFECTS.register("burn", BurnSpellEffect::new);
    public static final DeferredHolder<SpellEffect, CharmSpellEffect> CHARM = SPELL_EFFECTS.register("charm", CharmSpellEffect::new);
    public static final DeferredHolder<SpellEffect, CleanseSpellEffect> CLEANSE = SPELL_EFFECTS.register("cleanse", CleanseSpellEffect::new);
    public static final DeferredHolder<SpellEffect, DigSpellEffect> DIG = SPELL_EFFECTS.register("dig", DigSpellEffect::new);
    public static final DeferredHolder<SpellEffect, DisintegrateSpellEffect> DISINTEGRATE = SPELL_EFFECTS.register("disintegrate", DisintegrateSpellEffect::new);
    public static final DeferredHolder<SpellEffect, ExplosionSpellEffect> EXPLOSION = SPELL_EFFECTS.register("explosion", ExplosionSpellEffect::new);
    public static final DeferredHolder<SpellEffect, ExtinguishSpellEffect> EXTINGUISH = SPELL_EFFECTS.register("extinguish", ExtinguishSpellEffect::new);
    public static final DeferredHolder<SpellEffect, FeedSpellEffect> FEED = SPELL_EFFECTS.register("feed", FeedSpellEffect::new);
}
