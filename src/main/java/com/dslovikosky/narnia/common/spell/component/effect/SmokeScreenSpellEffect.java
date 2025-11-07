package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.world.phys.Vec3;

public class SmokeScreenSpellEffect extends SpellEffect {
    public SmokeScreenSpellEffect() {
        super(Constants.modLocation("smoke_screen"));
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Vec3 position = state.getPosition();

        // Create smoke particle
        state.getLevel().sendParticles(ModParticleTypes.SMOKE_SCREEN.get(),
                position.x() + RANDOM.nextDouble() - 0.5,
                position.y() + RANDOM.nextDouble() - 0.5,
                position.z() + RANDOM.nextDouble() - 0.5,
                0, 0.0, 0.0, 0.0, 0.0);

        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return 0.2;
    }
}
