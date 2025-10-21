package com.dslovikosky.narnia.common.entity.spell;

import com.dslovikosky.narnia.common.constants.ModEntityDataSerializers;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.entity.spell.base.SpellEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;

public class SpellAOEEntity extends SpellEntity {
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(SpellAOEEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Color> COLOR = SynchedEntityData.defineId(SpellAOEEntity.class, ModEntityDataSerializers.COLOR.get());

    public SpellAOEEntity(final EntityType<SpellAOEEntity> entityType, final Level level) {
        super(entityType, level);
    }

    public SpellAOEEntity(final Level level, final Vec3 startPos, final float radius, final Color color) {
        super(ModEntityTypes.SPELL_AOE.get(), level, startPos, 10);
        this.entityData.set(RADIUS, radius);
        this.entityData.set(COLOR, color);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RADIUS, 0.0f);
        builder.define(COLOR, Color.WHITE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == RADIUS) {
            final double radius = this.entityData.get(RADIUS);
            this.boundingBoxForCulling = new AABB(
                    position().subtract(radius, radius, radius),
                    position().add(radius, radius, radius));
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(RADIUS, input.getFloatOr("radius", 0f));
        this.entityData.set(COLOR, new Color(input.getIntOr("red", 0), input.getIntOr("green", 0), input.getIntOr("blue", 0)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("radius", this.entityData.get(RADIUS));
        output.putInt("red", this.entityData.get(COLOR).getRed());
        output.putInt("green", this.entityData.get(COLOR).getGreen());
        output.putInt("blue", this.entityData.get(COLOR).getBlue());
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public Color getColor() {
        return this.entityData.get(COLOR);
    }
}
