package com.dslovikosky.narnia.common.model.damagesource;

import com.dslovikosky.narnia.common.constants.ModDamageTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpellDamageSource extends DamageSource {
    private final DeliveryTransitionState deliveryTransitionState;

    public SpellDamageSource(final DeliveryTransitionState deliveryTransitionState) {
        super(deliveryTransitionState.getLevel().holderOrThrow(ModDamageTypes.SPELL),
                deliveryTransitionState.getEntity(),
                deliveryTransitionState.getCasterEntity(),
                deliveryTransitionState.getPosition());
        this.deliveryTransitionState = deliveryTransitionState;
    }

    @Override
    public Component getLocalizedDeathMessage(final LivingEntity killedEntity) {
        final String baseText = "death.attack." + getMsgId();
        final Entity spellCaster = deliveryTransitionState.getCasterEntity();
        if (killedEntity == spellCaster) {
            return Component.translatable(baseText + ".suicide", killedEntity.getDisplayName());
        } else if (spellCaster != null) {
            return Component.translatable(baseText + ".player", killedEntity.getDisplayName(), spellCaster.getDisplayName(), deliveryTransitionState.getSpell().getName());
        } else {
            return Component.translatable(baseText, killedEntity.getDisplayName(), deliveryTransitionState.getSpell().getName());
        }
    }
}
