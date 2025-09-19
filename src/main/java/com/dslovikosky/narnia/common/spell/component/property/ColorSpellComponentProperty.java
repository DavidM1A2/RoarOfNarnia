package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.Color;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ColorSpellComponentProperty extends SpellComponentProperty<Color> {
    protected ColorSpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, Color> setter,
            final Function<SpellComponentInstance<?>, Color> getter,
            final Color defaultValue) {
        super(baseName, setter, getter, defaultValue);
    }

    @Override
    protected Color convertTo(String newValue) {
        final String[] rgbStrings = newValue.split("\\s+");
        if (rgbStrings.length != 3) {
            throw new InvalidValueException(Component.translatable("property_error.narnia.color.format"));
        }
        final int[] rgbs = new int[3];
        for (int i = 0; i < rgbStrings.length; i++) {
            String rgbString = rgbStrings[i];
            if (!NumberUtils.isDigits(rgbString)) {
                throw new InvalidValueException(Component.translatable("property_error.narnia.color.value_type"));
            }
            final int rgb = NumberUtils.createInteger(rgbString);
            if (rgb < 0 || rgb > 255) {
                throw new InvalidValueException(Component.translatable("property_error.narnia.color.value_range"));
            }
            rgbs[i] = rgb;
        }
        return new Color(rgbs[0], rgbs[1], rgbs[2]);
    }

    @Override
    public String convertFrom(Color value) {
        return value.getRed() + " " + value.getGreen() + " " + value.getBlue();
    }
}
