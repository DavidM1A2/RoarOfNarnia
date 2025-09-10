package com.dslovikosky.narnia.common.mob_effect;

import com.dslovikosky.narnia.common.constants.ModDimensions;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DrowsyMobEffect extends MobEffect {
    private static final int COLOR = ARGB.color(0, 2, 131, 255);

    public DrowsyMobEffect() {
        super(MobEffectCategory.NEUTRAL, COLOR, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, COLOR));
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (level.dimension() != ModDimensions.WOOD_BETWEEN_THE_WORLDS) {
            return false;
        }

        return super.applyEffectTick(level, entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
