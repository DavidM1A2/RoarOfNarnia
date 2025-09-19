package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class SelfSpellDeliveryMethod extends SpellDeliveryMethod {
    public SelfSpellDeliveryMethod() {
        super(Constants.modLocation("self"));
    }

    @Override
    public void execute(DeliveryTransitionState state) {
        // Self just procs the effects and transitions at the target entity
        final Entity entity = state.getEntity();
        final Vec3 position = state.getPosition();
        if (entity != null) {
            this.procEffects(state);
            this.transitionFrom(state);
        } else {
        }
    }

    @Override
    public double getDeliveryCost(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 0.1;
    }

    @Override
    public double getMultiplicity(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return 1.0;
    }
}
