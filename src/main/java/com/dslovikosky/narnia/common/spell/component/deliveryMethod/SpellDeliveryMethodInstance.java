package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.ModRegistries;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class SpellDeliveryMethodInstance extends SpellComponentInstance<SpellDeliveryMethod> {
    public SpellDeliveryMethodInstance(final SpellDeliveryMethod component) {
        super(component);
    }

    public SpellDeliveryMethodInstance(final SpellDeliveryMethod component, final CompoundTag nbt) {
        super(component, nbt);
    }

    public static SpellComponentInstance<SpellDeliveryMethod> createFromNbt(final CompoundTag nbt) {
        final String deliveryMethodId = nbt.getString(NBT_TYPE_ID).get();
        return new SpellDeliveryMethodInstance(ModRegistries.SPELL_DELIVERY_METHODS.getValue(ResourceLocation.parse(deliveryMethodId)), nbt);
    }
}
