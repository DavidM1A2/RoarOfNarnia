package com.dslovikosky.narnia.common.entity.spell;

import com.dslovikosky.narnia.common.constants.ModEntityDataSerializers;
import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import com.dslovikosky.narnia.common.entity.spell.base.SpellEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;

public class SpellWallEntity extends SpellEntity {
    private static final EntityDataAccessor<Color> COLOR = SynchedEntityData.defineId(SpellWallEntity.class, ModEntityDataSerializers.COLOR.get());
    private static final EntityDataAccessor<Vec3> WIDTH = SynchedEntityData.defineId(SpellWallEntity.class, ModEntityDataSerializers.VEC3.get());
    private static final EntityDataAccessor<Vec3> HEIGHT = SynchedEntityData.defineId(SpellWallEntity.class, ModEntityDataSerializers.VEC3.get());

    public SpellWallEntity(final EntityType<?> entityType, final Level level) {
        super(entityType, level);
    }

    public SpellWallEntity(final Level world, final Vec3 centerPos, final Vec3 width, final Vec3 height, final Color color) {
        super(ModEntityTypes.SPELL_WALL.get(), world, centerPos, 12);
        this.entityData.set(COLOR, color);
        this.entityData.set(WIDTH, width);
        this.entityData.set(HEIGHT, height);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, Color.WHITE);
        builder.define(WIDTH, new Vec3(1.0, 0.0, 0.0));
        builder.define(HEIGHT, new Vec3(0.0, 1.0, 0.0));
    }

    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == WIDTH || key == HEIGHT) {
            final Vec3 position = position();
            final Vec3 width = this.entityData.get(WIDTH);
            final Vec3 height = this.entityData.get(HEIGHT);
            this.boundingBoxForCulling = new AABB(
                    position.subtract(width).subtract(height),
                    position.add(width).add(height)
            );
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(COLOR, new Color(input.getIntOr("red", 0), input.getIntOr("green", 0), input.getIntOr("blue", 0)));
        this.entityData.set(WIDTH, new Vec3(input.getDoubleOr("width_x", 0.0), input.getDoubleOr("width_y", 0.0), input.getDoubleOr("width_z", 0.0)));
        this.entityData.set(HEIGHT, new Vec3(input.getDoubleOr("height_x", 0.0), input.getDoubleOr("height_y", 0.0), input.getDoubleOr("height_z", 0.0)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("red", this.entityData.get(COLOR).getRed());
        output.putInt("green", this.entityData.get(COLOR).getGreen());
        output.putInt("blue", this.entityData.get(COLOR).getBlue());
        output.putDouble("width_x", this.entityData.get(WIDTH).x());
        output.putDouble("width_y", this.entityData.get(WIDTH).y());
        output.putDouble("width_z", this.entityData.get(WIDTH).z());
        output.putDouble("height_x", this.entityData.get(HEIGHT).x());
        output.putDouble("height_y", this.entityData.get(HEIGHT).y());
        output.putDouble("height_z", this.entityData.get(HEIGHT).z());
    }

    public Color getColor() {
        return this.entityData.get(COLOR);
    }

    public Vec3 getWidth() {
        return this.entityData.get(WIDTH);
    }

    public Vec3 getHeight() {
        return this.entityData.get(HEIGHT);
    }
}
