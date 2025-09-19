package com.dslovikosky.narnia.common.spell.component.property;

public class SpellComponentPropertyFactory {
    private SpellComponentPropertyFactory() {
    }

    public static IntSpellComponentPropertyBuilder intProperty() {
        return new IntSpellComponentPropertyBuilder();
    }

    public static LongSpellComponentPropertyBuilder longProperty() {
        return new LongSpellComponentPropertyBuilder();
    }

    public static DoubleSpellComponentPropertyBuilder doubleProperty() {
        return new DoubleSpellComponentPropertyBuilder();
    }

    public static FloatSpellComponentPropertyBuilder floatProperty() {
        return new FloatSpellComponentPropertyBuilder();
    }

    public static BooleanSpellComponentPropertyBuilder booleanProperty() {
        return new BooleanSpellComponentPropertyBuilder();
    }

    public static <T extends Enum<T>> EnumSpellComponentPropertyBuilder<T> enumProperty(final Enum<T> e) {
        return new EnumSpellComponentPropertyBuilder<>(e.getDeclaringClass().getEnumConstants());
    }

    public static ColorSpellComponentPropertyBuilder colorProperty() {
        return new ColorSpellComponentPropertyBuilder();
    }

    public static <T> RegistryEntrySpellComponentPropertyBuilder<T> registryEntryProperty() {
        return new RegistryEntrySpellComponentPropertyBuilder<>();
    }
}
