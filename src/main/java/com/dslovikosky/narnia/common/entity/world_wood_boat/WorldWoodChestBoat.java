package com.dslovikosky.narnia.common.entity.world_wood_boat;

import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.level.Level;

public class WorldWoodChestBoat extends ChestBoat {
    public WorldWoodChestBoat(final EntityType<WorldWoodChestBoat> entityType, final Level level) {
        super(entityType, level, ModItems.WORLD_WOOD_CHEST_BOAT::asItem);
    }
}
