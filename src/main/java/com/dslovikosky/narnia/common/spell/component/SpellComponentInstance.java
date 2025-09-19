package com.dslovikosky.narnia.common.spell.component;

import net.minecraft.nbt.CompoundTag;

public abstract class SpellComponentInstance<T extends SpellComponent<T>> {
    protected static final String NBT_TYPE_ID = "id";
    protected static final String NBT_EXTRA_DATA = "extra_data";

    private final T component;
    private final CompoundTag data;

    public SpellComponentInstance(final T component) {
        this.component = component;
        this.data = new CompoundTag();
    }

    public SpellComponentInstance(final T component, final CompoundTag nbt) {
        this.component = component;
        this.data = nbt.getCompound(NBT_EXTRA_DATA).get();
    }

    public void setDefaults() {
        component.getEditableProperties().forEach(it -> it.setDefaultValue(this));
    }

    public CompoundTag serializeNbt() {
        final CompoundTag nbt = new CompoundTag();

        nbt.putString(NBT_TYPE_ID, component.getId().toString());
        nbt.put(NBT_EXTRA_DATA, data);

        return nbt;
    }

    public T getComponent() {
        return component;
    }

    public CompoundTag getData() {
        return data;
    }
}
