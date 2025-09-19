package com.dslovikosky.narnia.common.spell.component.property;

public class DoubleSpellComponentPropertyBuilder extends BoundedSpellComponentPropertyBuilder<Double, DoubleSpellComponentPropertyBuilder> {
    @Override
    public SpellComponentProperty<Double> build() {
        return new DoubleSpellComponentProperty(getBaseName(), getSetter(), getGetter(), getDefaultValue(), getMinValue(), getMaxValue());
    }
}
