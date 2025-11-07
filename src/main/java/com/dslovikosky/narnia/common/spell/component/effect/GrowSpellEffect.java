package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class GrowSpellEffect extends SpellEffect {
    private static final String NBT_STRENGTH = "strength";

    public GrowSpellEffect() {
        super(Constants.modLocation("grow"));
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("strength"))
                        .withSetter(this::setStrength)
                        .withGetter(this::getStrength)
                        .withDefaultValue(1)
                        .withMinValue(1)
                        .withMaxValue(10)
                        .build()
        );
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final ServerLevel level = state.getLevel();
        BlockPos position = Optional.ofNullable(state.getEntity()).map(Entity::blockPosition).orElse(state.getBlockPosition());
        BlockState blockState = level.getBlockState(position);

        // If we hit a block that crops might be on check the block above and below and to if we can grow on that instead
        if (!(blockState.getBlock() instanceof BonemealableBlock)) {
            position = position.above();
            blockState = level.getBlockState(position);
            if (!(blockState.getBlock() instanceof BonemealableBlock)) {
                position = position.below(2);
                blockState = level.getBlockState(position);
            }
        }

        boolean blockUpdated = false;
        boolean blockValid = false;
        // Grob the block at the current position if it's a type 'IGrowable'
        for (int i = 0; i < getStrength(instance); i++) {
            if (!(blockState.getBlock() instanceof BonemealableBlock iGrowable)) {
                break;
            }

            if (!iGrowable.isValidBonemealTarget(level, position, blockState)) {
                break;
            }

            blockValid = true;
            if (!iGrowable.isBonemealSuccess(level, level.random, position, blockState)) {
                continue;
            }

            iGrowable.performBonemeal(level, level.random, position, blockState);
            blockUpdated = true;
            blockState = level.getBlockState(position);
        }

        if (blockUpdated) {
            level.sendBlockUpdated(position, blockState, blockState, Block.UPDATE_CLIENTS);
        }
        if (blockValid) {
            for (int i = 0; i < (blockUpdated ? 5 : 2); i++) {
                level.sendParticles(ModParticleTypes.GROW.get(),
                        position.getX() + RANDOM.nextDouble(), position.getY() + RANDOM.nextDouble(), position.getZ() + RANDOM.nextDouble(),
                        0, 0.0, 0.0, 0.0, 0.0);
            }
        } else {
            return ProcResult.failure();
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        return getStrength(instance) * 3.0;
    }

    public void setStrength(final SpellComponentInstance<?> instance, final int strength) {
        instance.getData().putInt(NBT_STRENGTH, strength);
    }

    public int getStrength(final SpellComponentInstance<?> instance) {
        return instance.getData().getIntOr(NBT_STRENGTH, 0);
    }
}
