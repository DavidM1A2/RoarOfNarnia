package com.dslovikosky.narnia.common.spell.component.property;

public class FloatSpellComponentPropertyBuilder extends BoundedSpellComponentPropertyBuilder<Float, FloatSpellComponentPropertyBuilder> {
    @Override
    public SpellComponentProperty<Float> build() {
        return new FloatSpellComponentProperty(getBaseName(), getSetter(), getGetter(), getDefaultValue(), getMinValue(), getMaxValue());
    }
}
