package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.InvalidValueException;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class SpellComponentProperty<T> {
    private final String baseName;
    private final BiConsumer<SpellComponentInstance<?>, T> setter;
    private final Function<SpellComponentInstance<?>, T> getter;
    private final T defaultValue;

    protected SpellComponentProperty(
            final String baseName,
            final BiConsumer<SpellComponentInstance<?>, T> setter,
            final Function<SpellComponentInstance<?>, T> getter,
            final T defaultValue) {
        this.baseName = baseName;
        this.setter = setter;
        this.getter = getter;
        this.defaultValue = defaultValue;
    }

    protected abstract T convertTo(final String newValue);

    protected String convertFrom(final T value) {
        return value.toString();
    }

    public void setValue(final SpellComponentInstance<?> instance, final String newValue) {
        try {
            setter.accept(instance, convertTo(newValue));
        } catch (final InvalidValueException e) {
            setDefaultValue(instance);
            throw e;
        }
    }

    public void setDefaultValue(final SpellComponentInstance<?> instance) {
        setter.accept(instance, defaultValue);
    }

    public String getValue(final SpellComponentInstance<?> instance) {
        T value;
        try {
            value = getter.apply(instance);
        } catch (final Exception e) {
            value = defaultValue;
        }
        return convertFrom(value);
    }

    public Component getName() {
        return Component.translatable(baseName + ".name");
    }

    public Component getDescription() {
        return Component.translatable(baseName + ".description");
    }
}
