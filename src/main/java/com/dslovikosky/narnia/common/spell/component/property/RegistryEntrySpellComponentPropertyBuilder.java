package com.dslovikosky.narnia.common.spell.component.property;

import net.minecraft.core.Registry;

import java.util.function.Function;

public class RegistryEntrySpellComponentPropertyBuilder<T> extends SpellComponentPropertyBuilder<T, RegistryEntrySpellComponentPropertyBuilder<T>> {
    private Registry<T> registry;
    private Function<T, Boolean> filter;

    public RegistryEntrySpellComponentPropertyBuilder<T> withRegistry(Registry<T> registry) {
        this.registry = registry;
        return this;
    }

    public RegistryEntrySpellComponentPropertyBuilder<T> withFilter(Function<T, Boolean> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public SpellComponentProperty<T> build() {
        return new RegistryEntrySpellComponentProperty<>(getBaseName(), getSetter(), getGetter(), getDefaultValue(), registry, filter);
    }
}
