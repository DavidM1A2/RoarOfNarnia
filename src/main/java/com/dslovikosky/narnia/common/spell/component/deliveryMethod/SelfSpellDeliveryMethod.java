package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.particle.SelfParticleData;
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

            final int numParticles = 10;
            final Vec3 particlePosition = entity.getPosition(1f).add(0.0, entity.getBbHeight() / 2.0, 0.0);
            for (int i = 0; i < numParticles; i++) {
                state.getLevel().sendParticles(new SelfParticleData(entity.getId(), (float) i / numParticles * 360),
                        particlePosition.x(), particlePosition.y(), particlePosition.z(), 1, 0.0, 0.0, 0.0, 0.0);
            }
        } else {
            state.getLevel().sendParticles(ModParticleTypes.SELF_FIZZLE.get(),
                    position.x(), position.y(), position.z(), 1, 0.0, 0.0, 0.0, 0.0);
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
