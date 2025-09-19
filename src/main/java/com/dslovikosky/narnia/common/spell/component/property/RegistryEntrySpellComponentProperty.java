package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class RegistryEntrySpellComponentProperty<T> extends SpellComponentProperty<T> {
    private final Registry<T> registry;
    private final Function<T, Boolean> filter;

    protected RegistryEntrySpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, T> setter,
            final Function<SpellComponentInstance<?>, T> getter,
            final T defaultValue,
            final Registry<T> registry,
            final Function<T, Boolean> filter) {
        super(baseName, setter, getter, defaultValue);
        this.registry = registry;
        this.filter = filter;
    }

    @Override
    protected T convertTo(String newValue) {
        final ResourceLocation key = ResourceLocation.tryParse(newValue);

        if (key == null) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.registry_entry.invalid_resource_location", newValue));
        }

        final T value = registry.getValue(key);

        if (value == null) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.registry_entry.missing_entry", newValue));
        }

        if (filter == null || filter.apply(value)) {
            return value;
        } else {
            throw new InvalidValueException(Component.translatable("property_error.narnia.registry_entry.disallowed_entry", newValue));
        }
    }

    @Override
    public String convertFrom(T value) {
        return registry.getKey(value).toString();
    }
}
