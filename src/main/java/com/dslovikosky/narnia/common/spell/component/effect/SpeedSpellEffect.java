package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.DurationSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.time.Duration;

public class SpeedSpellEffect extends DurationSpellEffect {
    private static final String NBT_MULTIPLIER = "multiplier";

    public SpeedSpellEffect() {
        super(Constants.modLocation("speed"), 1.0, 10.0, (double) Duration.ofMinutes(20).getSeconds());
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("multiplier"))
                        .withSetter(this::setMultiplier)
                        .withGetter(this::getMultiplier)
                        .withDefaultValue(1)
                        .withMinValue(-6)
                        .withMaxValue(10)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Vec3 exactPosition = state.getPosition();
        final Entity entityHit = state.getEntity();

        final int multiplier = getMultiplier(instance);
        if (multiplier == 0) {
            return ProcResult.failure();
        }

        final int duration = (int) Math.ceil(getDuration(instance) * 20);
        final Holder<MobEffect> effectType = multiplier >= 0 ? MobEffects.SPEED : MobEffects.SLOWNESS;
        final int effectAmplifier = Math.abs(multiplier) - 1;
        final MobEffectInstance effect = new MobEffectInstance(effectType, duration, effectAmplifier);

        if (entityHit instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(effect);
            for (int i = 0; i < 5; i++) {
                Vec3 speed = Vec3.directionFromRotation(entityHit.getXRot(), entityHit.getYRot()).reverse();
                // Remove the y component
                speed = new Vec3(speed.x(), (RANDOM.nextDouble() * entityHit.getBbHeight() / 2 + entityHit.getBbHeight() / 2) * 0.25, speed.z());
                // Normalize the direction
                speed = speed.normalize();
                // Set the y component to upwards, perturb the x and z components
                speed = new Vec3(
                        speed.x() * 0.2 + (RANDOM.nextDouble() - 0.5) * 0.2,
                        (RANDOM.nextDouble() * entityHit.getBbHeight() / 2 + entityHit.getBbHeight() / 2) * 0.25,
                        speed.z() * 0.2 + (RANDOM.nextDouble() - 0.5) * 0.2
                );

                state.getLevel().sendParticles(ModParticleTypes.DUST_CLOUD.get(), exactPosition.x(), exactPosition.y(), exactPosition.z(),
                        0, speed.x(), speed.y(), speed.z(), 1.0);
            }
        } else {
            final ServerLevel world = state.getLevel();
            final AreaEffectCloud aoePotion = new AreaEffectCloud(world, exactPosition.x(), exactPosition.y(), exactPosition.z());
            aoePotion.addEffect(effect);
            aoePotion.setOwner((LivingEntity) state.getCasterEntity());
            aoePotion.setRadiusPerTick(0f);
            aoePotion.setDuration(duration);
            world.addFreshEntity(aoePotion);

            final Vec3 speed = new Vec3(RANDOM.nextDouble() - 0.5, 0.0, RANDOM.nextDouble() - 0.5)
                    .normalize()
                    .scale(0.2)
                    .add(0.0, RANDOM.nextDouble() * 0.3 + 0.3, 0.0);
            world.sendParticles(ModParticleTypes.DUST_CLOUD.get(), exactPosition.x(), exactPosition.y(), exactPosition.z(),
                    0, speed.x(), speed.y(), speed.z(), 1.0);
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        // Each second of speed costs 0.25
        final double durationCost = getDuration(instance) * 0.25;
        // Each level of speed costs 1.0 per second
        final double speedCostMultiplier = Math.abs(getMultiplier(instance)) * 1.0;
        return speedCostMultiplier * durationCost;
    }

    public void setMultiplier(final SpellComponentInstance<?> instance, final int amount) {
        instance.getData().putInt(NBT_MULTIPLIER, amount);
    }

    public int getMultiplier(final SpellComponentInstance<?> instance) {
        return instance.getData().getIntOr(NBT_MULTIPLIER, 0);
    }
}
