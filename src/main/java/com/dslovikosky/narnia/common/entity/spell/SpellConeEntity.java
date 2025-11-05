package com.dslovikosky.narnia.common.entity.spell;

import com.dslovikosky.narnia.common.constants.ModEntityDataSerializers;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.entity.spell.base.SpellEntity;
import com.dslovikosky.narnia.common.utils.ConeUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;

public class SpellConeEntity extends SpellEntity {
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(SpellConeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Color> COLOR = SynchedEntityData.defineId(SpellConeEntity.class, ModEntityDataSerializers.COLOR.get());
    private static final EntityDataAccessor<Float> LENGTH = SynchedEntityData.defineId(SpellConeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vec3> DIRECTION = SynchedEntityData.defineId(SpellConeEntity.class, ModEntityDataSerializers.VEC3.get());

    public SpellConeEntity(final EntityType<SpellConeEntity> entityType, final Level level) {
        super(entityType, level);
    }

    public SpellConeEntity(final Level level, final Vec3 tipPos, final float radius, final float length, final Color color, final Vec3 direction) {
        super(ModEntityTypes.SPELL_CONE.get(), level, tipPos, 12);
        this.entityData.set(RADIUS, radius);
        this.entityData.set(LENGTH, length);
        this.entityData.set(COLOR, color);
        this.entityData.set(DIRECTION, direction);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RADIUS, 1.0f);
        builder.define(LENGTH, 2.0f);
        builder.define(COLOR, Color.WHITE);
        builder.define(DIRECTION, new Vec3(0.0, 1.0, 0.0));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == RADIUS || key == LENGTH || key == DIRECTION) {
            final double radius = this.entityData.get(RADIUS);
            final double length = this.entityData.get(LENGTH);
            final Vec3 direction = this.entityData.get(DIRECTION);
            this.boundingBoxForCulling = ConeUtils.getBoundingBox(position(), direction, radius, length);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(RADIUS, input.getFloatOr("radius", 0f));
        this.entityData.set(COLOR, new Color(input.getIntOr("red", 0), input.getIntOr("green", 0), input.getIntOr("blue", 0)));
        this.entityData.set(LENGTH, input.getFloatOr("length", 0f));
        this.entityData.set(DIRECTION, new Vec3(input.getDoubleOr("direction_x", 0.0), input.getDoubleOr("direction_y", 0.0), input.getDoubleOr("direction_z", 0.0)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("radius", this.entityData.get(RADIUS));
        output.putInt("red", this.entityData.get(COLOR).getRed());
        output.putInt("green", this.entityData.get(COLOR).getGreen());
        output.putInt("blue", this.entityData.get(COLOR).getBlue());
        output.putFloat("length", this.entityData.get(LENGTH));
        output.putDouble("direction_x", this.entityData.get(DIRECTION).x());
        output.putDouble("direction_y", this.entityData.get(DIRECTION).y());
        output.putDouble("direction_z", this.entityData.get(DIRECTION).z());
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public float getLength() {
        return this.entityData.get(LENGTH);
    }

    public Color getColor() {
        return this.entityData.get(COLOR);
    }

    public Vec3 getConeDirection() {
        return this.entityData.get(DIRECTION);
    }
}
