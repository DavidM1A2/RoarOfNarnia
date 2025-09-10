package com.dslovikosky.narnia.common.mob_effect;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DrowsyMobEffect extends MobEffect {
    private static final int COLOR = ARGB.color(0, 2, 131, 255);

    public DrowsyMobEffect() {
        super(MobEffectCategory.NEUTRAL, COLOR, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, COLOR));
    }
}
