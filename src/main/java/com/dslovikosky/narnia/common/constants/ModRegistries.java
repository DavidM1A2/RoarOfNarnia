package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.schematic.Schematic;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;

public class ModRegistries {
    public static final ResourceKey<Registry<Schematic>> SCHEMATIC_KEY = ResourceKey.createRegistryKey(Constants.modLocation("schematic"));
    public static final ResourceKey<Registry<SpellPowerSource<?>>> SPELL_POWER_SOURCES_KEY = ResourceKey.createRegistryKey(Constants.modLocation("spell_power_sources"));
    public static final ResourceKey<Registry<SpellDeliveryMethod>> SPELL_DELIVERY_METHODS_KEY = ResourceKey.createRegistryKey(Constants.modLocation("spell_delivery_methods"));
    public static final ResourceKey<Registry<SpellEffect>> SPELL_EFFECTS_KEY = ResourceKey.createRegistryKey(Constants.modLocation("spell_effects"));

    public static final Registry<Schematic> SCHEMATIC = new RegistryBuilder<>(SCHEMATIC_KEY).sync(false).maxId(256).create();
    public static final Registry<SpellPowerSource<?>> SPELL_POWER_SOURCES = new RegistryBuilder<>(SPELL_POWER_SOURCES_KEY).sync(true).maxId(256).create();
    public static final Registry<SpellDeliveryMethod> SPELL_DELIVERY_METHODS = new RegistryBuilder<>(SPELL_DELIVERY_METHODS_KEY).sync(true).maxId(256).create();
    public static final Registry<SpellEffect> SPELL_EFFECTS = new RegistryBuilder<>(SPELL_EFFECTS_KEY).sync(true).maxId(256).create();

    public static final List<Registry<?>> REGISTRIES = List.of(SCHEMATIC, SPELL_POWER_SOURCES, SPELL_DELIVERY_METHODS, SPELL_EFFECTS);
}
