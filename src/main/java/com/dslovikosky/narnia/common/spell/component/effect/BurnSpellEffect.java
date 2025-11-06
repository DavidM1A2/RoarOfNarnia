package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.DurationSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class BurnSpellEffect extends DurationSpellEffect {
    public BurnSpellEffect() {
        super(Constants.modLocation("burn"), 1.0, 1.0, 60.0);
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Entity entity = state.getEntity();
        if (entity != null) {
            for (int i = 0; i < 8; i++) {
                final Vec3 position = entity.position().add(
                        (RANDOM.nextDouble() - 0.5) * entity.getBoundingBox().getXsize() * 1.5,
                        (RANDOM.nextDouble() - 0.5) * entity.getBoundingBox().getYsize() * 0.8,
                        (RANDOM.nextDouble() - 0.5) * entity.getBoundingBox().getZsize() * 1.5
                );
                state.getLevel().sendParticles(ModParticleTypes.FIRE.get(), position.x(), position.y(), position.z(), 0,
                        0.01 * (RANDOM.nextDouble() - 0.5), 0.1, 0.01 * (RANDOM.nextDouble() - 0.5), 1.0);
            }
            entity.setRemainingFireTicks((int) Math.max(entity.getRemainingFireTicks(), Math.ceil(getDuration(instance) * 20)));
        } else {
            final ServerLevel world = state.getLevel();
            final Vec3 reverseHitDir = state.getDirection().reverse();
            final Vec3 position = state.getPosition();
            final BlockPos blockPosition = BlockPos.containing(state.getPosition().add(reverseHitDir.scale(0.01)));
            boolean setBlockOnFire = false;
            if (world.isEmptyBlock(blockPosition)) {
                if (!world.isEmptyBlock(blockPosition.below())) {
                    state.getLevel().sendParticles(ModParticleTypes.FIRE.get(), position.x(), position.y(), position.z(), 0,
                            0.01 * (RANDOM.nextDouble() - 0.5), 0.1, 0.01 * (RANDOM.nextDouble() - 0.5), 1.0);
                    world.setBlockAndUpdate(blockPosition, Blocks.FIRE.defaultBlockState());
                    setBlockOnFire = true;
                }
            }
            if (!setBlockOnFire) {
                return ProcResult.failure();
            }
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        // If burning an entity, add 3.0 vitae per second
        // Burning a block only costs 1, first tick of entity burn is half off
        return getDuration(instance) * 2.0 - 1.0;
    }
}
