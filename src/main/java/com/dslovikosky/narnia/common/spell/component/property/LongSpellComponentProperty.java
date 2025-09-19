package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class LongSpellComponentProperty extends SpellComponentProperty<Long> {
    private final Long minValue;
    private final Long maxValue;

    protected LongSpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, Long> setter,
            final Function<SpellComponentInstance<?>, Long> getter,
            final Long defaultValue,
            final Long minValue,
            final Long maxValue) {
        super(baseName, setter, getter, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected Long convertTo(String newValue) {
        final long longValue;
        try {
            longValue = Long.parseLong(newValue);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.long.format", newValue));
        }

        if (minValue != null && longValue < minValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.long.too_small", getName(), minValue));
        }
        if (maxValue != null && longValue > maxValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.long.too_large", getName(), maxValue));
        }
        return longValue;
    }
}
