package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

class BooleanSpellComponentProperty extends SpellComponentProperty<Boolean> {
    protected BooleanSpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, Boolean> setter,
            final Function<SpellComponentInstance<?>, Boolean> getter,
            final Boolean defaultValue) {
        super(baseName, setter, getter, defaultValue);
    }

    @Override
    protected Boolean convertTo(String newValue) {
        if (newValue.equals("true")) {
            return true;
        }
        if (newValue.equals("false")) {
            return false;
        }
        throw new InvalidValueException(Component.translatable("property_error.narnia.boolean.format", newValue));
    }
}
