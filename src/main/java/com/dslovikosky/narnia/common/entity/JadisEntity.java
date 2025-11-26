package com.dslovikosky.narnia.common.entity;

import com.dslovikosky.narnia.common.constants.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class JadisEntity extends PathfinderMob {
    public JadisEntity(final EntityType<JadisEntity> entityType, final Level level) {
        super(entityType, level);
    }

    public JadisEntity(final Level level) {
        super(ModEntityTypes.JADIS.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 4)
                .add(Attributes.FOLLOW_RANGE, 64.0);
    }
}
