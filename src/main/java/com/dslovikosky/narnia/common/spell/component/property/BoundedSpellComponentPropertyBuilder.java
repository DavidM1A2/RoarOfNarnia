package com.dslovikosky.narnia.common.spell.component.property;

abstract class BoundedSpellComponentPropertyBuilder<T, V extends BoundedSpellComponentPropertyBuilder<T, V>> extends SpellComponentPropertyBuilder<T, V> {
    private T minValue;
    private T maxValue;

    public V withMinValue(final T minValue) {
        this.minValue = minValue;
        return (V) this;
    }

    public V withMaxValue(final T maxValue) {
        this.maxValue = maxValue;
        return (V) this;
    }

    T getMinValue() {
        return minValue;
    }

    T getMaxValue() {
        return maxValue;
    }
}
