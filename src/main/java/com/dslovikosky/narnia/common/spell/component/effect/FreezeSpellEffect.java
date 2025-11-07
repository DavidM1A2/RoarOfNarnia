package com.dslovikosky.narnia.common.spell.component.effect;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModAttachmentTypes;
import com.dslovikosky.narnia.common.constants.ModParticleTypes;
import com.dslovikosky.narnia.common.model.attachment_type.SpellFreezeData;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.effect.base.DurationSpellEffect;
import com.dslovikosky.narnia.common.spell.component.effect.base.ProcResult;
import com.dslovikosky.narnia.common.spell.component.effect.base.SpellEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FreezeSpellEffect extends DurationSpellEffect {
    public FreezeSpellEffect() {
        super(Constants.modLocation("freeze"), 1.0, 1.0, 60.0);
    }

    @Override
    public ProcResult proc(DeliveryTransitionState state, SpellComponentInstance<SpellEffect> instance) {
        final ServerLevel world = state.getLevel();
        final BlockPos blockPos = state.getBlockPosition();
        final Entity entity = state.getEntity();

        // If the entity hit is living freeze it in place
        if (entity != null) {
            // We can only freeze a living entity
            if (entity instanceof LivingEntity livingEntity) {
                // If we hit a player, freeze their position and direction
                if (entity instanceof Player player) {
                    final SpellFreezeData freezeData = player.getData(ModAttachmentTypes.SPELL_FREEZE_DATA);
                    player.setData(ModAttachmentTypes.SPELL_FREEZE_DATA, new SpellFreezeData(
                            Math.max(freezeData.getFreezeTicks(), (int) Math.ceil(getDuration(instance) * 20)),
                            new Vec3(entity.getX(), entity.getY(), entity.getZ()),
                            entity.getXRot(),
                            entity.getYRot()
                    ));
                } else {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, (int) Math.ceil(getDuration(instance) * 20), 99));
                }
                final double width = entity.getBbWidth() * 2.5;
                final double height = entity.getBbHeight();
                final Vec3 position = entity.getPosition(1f);
                for (int i = 0; i < 10; i++) {
                    world.sendParticles(ModParticleTypes.FREEZE.get(),
                            position.x() + RANDOM.nextDouble() * width - width / 2,
                            position.y() + RANDOM.nextDouble() * height,
                            position.z() + RANDOM.nextDouble() * width - width / 2,
                            // Speed's x isn't speed, but the duration that the freeze will last
                            0, getDuration(instance) * 20, 0.0, 0.0, 1.0);
                }
            } else {
                return ProcResult.failure();
            }
        } else {
            final BlockState hitBlock = world.getBlockState(blockPos);
            if (hitBlock.getBlock() != Blocks.LAVA && hitBlock.canBeReplaced()) {
                world.setBlockAndUpdate(blockPos, Blocks.PACKED_ICE.defaultBlockState());
                for (int i = 0; i < 4; i++) {
                    world.sendParticles(ModParticleTypes.FREEZE.get(),
                            blockPos.getX() + (RANDOM.nextBoolean() ? 1.01 : -0.01),
                            blockPos.getY() + (RANDOM.nextBoolean() ? 1.01 : -0.01),
                            blockPos.getZ() + (RANDOM.nextBoolean() ? 1.01 : -0.01),
                            // Speed's x isn't speed, but the duration that the freeze will last
                            0, 20.0, 0.0, 0.0, 1.0);
                }
            } else {
                return ProcResult.failure();
            }
        }
        return ProcResult.success();
    }

    @Override
    public double getCost(SpellComponentInstance<SpellEffect> instance) {
        // Freezing a block costs 3
        final double baseCost = 3.0;
        // Each second of duration costs 3.0, but the first 1 seconds are free
        final double durationCost = Math.max((getDuration(instance) - 1) * 3.0, 0.0);
        return baseCost + durationCost * durationCost;
    }
}
