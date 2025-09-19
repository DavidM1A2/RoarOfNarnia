package com.dslovikosky.narnia.common.spell.component.property;

public class BooleanSpellComponentPropertyBuilder extends SpellComponentPropertyBuilder<Boolean, BooleanSpellComponentPropertyBuilder> {
    @Override
    public SpellComponentProperty<Boolean> build() {
        return new BooleanSpellComponentProperty(getBaseName(), getSetter(), getGetter(), getDefaultValue());
    }
}
