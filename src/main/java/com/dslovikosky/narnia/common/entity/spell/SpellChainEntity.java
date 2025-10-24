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

public class SpellChainEntity extends SpellEntity {
    private static final EntityDataAccessor<Vec3> END_POS = SynchedEntityData.defineId(SpellChainEntity.class, ModEntityDataSerializers.VEC3.get());

    public SpellChainEntity(final EntityType<SpellChainEntity> entityType, final Level level) {
        super(entityType, level);
    }

    public SpellChainEntity(final Level level, final Vec3 startPos, final Vec3 endPos) {
        super(ModEntityTypes.SPELL_CHAIN.get(), level, startPos, 10);
        this.entityData.set(END_POS, endPos);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(END_POS, Vec3.ZERO);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == END_POS) {
            this.boundingBoxForCulling = new AABB(position(), this.entityData.get(END_POS));
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(END_POS, new Vec3(input.getDoubleOr("end_pos_x", 0.0),
                input.getDoubleOr("end_pos_y", 0.0),
                input.getDoubleOr("end_pos_z", 0.0)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putDouble("end_pos_x", this.entityData.get(END_POS).x());
        output.putDouble("end_pos_y", this.entityData.get(END_POS).y());
        output.putDouble("end_pos_z", this.entityData.get(END_POS).z());
    }

    public Vec3 getEndPos() {
        return this.entityData.get(END_POS);
    }
}
