package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FloatSpellComponentProperty extends SpellComponentProperty<Float> {
    private final Float minValue;
    private final Float maxValue;

    protected FloatSpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, Float> setter,
            final Function<SpellComponentInstance<?>, Float> getter,
            final Float defaultValue,
            final Float minValue,
            final Float maxValue) {
        super(baseName, setter, getter, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected Float convertTo(String newValue) {
        final float floatValue;
        try {
            floatValue = Float.parseFloat(newValue);
        } catch (NumberFormatException e) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.float.format", newValue));
        }

        if (minValue != null && floatValue < minValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.float.too_small", getName(), minValue));
        }
        if (maxValue != null && floatValue > maxValue) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.float.too_large", getName(), maxValue));
        }
        return floatValue;
    }
}
