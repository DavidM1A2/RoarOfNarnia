package com.dslovikosky.narnia.common.spell.component.property;

public class IntSpellComponentPropertyBuilder extends BoundedSpellComponentPropertyBuilder<Integer, IntSpellComponentPropertyBuilder> {
    @Override
    public SpellComponentProperty<Integer> build() {
        return new IntSpellComponentProperty(getBaseName(), getSetter(), getGetter(), getDefaultValue(), getMinValue(), getMaxValue());
    }
}
