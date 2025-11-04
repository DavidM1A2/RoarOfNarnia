package com.dslovikosky.narnia.common.entity.spell;

import com.dslovikosky.narnia.common.constants.ModEntityDataSerializers;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.constants.ModSpellDeliveryMethods;
import com.dslovikosky.narnia.common.particle.ProjectileParticleData;
import com.dslovikosky.narnia.common.spell.Spell;
import com.dslovikosky.narnia.common.spell.SpellStage;
import com.dslovikosky.narnia.common.spell.component.DeliveryTransitionState;
import com.dslovikosky.narnia.common.spell.component.SpellComponentInstance;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.ProjectileSpellDeliveryMethod;
import com.dslovikosky.narnia.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.dslovikosky.narnia.common.utils.MathUtils;
import com.dslovikosky.narnia.common.utils.UuidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class SpellProjectileEntity extends Entity implements IEntityWithComplexSpawn {
    private static final double HIT_DELIVERY_TRANSITION_OFFSET = 0.01;

    private static final EntityDataAccessor<Spell> SPELL = SynchedEntityData.defineId(SpellProjectileEntity.class, ModEntityDataSerializers.SPELL.get());
    private static final EntityDataAccessor<Integer> STAGE_INDEX = SynchedEntityData.defineId(SpellProjectileEntity.class, EntityDataSerializers.INT);

    private Entity shooter = null;
    private UUID casterEntityId = null;
    private float distanceRemainingBlocks = 0f;
    private int ticksInAir = 0;

    public SpellProjectileEntity(final EntityType<SpellProjectileEntity> entityType, final Level level) {
        super(entityType, level);
    }

    public SpellProjectileEntity(
            final Level level,
            final Spell spell,
            final int stageIndex,
            final Entity spellCaster,
            final Vec3 position,
            final Vec3 velocity,
            final Entity shooter
    ) {
        super(ModEntityTypes.SPELL_PROJECTILE.get(), level);
        this.shooter = shooter;
        this.casterEntityId = Optional.ofNullable(spellCaster).map(Entity::getUUID).orElse(null);
        this.entityData.set(SpellProjectileEntity.SPELL, spell);
        this.entityData.set(SpellProjectileEntity.STAGE_INDEX, stageIndex);
        final ProjectileSpellDeliveryMethod projectileSpellDeliveryMethod = ModSpellDeliveryMethods.PROJECTILE.get();
        final SpellComponentInstance<SpellDeliveryMethod> deliveryInstance = spell.getStage(stageIndex).getDeliveryInstance();
        this.distanceRemainingBlocks = (float) projectileSpellDeliveryMethod.getRange(deliveryInstance);

        // Grab the projectile speed from the delivery method
        final double blocksPerSecond = projectileSpellDeliveryMethod.getSpeed(deliveryInstance);
        final double blocksPerTick = blocksPerSecond / 20.0;

        // Update the acceleration vector by normalizing it and multiplying by speed
        this.setDeltaMovement(velocity.normalize().scale(blocksPerTick));

        // Position the entity at the center of the shooter moved slightly in the dir of fire
        setPos(position.add(this.getDeltaMovement()));

        if (shooter != null) {
            setRot(shooter.getYRot(), shooter.getXRot());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SPELL, new Spell());
        builder.define(STAGE_INDEX, 0);
    }

    @Override
    public void tick() {
        super.tick();

        final Level level = level();
        if (!level.isLoaded(blockPosition())) {
            remove(RemovalReason.DISCARDED);
            return;
        }

        // We are in the air, so increment our counter
        this.ticksInAir = this.ticksInAir + 1;

        // Sometimes show particles
        if (level.isClientSide()) {
            for (int i = 0; i < random.nextInt(3); i++) {
                final Color color = getColor();
                final Vec3 position = position();
                final Vec3 velocity = getDeltaMovement().reverse()
                        .scale(0.4)
                        .multiply(random.nextDouble() * 0.4 + 0.8, random.nextDouble() * 0.4 + 0.8, random.nextDouble() * 0.4 + 0.8);
                level.addParticle(
                        new ProjectileParticleData(random.nextFloat() * 0.6f + 0.6f, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f),
                        position.x + getBbWidth() / 2 + (random.nextDouble() - 0.5) * 0.4,
                        position.y + getBbHeight() / 2 + (random.nextDouble() - 0.5) * 0.4,
                        position.z + getBbWidth() / 2 + (random.nextDouble() - 0.5) * 0.4,
                        velocity.x,
                        velocity.y,
                        velocity.z
                );
            }
        }

        // Process hit detection server side
        if (!level.isClientSide()) {
            final HitResult rayTraceResult = checkCollision();

            // If the ray trace hit something, perform the hit effect
            if (rayTraceResult.getType() != HitResult.Type.MISS) {
                onImpact(rayTraceResult);
            }
        }

        // Continue flying in the direction of motion, update the position
        setPos(getX() + getDeltaMovement().x(), getY() + getDeltaMovement().y(), getZ() + getDeltaMovement().z());

        // Update distance flown, and kill the entity if it went
        final double distanceFlown = getDeltaMovement().length();
        this.distanceRemainingBlocks = this.distanceRemainingBlocks - (float) distanceFlown;

        // If we're out of distance deliver the spell and kill the projectile
        if (this.distanceRemainingBlocks <= 0) {
            final Spell spell = this.entityData.get(SPELL);
            final int spellIndex = this.entityData.get(STAGE_INDEX);

            // Update spell logic server side
            if (!level.isClientSide()) {
                final DeliveryTransitionState state = new DeliveryTransitionState(
                        spell,
                        spellIndex,
                        level,
                        position(),
                        blockPosition(),
                        getDeltaMovement().normalize(),
                        MathUtils.getNormal(getDeltaMovement()),
                        Optional.ofNullable(casterEntityId).map(level::getEntity).orElse(null),
                        null,
                        this
                );

                // Proc the effects and transition
                final SpellDeliveryMethod currentDeliveryMethod = spell.getStage(spellIndex).getDeliveryInstance().getComponent();
                currentDeliveryMethod.procEffects(state);
                currentDeliveryMethod.transitionFrom(state);

                remove(RemovalReason.DISCARDED);
            }
        }
    }

    private HitResult checkCollision() {
        final Predicate<Entity> entityHitPredicate = entity -> {
            if (ticksInAir > 25) {
                return true;
            } else {
                return entity != shooter;
            }
        };

        // < 0.003 speed causes collision detection to break. Use custom logic
        if (getDeltaMovement().lengthSqr() < 0.00001) {
            final BlockHitResult blockResult = level().clip(
                    new ClipContext(
                            position(),
                            position().add(0.0, 0.001, 0.0),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE,
                            this
                    )
            );
            if (blockResult.getType() != HitResult.Type.MISS) {
                return blockResult;
            }

            final List<Entity> entities = level().getEntities(this, getBoundingBox());
            for (final Entity entity : entities) {
                if (entityHitPredicate.test(entity)) {
                    return new EntityHitResult(entity);
                }
            }
            return BlockHitResult.miss(position(), Direction.NORTH, blockPosition());
        } else {
            // Perform a ray case to test if we've hit something. We can only hit the entity that fired the projectile after 25 ticks
            return ProjectileUtil.getHitResultOnMoveVector(this, entityHitPredicate);
        }
    }

    private void onImpact(final HitResult result) {
        final Level level = level();
        if (level.isClientSide()) {
            return;
        }

        final Spell spell = this.entityData.get(SPELL);
        final int stageIndex = this.entityData.get(STAGE_INDEX);

        // Grab the current spell stage
        final SpellStage currentStage = spell.getStage(stageIndex);

        // If we hit something process the hit
        if (result.getType() == HitResult.Type.MISS) {
            return;
        }

        final SpellDeliveryMethod currentDeliveryMethod = currentStage.getDeliveryInstance().getComponent();
        final Vec3 direction = getDeltaMovement().normalize();
        if (result.getType() == HitResult.Type.BLOCK && result instanceof BlockHitResult blockHitResult) {
            // Grab the hit position
            BlockPos hitPos = blockHitResult.getBlockPos();

            // If we hit an air block find the block to the side of the air, hit that instead
            if (level.isEmptyBlock(hitPos)) {
                hitPos = hitPos.relative(blockHitResult.getDirection().getOpposite());
            }
            final DeliveryTransitionState state = new DeliveryTransitionState(
                    spell,
                    stageIndex,
                    level,
                    blockHitResult.getLocation(),
                    hitPos,
                    direction,
                    MathUtils.getNormal(direction),
                    Optional.ofNullable(casterEntityId).map(level::getEntity).orElse(null),
                    null,
                    this
            );

            // Proc the effects and transition
            currentDeliveryMethod.procEffects(state);
            currentDeliveryMethod.transitionFrom(state.copy(result.getLocation().subtract(direction.scale(HIT_DELIVERY_TRANSITION_OFFSET))));
        } else if (result.getType() == HitResult.Type.ENTITY && result instanceof EntityHitResult entityHitResult) {
            final Entity entityHit = entityHitResult.getEntity();
            final DeliveryTransitionState state = new DeliveryTransitionState(
                    spell,
                    stageIndex,
                    level,
                    result.getLocation(),
                    BlockPos.containing(entityHitResult.getLocation()),
                    direction,
                    MathUtils.getNormal(direction),
                    Optional.ofNullable(casterEntityId).map(level::getEntity).orElse(null),
                    entityHit,
                    this
            );

            // Proc the effects and transition
            currentDeliveryMethod.procEffects(state);

            final Vec3 position = entityHit.getEyePosition(1.0f);
            final DeliveryTransitionState transitionState = new DeliveryTransitionState(
                    state.getSpell(),
                    state.getStageIndex(),
                    state.getLevel(),
                    position,
                    BlockPos.containing(position),
                    entityHit.getLookAngle(),
                    entityHit.getUpVector(1f),
                    state.getCasterEntity(),
                    state.getEntity(),
                    state.getDeliveryEntity()
            );
            currentDeliveryMethod.transitionFrom(transitionState);
        }

        // Kill the projectile
        remove(RemovalReason.DISCARDED);
    }

    public Color getColor() {
        return ModSpellDeliveryMethods.PROJECTILE.get().getColor(entityData.get(SPELL).getSpellStages().get(entityData.get(STAGE_INDEX)).getDeliveryInstance());
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 0.2f;
    }

    @Override
    @Deprecated
    public float getLightLevelDependentMagicValue() {
        return 1f;
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.entityData.set(SPELL, new Spell(input.read("spell", CompoundTag.CODEC).orElse(new CompoundTag())));
        this.entityData.set(STAGE_INDEX, input.getIntOr("spell_index", 0));
        this.ticksInAir = input.getIntOr("ticks_in_air", 0);
        this.distanceRemainingBlocks = input.getFloatOr("distance_remaining_blocks", 0f);
        this.casterEntityId = UuidUtils.read("caster_entity_id", input).orElse(null);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.store("spell", CompoundTag.CODEC, this.entityData.get(SPELL).serializeNbt());
        output.putInt("spell_index", this.entityData.get(STAGE_INDEX));
        output.putInt("ticks_in_air", this.ticksInAir);
        output.putFloat("distance_remaining_blocks", this.distanceRemainingBlocks);
        if (casterEntityId != null) {
            UuidUtils.write("caster_entity_id", output, casterEntityId);
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.entityData.set(SPELL, new Spell(additionalData.readNbt()));
        this.entityData.set(STAGE_INDEX, additionalData.readInt());
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeNbt(this.entityData.get(SPELL).serializeNbt());
        buffer.writeInt(this.entityData.get(STAGE_INDEX));
    }
}
