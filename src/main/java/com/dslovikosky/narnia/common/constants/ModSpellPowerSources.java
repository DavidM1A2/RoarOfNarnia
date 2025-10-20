package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.spell.component.powerSource.AlchemySpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.CreativeSpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.HealthSpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.InnateSpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.LeechSpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.LunarSpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.SolarSpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.SpellScrollSpellPowerSource;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSpellPowerSources {
    public static final DeferredRegister<SpellPowerSource<?>> SPELL_POWER_SOURCES = DeferredRegister.create(ModRegistries.SPELL_POWER_SOURCES, Constants.MOD_ID);

    public static final DeferredHolder<SpellPowerSource<?>, CreativeSpellPowerSource> CREATIVE = SPELL_POWER_SOURCES.register("creative", CreativeSpellPowerSource::new);
    public static final DeferredHolder<SpellPowerSource<?>, AlchemySpellPowerSource> ALCHEMY = SPELL_POWER_SOURCES.register("alchemy", AlchemySpellPowerSource::new);
    public static final DeferredHolder<SpellPowerSource<?>, HealthSpellPowerSource> HEALTH = SPELL_POWER_SOURCES.register("health", HealthSpellPowerSource::new);
    public static final DeferredHolder<SpellPowerSource<?>, InnateSpellPowerSource> INNATE = SPELL_POWER_SOURCES.register("innate", InnateSpellPowerSource::new);
    public static final DeferredHolder<SpellPowerSource<?>, LeechSpellPowerSource> LEECH = SPELL_POWER_SOURCES.register("leech", LeechSpellPowerSource::new);
    public static final DeferredHolder<SpellPowerSource<?>, LunarSpellPowerSource> LUNAR = SPELL_POWER_SOURCES.register("lunar", LunarSpellPowerSource::new);
    public static final DeferredHolder<SpellPowerSource<?>, SolarSpellPowerSource> SOLAR = SPELL_POWER_SOURCES.register("solar", SolarSpellPowerSource::new);
    public static final DeferredHolder<SpellPowerSource<?>, SpellScrollSpellPowerSource> SPELL_SCROLL = SPELL_POWER_SOURCES.register("spell_scroll", SpellScrollSpellPowerSource::new);
}
