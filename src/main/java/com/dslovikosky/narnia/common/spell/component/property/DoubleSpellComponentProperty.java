package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class DoubleSpellComponentProperty extends SpellComponentProperty<Double> {
    private final Double minValue;
    private final Double maxValue;

    protected DoubleSpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, Double> setter,
            final Function<SpellComponentInstance<?>, Double> getter,
            final Double defaultValue,
            final Double minValue,
            final Double maxValue) {
        super(baseName, setter, getter, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected Double convertTo(String newValue) {
        // Ensure the number is parsable
        final double doubleValue;
        try {
            doubleValue = Double.parseDouble(newValue);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.double.format", newValue));
        }

        // Ensure the double is valid
        if (minValue != null && doubleValue < minValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.double.too_small", getName(), minValue));
        }
        if (maxValue != null && doubleValue > maxValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.double.too_large", getName(), maxValue));
        }
        return doubleValue;
    }
}
