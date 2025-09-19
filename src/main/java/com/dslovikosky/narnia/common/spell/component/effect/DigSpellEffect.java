package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.DurationSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.time.Duration;

public class DigSpellEffect extends DurationSpellEffect {
    private static final String NBT_SPEED = "speed";
    private static final double FREE_DURATION = 3.0;

    public DigSpellEffect() {
        super(Constants.modLocation("dig"), 0.0, FREE_DURATION, (double) Duration.ofMinutes(20).getSeconds());
        addEditableProperty(SpellComponentPropertyFactory.intProperty()
                .withBaseName(getUnlocalizedPropertyBaseName("speed"))
                .withSetter(this::setSpeed)
                .withGetter(this::getSpeed)
                .withDefaultValue(1)
                .withMinValue(-10)
                .withMaxValue(10)
                .build());
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final Level world = state.getLevel();
        final Entity entity = state.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            final double speed = getSpeed(instance);
            if (speed != 0) {
                final Holder<MobEffect> effectType = speed >= 0 ? MobEffects.HASTE : MobEffects.MINING_FATIGUE;
                final MobEffectInstance effect = new MobEffectInstance(effectType, (int) Math.ceil(getDuration(instance) * 20), (int) (Math.abs(speed) - 1));
                livingEntity.addEffect(effect);
            } else {
                return ProcResult.failure();
            }
        } else {
            // Digs the block at the position
            final BlockPos position = state.getBlockPosition();
            if (canBlockBeDestroyed(world, position)) {
                world.destroyBlock(position, true);
            } else {
                return ProcResult.failure();
            }
        }
        return ProcResult.success();
    }

    private boolean canBlockBeDestroyed(final Level level, final BlockPos blockPos) {
        final BlockState blockState = level.getBlockState(blockPos);
        return blockState.getDestroySpeed(level, blockPos) != -1f && !blockState.isAir();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        final double speed = getSpeed(instance);
        // Digging a block costs 1
        final double baseCost = 1.0;
        // Each second of duration costs 0.25, but the first 3 seconds are free
        final double durationCost = Math.max(0, (getDuration(instance) - FREE_DURATION) * 0.25);
        final double speedCostMultiplier = Math.abs(speed);
        return baseCost + speedCostMultiplier * durationCost;
    }

    public void setSpeed(final SpellComponentInstance<?> instance, final int amount) {
        instance.getData().putInt(NBT_SPEED, amount);
    }

    public int getSpeed(final SpellComponentInstance<?> instance) {
        return instance.getData().getInt(NBT_SPEED).get();
    }
}
