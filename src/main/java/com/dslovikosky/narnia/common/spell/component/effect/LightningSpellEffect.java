package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.Vec3;

public class LightningSpellEffect extends SpellEffect {
    public LightningSpellEffect() {
        super(Constants.modLocation("lightning"));
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Vec3 position = state.getPosition();
        final LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, state.getLevel());
        lightningBolt.setPos(position.x(), position.y(), position.z());
        lightningBolt.lookAt(EntityAnchorArgument.Anchor.FEET, state.getDirection());
        state.getLevel().addFreshEntity(lightningBolt);
        for (int i = 0; i < 12; i++) {
            state.getLevel().sendParticles(ModParticleTypes.LIGHTNING.get(), position.x(), position.y(), position.z(),
                    0, 0.0, 0.0, 0.0, 0);
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return 7.0;
    }
}
