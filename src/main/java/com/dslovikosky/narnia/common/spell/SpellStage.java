package com.dslovikosky.narnia.common.spell;

import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.SpellDeliveryMethodInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffectInstance;
import net.minecraft.nbt.CompoundTag;

public class SpellStage {
    // NBT Tag constants
    private static final String NBT_DELIVERY_METHOD = "delivery_method";
    private static final String NBT_EFFECT_BASE = "effect_";

    private static final int MAX_EFFECTS_PER_STAGE = 4;

    private final SpellComponentInstance<SpellEffect>[] effectInstances = new SpellComponentInstance[MAX_EFFECTS_PER_STAGE];
    private SpellComponentInstance<SpellDeliveryMethod> deliveryInstance = null;

    public SpellStage() {
    }

    public SpellStage(final CompoundTag nbt) {
        // The spell stage delivery method can be null, double check that it exists before reading it and its state
        if (nbt.contains(NBT_DELIVERY_METHOD)) {
            deliveryInstance = SpellDeliveryMethodInstance.createFromNbt(nbt.getCompound(NBT_DELIVERY_METHOD).get());
        }

        // Go over each spell effect
        for (int i = 0; i < effectInstances.length; i++) {
            // The spell stage effects can be null, so we need to skip null effects
            if (nbt.contains(NBT_EFFECT_BASE + i)) {
                effectInstances[i] = SpellEffectInstance.createFromNbt(nbt.getCompound(NBT_EFFECT_BASE + i).get());
            }
        }
    }

    public double getCost() {
        if (!isValid()) {
            return 0;
        }

        final SpellDeliveryMethod deliveryMethod = deliveryInstance.getComponent();
        double cost = deliveryMethod.getDeliveryCost(deliveryInstance);
        for (final SpellComponentInstance<SpellEffect> effectInstance : effectInstances) {
            if (effectInstance != null) {
                cost = cost + deliveryMethod.getMultiplicity(deliveryInstance) * effectInstance.getComponent().getCost(effectInstance);
            }
        }
        return cost;
    }

    public boolean isValid() {
        return this.deliveryInstance != null;
    }

    public CompoundTag serializeNbt() {
        final CompoundTag nbt = new CompoundTag();

        // The spell stage delivery method can be null, double check that it isn't before writing it and its state
        if (deliveryInstance != null) {
            nbt.put(NBT_DELIVERY_METHOD, deliveryInstance.serializeNbt());
        }

        // The spell stage effects can be null, so we need to skip null effects
        for (int i = 0; i < effectInstances.length; i++) {
            if (effectInstances[i] != null) {
                nbt.put(NBT_EFFECT_BASE + i, effectInstances[i].serializeNbt());
            }
        }

        return nbt;
    }

    public SpellComponentInstance<SpellDeliveryMethod> getDeliveryInstance() {
        return deliveryInstance;
    }

    public void setDeliveryInstance(SpellComponentInstance<SpellDeliveryMethod> deliveryInstance) {
        this.deliveryInstance = deliveryInstance;
    }

    public SpellComponentInstance<SpellEffect>[] getEffects() {
        return effectInstances;
    }
}
