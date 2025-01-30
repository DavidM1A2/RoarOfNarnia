package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block.entity.PositionalMarkerBlockEntity;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PositionalMarkerBlockEntity>> POSITIONAL_MARKER = BLOCK_ENTITIES.register("positional_marker",
            it -> new BlockEntityType<>(PositionalMarkerBlockEntity::new, ImmutableSet.of(ModBlocks.POSITIONAL_MARKER.get()), null));
}
