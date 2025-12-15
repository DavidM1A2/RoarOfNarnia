package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block_entity.CharnBellBlockEntity;
import com.dslovikosky.narnia.common.block_entity.CharnImageHallStatueBlockEntity;
import com.dslovikosky.narnia.common.block_entity.RuneBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CharnBellBlockEntity>> CHARN_BELL = BLOCK_ENTITY_TYPES.register(
            "charn_bell", () -> new BlockEntityType<>(CharnBellBlockEntity::new, ModBlocks.CHARN_BELL.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CharnImageHallStatueBlockEntity>> CHARN_IMAGE_HALL_STATUE = BLOCK_ENTITY_TYPES.register(
            "charn_image_hall_statue", () -> new BlockEntityType<>(CharnImageHallStatueBlockEntity::new, ModBlocks.CHARN_IMAGE_HALL_STATUE.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RuneBlockEntity>> RUNE = BLOCK_ENTITY_TYPES.register(
            "rune", () -> new BlockEntityType<>(RuneBlockEntity::new, ModBlocks.BLUE_RUNE.get(), ModBlocks.GREEN_RUNE.get(), ModBlocks.RED_RUNE.get(), ModBlocks.YELLOW_RUNE.get()));
}
