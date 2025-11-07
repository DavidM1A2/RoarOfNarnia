package com.dslovikosky.narnia.common.spell.component.effect.base;

import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.resources.ResourceLocation;

public abstract class DurationSpellEffect extends SpellEffect {
    private static final String NBT_DURATION = "duration";

    public DurationSpellEffect(final ResourceLocation id, final Double minDuration, final Double defaultDuration, final Double maxDuration) {
        super(id);
        addEditableProperty(SpellComponentPropertyFactory.doubleProperty()
                .withBaseName(getUnlocalizedBaseName())
                .withSetter(this::setDuration)
                .withGetter(this::getDuration)
                .withMinValue(minDuration)
                .withDefaultValue(defaultDuration)
                .withMaxValue(maxDuration)
                .build());
    }

    public void setDuration(final SpellComponentInstance<?> instance, final double duration) {
        instance.getData().putDouble(NBT_DURATION, duration);
    }

    public double getDuration(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_DURATION, 0.0);
    }
}
