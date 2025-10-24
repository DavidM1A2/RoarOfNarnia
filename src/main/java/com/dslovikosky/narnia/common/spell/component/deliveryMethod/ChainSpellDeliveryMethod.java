package com.dslovikosky.narnia.common.spell.component.deliveryMethod;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.spell.SpellChainEntity;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.property.SpellComponentPropertyFactory;
import com.dslovikosky.narnia.common.utils.MathUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ChainSpellDeliveryMethod extends SpellDeliveryMethod {
    private static final String NBT_MAX_DISTANCE = "max_distance";
    private static final String NBT_MAX_HOPS = "max_hops";

    public ChainSpellDeliveryMethod() {
        super(Constants.modLocation("chain"));
        addEditableProperty(
                SpellComponentPropertyFactory.doubleProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("max_distance"))
                        .withMinValue(0.0)
                        .withDefaultValue(5.0)
                        .withMaxValue(20.0)
                        .withSetter(this::setMaxDistance)
                        .withGetter(this::getMaxDistance)
                        .build()
        );
        addEditableProperty(
                SpellComponentPropertyFactory.intProperty()
                        .withBaseName(getUnlocalizedPropertyBaseName("max_hops"))
                        .withMinValue(1)
                        .withDefaultValue(3)
                        .withMaxValue(25)
                        .withSetter(this::setMaxHops)
                        .withGetter(this::getMaxHops)
                        .build()
        );
    }

    @Override
    public void execute(DeliveryTransitionState state) {
        final SpellComponentInstance<?> instance = state.getCurrentStage().getDeliveryInstance();
        final double maxDistance = getMaxDistance(instance);
        final int maxHops = getMaxHops(instance);

        recursivelyChain(state.getSpell(), state.getStageIndex(), state.getCasterEntity(), state.getLevel(), state.getPosition(), state.getPosition(), maxHops, maxDistance, new HashSet<>());
    }

    private void recursivelyChain(
            final Spell spell,
            final int stageIndex,
            final Entity casterEntity,
            final Level level,
            final Vec3 position,
            final Vec3 lastHitCenterPos,
            final int hopsRemaining,
            final double maxRange,
            final Set<LivingEntity> hitEntities
    ) {
        // Recursive base case
        if (hopsRemaining <= 0) {
            return;
        }

        final Predicate<Entity> entityFilter = entity -> entity instanceof LivingEntity && !hitEntities.contains(entity) && canSee(entity, position);
        final LivingEntity nearestEntity = level.getEntities(
                        (Entity) null,
                        new AABB(position.x - maxRange, position.y - maxRange, position.z - maxRange, position.x + maxRange, position.y + maxRange, position.z + maxRange),
                        entityFilter)
                .stream()
                .map(entity -> (LivingEntity) entity)
                .filter(entity -> entity.distanceToSqr(position) < maxRange * maxRange)
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(position)))
                .orElse(null);

        // No entity to chain to, we're done
        if (nearestEntity == null) {
            return;
        }

        // Proc the effects and transition from the entity
        final Vec3 direction = nearestEntity.position().subtract(position).normalize();
        final DeliveryTransitionState transitionState = new DeliveryTransitionState(
                spell,
                stageIndex,
                level,
                nearestEntity.position(),
                nearestEntity.blockPosition(),
                direction,
                MathUtils.getNormal(direction),
                casterEntity,
                nearestEntity,
                null
        );
        procEffects(transitionState);
        transitionFrom(transitionState.copy(nearestEntity.getLookAngle(), nearestEntity.getUpVector(1f)));

        final Vec3 nearestEntityCenterPos = nearestEntity.position().add(0.0, nearestEntity.getBbHeight() / 2.0, 0.0);
        level.addFreshEntity(new SpellChainEntity(level, lastHitCenterPos, nearestEntityCenterPos));

        hitEntities.add(nearestEntity);
        recursivelyChain(spell, stageIndex, casterEntity, level, nearestEntity.position(), nearestEntityCenterPos, hopsRemaining - 1, maxRange, hitEntities);
    }

    private boolean canSee(final Entity entity, final Vec3 position) {
        final Vec3 startPos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
        if (position.distanceToSqr(startPos) > 128.0 * 128.0) {
            return false;
        } else {
            return entity.level()
                    .clip(new ClipContext(startPos, position, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
                    .getType() == HitResult.Type.MISS;
        }
    }

    @Override
    public double getDeliveryCost(SpellComponentInstance<SpellDeliveryMethod> instance) {
        // Each hop adds 1.5 vitae
        final double hopsCostMultiplier = getMaxHops(instance) * 1.5;
        // 10 blocks per vitae
        final double distanceCost = getMaxDistance(instance) * 0.1;
        return hopsCostMultiplier * distanceCost;
    }

    @Override
    public double getMultiplicity(SpellComponentInstance<SpellDeliveryMethod> instance) {
        return getMaxHops(instance);
    }

    public void setMaxDistance(final SpellComponentInstance<?> instance, final double maxDistance) {
        instance.getData().putDouble(NBT_MAX_DISTANCE, maxDistance);
    }

    public double getMaxDistance(final SpellComponentInstance<?> instance) {
        return instance.getData().getDoubleOr(NBT_MAX_DISTANCE, 0.0);
    }

    public void setMaxHops(final SpellComponentInstance<?> instance, final int maxHops) {
        instance.getData().putInt(NBT_MAX_HOPS, maxHops);
    }

    public int getMaxHops(final SpellComponentInstance<?> instance) {
        return instance.getData().getIntOr(NBT_MAX_HOPS, 0);
    }
}
