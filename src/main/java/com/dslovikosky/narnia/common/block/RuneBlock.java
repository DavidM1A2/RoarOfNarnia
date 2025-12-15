package com.dslovikosky.narnia.common.block;

import com.dslovikosky.narnia.common.block_entity.RuneBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlockEntities;
import com.dslovikosky.narnia.common.constants.ModDimensions;
import com.dslovikosky.narnia.common.world.data.CharnRuneSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RuneBlock extends Block implements EntityBlock {
    private final Color color;

    public RuneBlock(final Color color) {
        super(Properties.of()
                .noOcclusion()
                .mapColor(MapColor.SAND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(3.0F, 9.0F)
                .isViewBlocking((ignoredA, ignoredB, ignoredC) -> false)
                .lightLevel((state) -> 15)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation(color.getName() + "_rune"))));
        this.color = color;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new RuneBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ModBlockEntities.RUNE.get()) {
            return RuneBlockEntity::tick;
        }

        return null;
    }

    @Override
    public boolean onDestroyedByPlayer(final BlockState state, final Level level, final BlockPos pos, final Player player, final ItemStack toolStack, final boolean willHarvest, final FluidState fluid) {
        if (level.dimension() == ModDimensions.DARK_CITY_RUINS && !level.isClientSide() && level instanceof ServerLevel serverLevel) {
            final CharnRuneSavedData runeData = serverLevel.getDataStorage().computeIfAbsent(CharnRuneSavedData.ID);
            runeData.setRune(getColor(), true);
        }

        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    public Color getColor() {
        return color;
    }

    public enum Color implements StringRepresentable {
        BLUE("blue"),
        GREEN("green"),
        RED("red"),
        YELLOW("yellow");

        private final String name;

        Color(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
