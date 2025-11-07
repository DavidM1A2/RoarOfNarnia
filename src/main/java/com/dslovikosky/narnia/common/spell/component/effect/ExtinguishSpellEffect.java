package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.phys.Vec3;

public class ExtinguishSpellEffect extends SpellEffect {
    public ExtinguishSpellEffect() {
        super(Constants.modLocation("extinguish"));
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        // If we hit an entity extinguish them
        final Entity entity = state.getEntity();
        if (entity != null) {
            if (entity.isOnFire()) {
                final double width = entity.getBbWidth();
                final double height = entity.getBbHeight();
                entity.clearFire();
                state.getLevel().sendParticles(
                        ParticleTypes.LARGE_SMOKE,
                        entity.getX(),
                        entity.getY() + height / 2,
                        entity.getZ(),
                        // Spawn 6 particles
                        6,
                        // Velocity is used as an offset for the particle
                        width / 2,
                        height / 2,
                        width / 2,
                        0.0
                );
            } else {
                return ProcResult.failure();
            }
        } else {
            final ServerLevel level = state.getLevel();
            final Vec3 reverseHitDir = state.getDirection().reverse();
            final BlockPos position = BlockPos.containing(state.getPosition().add(reverseHitDir.scale(0.01)));
            if (level.getBlockState(position).getBlock() instanceof FireBlock) {
                level.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState());
                level.sendParticles(
                        ParticleTypes.LARGE_SMOKE,
                        // Randomize the position within the block
                        position.getX() + 0.5,
                        position.getY() + 0.5,
                        position.getZ() + 0.5,
                        // Spawn two particles
                        2,
                        // Velocity is used as an offset for the particle
                        0.5,
                        0.5,
                        0.5,
                        0.0
                );
            } else {
                return ProcResult.failure();
            }
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return 0.2;
    }
}
