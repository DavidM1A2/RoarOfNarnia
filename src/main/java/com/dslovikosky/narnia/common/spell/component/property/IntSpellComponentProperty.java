package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class IntSpellComponentProperty extends SpellComponentProperty<Integer> {
    private final Integer minValue;
    private final Integer maxValue;

    protected IntSpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, Integer> setter,
            final Function<SpellComponentInstance<?>, Integer> getter,
            final Integer defaultValue,
            final Integer minValue,
            final Integer maxValue) {
        super(baseName, setter, getter, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected Integer convertTo(String newValue) {
        final int intValue;
        try {
            intValue = Integer.parseInt(newValue);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.integer.format", newValue));
        }

        if (minValue != null && intValue < minValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.integer.too_small", getName(), minValue));
        }
        if (maxValue != null && intValue > maxValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.integer.too_large", getName(), maxValue));
        }
        return intValue;
    }
}
