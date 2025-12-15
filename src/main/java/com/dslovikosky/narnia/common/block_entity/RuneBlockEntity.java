package com.dslovikosky.narnia.common.block_entity;

import com.dslovikosky.narnia.common.block.RuneBlock;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.dslovikosky.narnia.common.world.data.CharnRuneSavedData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RuneBlockEntity extends BlockEntity {
    public RuneBlockEntity(final BlockPos pos, final BlockState blockState) {
        super(ModBlockEntities.RUNE.get(), pos, blockState);
    }

    public static void tick(final Level level, final BlockPos pos, final BlockState state, final BlockEntity blockEntity) {
        if (level.getGameTime() % 20 != 0) {
            return;
        }

        final Block block = state.getBlock();
        if (level.isClientSide() || (!(block instanceof RuneBlock runeBlock)) || (!(level instanceof ServerLevel serverLevel))) {
            return;
        }

        final CharnRuneSavedData runeData = serverLevel.getDataStorage().computeIfAbsent(CharnRuneSavedData.ID);
        final RuneBlock.Color color = runeBlock.getColor();
        if (runeData.isRuneBroken(color)) {
            level.destroyBlock(pos, false);
        }
    }
}
