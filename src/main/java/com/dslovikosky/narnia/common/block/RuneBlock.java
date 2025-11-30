package com.dslovikosky.narnia.common.block;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class RuneBlock extends Block {
    public RuneBlock(final Color color) {
        super(Properties.of()
                .noOcclusion()
                .mapColor(MapColor.SAND)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(3.0F, 9.0F)
                .isViewBlocking((ignoredA, ignoredB, ignoredC) -> false)
                .setId(ResourceKey.create(Registries.BLOCK, Constants.modLocation(color.getName() + "_rune"))));
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
