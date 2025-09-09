package com.dslovikosky.narnia.common.entity.world_wood_boat;

import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;

public class WorldWoodBoat extends Boat {
    public WorldWoodBoat(final EntityType<Boat> entityType, final Level level) {
        super(entityType, level, ModItems.WORLD_WOOD_BOAT::asItem);
    }
}
