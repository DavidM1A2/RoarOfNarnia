package com.dslovikosky.narnia.common.block;

import com.dslovikosky.narnia.common.block_entity.RuneBlockEntity;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
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

    public Color getColor() {
        return color;
    }

    public enum Color {
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
    }
}
