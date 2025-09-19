package com.dslovikosky.narnia.common.spell.component.property;

public class LongSpellComponentPropertyBuilder extends BoundedSpellComponentPropertyBuilder<Long, LongSpellComponentPropertyBuilder> {
    @Override
    public SpellComponentProperty<Long> build() {
        return new LongSpellComponentProperty(getBaseName(), getSetter(), getGetter(), getDefaultValue(), getMinValue(), getMaxValue());
    }
}
