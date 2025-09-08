package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block.entity.WorldWoodBoat;
import com.dslovikosky.narnia.common.block.entity.WorldWoodChestBoat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityTypes {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(Constants.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> WORLD_WOOD_BOAT = ENTITY_TYPES.register(
            "world_wood_boat",
            () -> EntityType.Builder.of(WorldWoodBoat::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.375F, 0.5625F)
                    .eyeHeight(0.5625F)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("world_wood_boat")))
    );

    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> WORLD_WOOD_CHEST_BOAT = ENTITY_TYPES.register(
            "world_wood_chest_boat",
            () -> EntityType.Builder.of(WorldWoodChestBoat::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.375F, 0.5625F)
                    .eyeHeight(0.5625F)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("world_wood_chest_boat")))
    );
}
