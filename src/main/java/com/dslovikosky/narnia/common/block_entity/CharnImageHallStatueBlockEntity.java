package com.dslovikosky.narnia.common.block_entity;

import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CharnImageHallStatueBlockEntity extends BlockEntity {
    public CharnImageHallStatueBlockEntity(final BlockPos pos, final BlockState blockState) {
        super(ModBlockEntities.CHARN_IMAGE_HALL_STATUE.get(), pos, blockState);
    }
}
