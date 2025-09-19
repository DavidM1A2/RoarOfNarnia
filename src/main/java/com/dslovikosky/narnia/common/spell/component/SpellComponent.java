package com.dslovikosky.narnia.common.spell.component;

import com.dslovikosky.narnia.common.spell.component.property.SpellComponentProperty;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class SpellComponent<T extends SpellComponent<T>> extends SpellComponentBase {
    private final List<SpellComponentProperty<?>> editableProperties = new ArrayList<>();

    public SpellComponent(final ResourceLocation id, final ResourceLocation icon) {
        super(id, icon);
    }

    public String getUnlocalizedPropertyBaseName(final String propertyName) {
        return getUnlocalizedBaseName() + "." + propertyName;
    }

    public void addEditableProperty(final SpellComponentProperty<?> property) {
        this.editableProperties.add(property);
    }

    public List<SpellComponentProperty<?>> getEditableProperties() {
        return editableProperties;
    }
}
