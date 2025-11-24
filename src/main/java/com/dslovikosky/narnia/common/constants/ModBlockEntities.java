package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block_entity.CharnBellBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CharnBellBlockEntity>> CHARN_BELL = BLOCK_ENTITY_TYPES.register(
            "charn_bell", () -> new BlockEntityType<>(CharnBellBlockEntity::new, ModBlocks.CHARN_BELL.get()));
}
