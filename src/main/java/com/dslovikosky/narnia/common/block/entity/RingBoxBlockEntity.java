package com.dslovikosky.narnia.common.block.entity;

import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RingBoxBlockEntity extends BlockEntity {
    public RingBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.RING_BOX.get(), blockPos, blockState);
    }
}
