package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

class EnumSpellComponentProperty<T extends Enum<T>> extends SpellComponentProperty<T> {
    private final T[] values;

    protected EnumSpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, T> setter,
            final Function<SpellComponentInstance<?>, T> getter,
            final T defaultValue,
            final T[] values) {
        super(baseName, setter, getter, defaultValue);
        this.values = values;
    }

    @Override
    protected T convertTo(String newValue) {
        return Arrays.stream(values)
                .filter(v -> v.toString().equals(newValue))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException(Component.translatable("property_error.narnia.enum.format", newValue)));
    }
}
