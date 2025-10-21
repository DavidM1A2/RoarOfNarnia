package com.dslovikosky.narnia.common.entity.spell.base;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class SpellEntity extends Entity {
    private static final String NBT_LIFESPAN_TICKS = "lifespan_ticks";
    private static final EntityDataAccessor<Integer> LIFESPAN_TICKS = SynchedEntityData.defineId(SpellEntity.class, EntityDataSerializers.INT);

    protected AABB boundingBoxForCulling = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    public SpellEntity(final EntityType<?> entityType, final Level level) {
        super(entityType, level);
    }

    public SpellEntity(final EntityType<?> entityType, final Level level, final Vec3 startPos, final int lifespanTicks) {
        this(entityType, level);
        this.entityData.set(LIFESPAN_TICKS, lifespanTicks);
        this.setPos(startPos);
        this.setRot(0f, 0f);
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            if (tickCount > this.entityData.get(LIFESPAN_TICKS)) {
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        // Copy & Pasted from base class, except uses culling AABB instead of the regular AABB
        double boundingBoxSize = this.boundingBoxForCulling.getSize();
        if (Double.isNaN(boundingBoxSize)) {
            boundingBoxSize = 1.0;
        }

        boundingBoxSize = boundingBoxSize * 64.0 * getViewScale();
        return distance < boundingBoxSize * boundingBoxSize;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIFESPAN_TICKS, 0);
    }

    public AABB getBoundingBoxForCulling() {
        return boundingBoxForCulling;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.entityData.set(LIFESPAN_TICKS, input.getIntOr(NBT_LIFESPAN_TICKS, 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt(NBT_LIFESPAN_TICKS, this.entityData.get(LIFESPAN_TICKS));
    }

    public int getLifespanTicks() {
        return this.entityData.get(LIFESPAN_TICKS);
    }
}
