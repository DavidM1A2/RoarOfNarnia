package com.dslovikosky.narnia.common.spell.component.property;

import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;

import java.util.function.BiConsumer;
import java.util.function.Function;

abstract class SpellComponentPropertyBuilder<T, V extends SpellComponentPropertyBuilder<T, V>> {
    private String baseName;
    private BiConsumer<SpellComponentInstance<?>, T> setter;
    private Function<SpellComponentInstance<?>, T> getter;
    private T defaultValue;

    public V withBaseName(final String baseName) {
        this.baseName = baseName;
        return (V) this;
    }

    public V withSetter(final BiConsumer<SpellComponentInstance<?>, T> setter) {
        this.setter = setter;
        return (V) this;
    }

    public V withGetter(final Function<SpellComponentInstance<?>, T> getter) {
        this.getter = getter;
        return (V) this;
    }

    public V withDefaultValue(final T defaultValue) {
        this.defaultValue = defaultValue;
        return (V) this;
    }

    String getBaseName() {
        return baseName;
    }

    BiConsumer<SpellComponentInstance<?>, T> getSetter() {
        return setter;
    }

    Function<SpellComponentInstance<?>, T> getGetter() {
        return getter;
    }

    T getDefaultValue() {
        return defaultValue;
    }

    public abstract SpellComponentProperty<T> build();
}
