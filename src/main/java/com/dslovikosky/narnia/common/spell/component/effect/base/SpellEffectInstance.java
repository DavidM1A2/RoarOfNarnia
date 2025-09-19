package com.dslovikosky.narnia.common.spell.component.effect.base;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class SpellEffectInstance extends SpellComponentInstance<SpellEffect> {
    public SpellEffectInstance(final SpellEffect component) {
        super(component);
    }

    public SpellEffectInstance(final SpellEffect component, final CompoundTag nbt) {
        super(component, nbt);
    }

    public static SpellComponentInstance<SpellEffect> createFromNbt(final CompoundTag nbt) {
        final String effectTypeId = nbt.getString(NBT_TYPE_ID).get();
        return new SpellEffectInstance(ModRegistries.SPELL_EFFECTS.getValue(ResourceLocation.parse(effectTypeId)), nbt);
    }
}
