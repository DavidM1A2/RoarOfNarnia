package com.dslovikosky.narnia.common.block;

import com.dslovikosky.narnia.common.block_entity.CharnImageHallStatueBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.model.CharnStatueType;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CharnImageHallStatueBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final EnumProperty<CharnStatueType> TYPE = EnumProperty.create("charn_statue_type", CharnStatueType.class);

    private static final MapCodec<CharnImageHallStatueBlock> CODEC = simpleCodec(CharnImageHallStatueBlock::new);

    public CharnImageHallStatueBlock() {
        this(Properties.of()
                .noOcclusion()
                .mapColor(MapColor.COLOR_BLACK)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(50.0F, 1200.0F)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation("charn_image_hall_statue"))));
    }

    private CharnImageHallStatueBlock(final Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new CharnImageHallStatueBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TYPE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(TYPE, CharnStatueType.JADIS);
    }
}
