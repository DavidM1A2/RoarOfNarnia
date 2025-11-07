package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModDamageSources;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DisintegrateSpellEffect extends SpellEffect {
    private static final String NBT_STRENGTH = "strength";

    public DisintegrateSpellEffect() {
        super(Constants.modLocation("disintegrate"));
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Vec3 exactPosition = state.getPosition();
        final ServerLevel world = state.getLevel();
        final float strength = getStrength(instance);
        final Entity entityHit = state.getEntity();
        if (entityHit != null) {
            final float entityWidth = entityHit.getBbWidth();
            final float entityHeight = entityHit.getBbHeight();
            final double sinOffset = Math.PI * 2 * RANDOM.nextDouble();
            entityHit.hurt(ModDamageSources.spell(state), strength);
            for (int i = 0; i < 4; i++) {
                world.sendParticles(ModParticleTypes.DISINTEGRATE.get(),
                        exactPosition.x() + Math.sin(sinOffset + i * Math.PI / 2) * entityWidth / 2 * 1.5,
                        exactPosition.y() + entityHeight / 2.0,
                        exactPosition.z() + Math.cos(sinOffset + i * Math.PI / 2) * entityWidth / 2 * 1.5,
                        0, 0, 0, 0, 0);
            }
        } else if (canBlockBeDestroyed(world, state.getBlockPosition())) {
            world.setBlockAndUpdate(state.getBlockPosition(), Blocks.AIR.defaultBlockState());
            world.sendParticles(ModParticleTypes.DISINTEGRATE.get(), exactPosition.x(), exactPosition.y(), exactPosition.z(), 0, 0, 0, 0, 0);
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return getStrength(instance) * 2.0 - 1;
    }

    private boolean canBlockBeDestroyed(final Level level, final BlockPos blockPos) {
        final BlockState blockState = level.getBlockState(blockPos);
        return blockState.getDestroySpeed(level, blockPos) != -1f && !blockState.isAir();
    }

    public void setStrength(final SpellComponentInstance<?> instance, final float amount) {
        instance.getData().putFloat(NBT_STRENGTH, amount);
    }

    public float getStrength(final SpellComponentInstance<?> instance) {
        return instance.getData().getFloatOr(NBT_STRENGTH, 0f);
    }
}
