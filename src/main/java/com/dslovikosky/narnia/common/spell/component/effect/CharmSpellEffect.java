package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.model.attachment_type.SpellCharmData;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.DurationSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public class CharmSpellEffect extends DurationSpellEffect {
    public CharmSpellEffect() {
        super(Constants.modLocation("charm"), 1.0, 1.0, 60.0);
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Entity entity = state.getEntity();
        final Entity spellOwner = state.getCasterEntity();
        // If we hit an entity that is an animal set them in love
        if (entity instanceof Animal animal) {
            animal.setInLove(spellOwner instanceof Player player ? player : null);
        } else if (entity instanceof Player && spellOwner != null) {
            // Set the player's charm data
            entity.setData(ModAttachmentTypes.SPELL_CHARM_DATA, new SpellCharmData((int) Math.ceil(getDuration(instance) * 20), spellOwner.getUUID()));

            final double width = entity.getBbWidth();
            final double height = entity.getBbHeight();

            // Spawn 4 random heart particles
            for (int i = 0; i < 4; i++) {
                state.getLevel().sendParticles(
                        ParticleTypes.HEART,
                        // The position will be somewhere inside the player's hitbox
                        state.getPosition().x() + RANDOM.nextFloat() * width * 2.0f - width,
                        state.getPosition().y() + RANDOM.nextFloat() * height,
                        state.getPosition().z() + RANDOM.nextFloat() * width * 2.0f - width,
                        // Spawn one particle
                        1,
                        // Randomize velocity
                        RANDOM.nextGaussian() * 0.02,
                        RANDOM.nextGaussian() * 0.02,
                        RANDOM.nextGaussian() * 0.02,
                        0.02);
            }
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        // Charming an entity costs 3
        final double baseCost = 3;
        // Each second of duration costs 2.0, but the first 1 seconds are free
        final double durationCost = Math.max(0, (getDuration(instance) - 1) * 2.0);
        return baseCost + durationCost * durationCost;
    }
}
