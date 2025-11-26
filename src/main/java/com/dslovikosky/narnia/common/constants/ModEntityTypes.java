package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.entity.JadisEntity;
import com.dslovikosky.narnia.common.entity.spell.SpellAOEEntity;
import com.dslovikosky.narnia.common.entity.spell.SpellChainEntity;
import com.dslovikosky.narnia.common.entity.spell.SpellConeEntity;
import com.dslovikosky.narnia.common.entity.spell.SpellProjectileEntity;
import com.dslovikosky.narnia.common.entity.spell.SpellWallEntity;
import com.dslovikosky.narnia.common.entity.world_wood_boat.WorldWoodBoat;
import com.dslovikosky.narnia.common.entity.world_wood_boat.WorldWoodChestBoat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
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

    public static final DeferredHolder<EntityType<?>, EntityType<WorldWoodChestBoat>> WORLD_WOOD_CHEST_BOAT = ENTITY_TYPES.register(
            "world_wood_chest_boat",
            () -> EntityType.Builder.of(WorldWoodChestBoat::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.375F, 0.5625F)
                    .eyeHeight(0.5625F)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("world_wood_chest_boat")))
    );

    public static final DeferredHolder<EntityType<?>, EntityType<JadisEntity>> JADIS = ENTITY_TYPES.register(
            "jadis",
            () -> EntityType.Builder.<JadisEntity>of(JadisEntity::new, MobCategory.CREATURE)
                    .setTrackingRange(50)
                    .setUpdateInterval(100)
                    .setShouldReceiveVelocityUpdates(true)
                    .noLootTable()
                    .sized(0.6F, 2.34F)
                    .eyeHeight(2.2F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("jadis")))
    );

    // Spell entities

    public static final DeferredHolder<EntityType<?>, EntityType<SpellAOEEntity>> SPELL_AOE = ENTITY_TYPES.register(
            "spell_aoe",
            () -> EntityType.Builder.<SpellAOEEntity>of(SpellAOEEntity::new, MobCategory.MISC)
                    .setTrackingRange(50)
                    .setUpdateInterval(100)
                    .setShouldReceiveVelocityUpdates(false)
                    .noLootTable()
                    .sized(0f, 0f)
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("spell_aoe")))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SpellChainEntity>> SPELL_CHAIN = ENTITY_TYPES.register(
            "spell_chain",
            () -> EntityType.Builder.<SpellChainEntity>of(SpellChainEntity::new, MobCategory.MISC)
                    .setTrackingRange(50)
                    .setUpdateInterval(100)
                    .setShouldReceiveVelocityUpdates(false)
                    .noLootTable()
                    .sized(0f, 0f)
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("spell_chain")))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SpellProjectileEntity>> SPELL_PROJECTILE = ENTITY_TYPES.register(
            "spell_projectile",
            () -> EntityType.Builder.<SpellProjectileEntity>of(SpellProjectileEntity::new, MobCategory.MISC)
                    .setTrackingRange(50)
                    .setUpdateInterval(1)
                    .setShouldReceiveVelocityUpdates(true)
                    .noLootTable()
                    .sized(0.2f, 0.2f)
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("spell_projectile")))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SpellConeEntity>> SPELL_CONE = ENTITY_TYPES.register(
            "spell_cone",
            () -> EntityType.Builder.<SpellConeEntity>of(SpellConeEntity::new, MobCategory.MISC)
                    .setTrackingRange(50)
                    .setUpdateInterval(100)
                    .setShouldReceiveVelocityUpdates(false)
                    .noLootTable()
                    .sized(0f, 0f)
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("spell_cone")))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SpellWallEntity>> SPELL_WALL = ENTITY_TYPES.register(
            "spell_wall",
            () -> EntityType.Builder.<SpellWallEntity>of(SpellWallEntity::new, MobCategory.MISC)
                    .setTrackingRange(50)
                    .setUpdateInterval(100)
                    .setShouldReceiveVelocityUpdates(false)
                    .noLootTable()
                    .sized(0f, 0f)
                    .noSummon()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Constants.modLocation("spell_wall")))
    );
}
