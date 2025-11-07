package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class TeleportSpellEffect extends SpellEffect {
    public TeleportSpellEffect() {
        super(Constants.modLocation("teleport"));
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final ServerLevel world = state.getLevel();
        final Entity spellCaster = state.getCasterEntity();
        if (spellCaster != null) {
            final Vec3 position = state.getPosition();
            Vec3 spellCasterPosition = spellCaster.position();

            // Create particles at the pre- and post-teleport position
            for (int i = 0; i < 4; i++) {
                world.sendParticles(ModParticleTypes.ENDER.get(), spellCasterPosition.x(), spellCasterPosition.y(), spellCasterPosition.z(),
                        0, 0.0, 0.0, 0.0, 0.0);
            }
            // Play sound at the pre- and post-teleport position
            world.playSound(
                    null,
                    position.x,
                    position.y,
                    position.z,
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    2.5f,
                    1.0f
            );

            spellCaster.teleportTo(position.x, position.y, position.z);

            for (int i = 0; i < 4; i++) {
                world.sendParticles(ModParticleTypes.ENDER.get(), position.x(), position.y(), position.z(),
                        0, 0.0, 0.0, 0.0, 0.0);
            }
            world.playSound(
                    null,
                    position.x,
                    position.y,
                    position.z,
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    2.5f,
                    1.0f
            );
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return 30.0;
    }
}
