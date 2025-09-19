package com.dslovikosky.narnia.common.spell.component.property;

import java.awt.Color;

public class ColorSpellComponentPropertyBuilder extends SpellComponentPropertyBuilder<Color, ColorSpellComponentPropertyBuilder> {
    @Override
    public SpellComponentProperty<Color> build() {
        return new ColorSpellComponentProperty(getBaseName(), getSetter(), getGetter(), getDefaultValue());
    }
}
