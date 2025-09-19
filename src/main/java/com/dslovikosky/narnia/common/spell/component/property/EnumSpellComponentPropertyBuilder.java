package com.dslovikosky.narnia.common.spell.component.property;

public class EnumSpellComponentPropertyBuilder<T extends Enum<T>> extends SpellComponentPropertyBuilder<T, EnumSpellComponentPropertyBuilder<T>> {
    private final T[] values;

    EnumSpellComponentPropertyBuilder(final T[] values) {
        this.values = values;
    }

    @Override
    public SpellComponentProperty<T> build() {
        return new EnumSpellComponentProperty<>(getBaseName(), getSetter(), getGetter(), getDefaultValue(), values);
    }
}
