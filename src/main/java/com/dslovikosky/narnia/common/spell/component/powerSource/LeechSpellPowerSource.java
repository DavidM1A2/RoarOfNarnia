package com.dslovikosky.narnia.common.spell.component.powerSource;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlockTags;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.CastEnvironment;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellCastResult;
import com.dslovikosky.narnia.common.spell.component.powerSource.base.SpellPowerSource;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class LeechSpellPowerSource extends SpellPowerSource<LeechSpellPowerSource.LeechContext> {
    private static final Logger LOG = LogManager.getLogger();
    private static final double VITAE_PER_HP = 4.0;
    private static final int LEECH_RANGE_BLOCKS = 12;
    private static final Map<TagKey<Block>, Double> TAG_TO_VITAE_VALUE = Map.of(
            ModBlockTags.GRASS_LIKE, 1.0,
            BlockTags.SWORD_EFFICIENT, 4.0,
            BlockTags.FLOWERS, 2.0
    );

    public LeechSpellPowerSource() {
        super(Constants.modLocation("leech"));
    }

    @Override
    public SpellCastResult cast(Entity entity, Spell spell, CastEnvironment<LeechContext> environment) {
        if (environment.getVitaeAvailable() < spell.getCost()) {
            return SpellCastResult.failure(Component.translatable(getUnlocalizedBaseName() + ".not_enough_power"));
        }

        double vitaeToDistribute = spell.getCost();

        // Destroy all leeched blocks
        final LeechContext context = environment.getContext();
        final List<BlockPos> blocksToDestroy = new ArrayList<>(context.nearbyBlockToVitae.keySet());
        Collections.shuffle(blocksToDestroy);
        for (final BlockPos blockToDestroy : blocksToDestroy) {
            if (vitaeToDistribute <= 0) {
                break;
            }

            vitaeToDistribute = vitaeToDistribute - context.nearbyBlockToVitae.get(blockToDestroy);
            if (context.grassPositions.contains(blockToDestroy)) {
                entity.level().setBlockAndUpdate(blockToDestroy, Blocks.DIRT.defaultBlockState());
            } else {
                entity.level().setBlockAndUpdate(blockToDestroy, Blocks.AIR.defaultBlockState());
            }
        }

        // Damage all leeched entities
        for (final Map.Entry<LivingEntity, Double> entry : distributeDamageOver(context.leechableEntities, vitaeToDistribute / VITAE_PER_HP).entrySet()) {
            final LivingEntity livingEntity = entry.getKey();
            livingEntity.hurt(livingEntity.damageSources().fellOutOfWorld(), entry.getValue().floatValue());
        }

        return SpellCastResult.success();
    }

    @Override
    public CastEnvironment<LeechContext> computeCastEnvironment(Entity entity) {
        final Map<BlockPos, Double> nearbyBlockToVitae = new HashMap<>();
        final Set<BlockPos> grassPositions = new HashSet<>();
        double totalBlockVitae = 0.0;

        // Find all nearby blocks that have vitae potential
        for (int x = -LEECH_RANGE_BLOCKS; x < LEECH_RANGE_BLOCKS; x++) {
            for (int y = -LEECH_RANGE_BLOCKS; y < LEECH_RANGE_BLOCKS; y++) {
                for (int z = -LEECH_RANGE_BLOCKS; z < LEECH_RANGE_BLOCKS; z++) {
                    if (x * x + y * y + z * z <= LEECH_RANGE_BLOCKS * LEECH_RANGE_BLOCKS) {
                        final BlockPos blockPos = entity.blockPosition().offset(x, y, z);
                        final BlockState blockState = entity.level().getBlockState(blockPos);
                        final Optional<Double> blockVitaeOpt = TAG_TO_VITAE_VALUE.entrySet()
                                .stream()
                                .filter(it -> blockState.is(it.getKey()))
                                .map(Map.Entry::getValue)
                                .findAny();
                        if (blockVitaeOpt.isPresent()) {
                            final double blockVitae = blockVitaeOpt.get();
                            totalBlockVitae = totalBlockVitae + blockVitae;
                            nearbyBlockToVitae.put(blockPos, blockVitae);
                            if (blockState.is(ModBlockTags.GRASS_LIKE)) {
                                grassPositions.add(blockPos);
                            }
                        }
                    }
                }
            }
        }

        // Find all nearby entities that have vitae potential. Skip this if we already have enough vitae from blocks
        final List<LivingEntity> leechableEntities = getLeechableEntities(entity);

        final double availableHealth = leechableEntities.stream().map(it -> it.getHealth() / 2.0).reduce(0.0, Double::sum);
        final double maxHealth = leechableEntities.stream().map(it -> it.getMaxHealth() / 2.0).reduce(0.0, Double::sum);

        return CastEnvironment.withVitae(
                totalBlockVitae + availableHealth * VITAE_PER_HP,
                totalBlockVitae + maxHealth * VITAE_PER_HP,
                new LeechContext(nearbyBlockToVitae, grassPositions, leechableEntities)
        );
    }

    @Override
    protected Number getSourceSpecificCost(double vitae) {
        return MathUtils.round(vitae, 1);
    }

    private List<LivingEntity> getLeechableEntities(final Entity entity) {
        return entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(LEECH_RANGE_BLOCKS), it -> {
            // Don't leech ourselves
            if (it == entity) {
                return false;
            }
            // Don't leech entities that are currently being hurt. This avoids players getting "free" leeches without doing damage
            if (it.hurtTime > 0) {
                return false;
            }
            // Don't leech entities through walls
            if (!it.hasLineOfSight(entity)) {
                return false;
            }
            // Don't leech armor stands/mannequins
            if (!(it instanceof Mob || it instanceof Player)) {
                return false;
            }
            return true;
        });
    }

    private Map<LivingEntity, Double> distributeDamageOver(final List<LivingEntity> entities, final double damage) {
        final Map<LivingEntity, Double> entityToDamage = new HashMap<>();
        int currentEntityToDamage = 0;
        double damageToDistribute = damage;
        // Safety measure to avoid infinite loops
        boolean didDamageInLastIteration = false;

        while (damageToDistribute > 0) {
            final LivingEntity affectedEntity = entities.get(currentEntityToDamage);
            final double currentDamage = entityToDamage.getOrDefault(affectedEntity, 0.0);
            final double damageToAdd = Math.min(Math.min(damageToDistribute, 1.0), affectedEntity.getHealth() / 2.0 - currentDamage);
            if (damageToAdd > 0) {
                didDamageInLastIteration = true;
            }
            damageToDistribute = damageToDistribute - damageToAdd;
            entityToDamage.put(affectedEntity, currentDamage + damageToAdd);

            currentEntityToDamage = (currentEntityToDamage + 1) % entities.size();
            if (currentEntityToDamage == 0 && !didDamageInLastIteration) {
                LOG.error("Attempted to leech more damage than possible? Remaining damage: {}", damageToDistribute);
                break;
            }
        }
        return entityToDamage;
    }

    public record LeechContext(Map<BlockPos, Double> nearbyBlockToVitae, Set<BlockPos> grassPositions, List<LivingEntity> leechableEntities) {
    }
}
