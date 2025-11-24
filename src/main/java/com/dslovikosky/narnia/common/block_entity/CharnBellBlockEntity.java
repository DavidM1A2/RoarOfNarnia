package com.dslovikosky.narnia.common.block_entity;

import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CharnBellBlockEntity extends BlockEntity {
    public CharnBellBlockEntity(final BlockPos pos, final BlockState blockState) {
        super(ModBlockEntities.CHARN_BELL.get(), pos, blockState);
    }
}
