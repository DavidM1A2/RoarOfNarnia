package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.model.damagesource.SpellDamageSource;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import net.minecraft.world.damagesource.DamageSource;

public class ModDamageSources {
    public static DamageSource spell(final DeliveryTransitionState deliveryTransitionState) {
        return new SpellDamageSource(deliveryTransitionState);
    }
}
