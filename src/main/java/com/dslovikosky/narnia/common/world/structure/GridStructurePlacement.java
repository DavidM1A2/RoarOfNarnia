package com.dslovikosky.narnia.common.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record GridStructurePlacement(int xFrequency, int zFrequency) implements StructurePlacement {
    public static final MapCodec<GridStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("x_frequency").forGetter(GridStructurePlacement::xFrequency),
            Codec.INT.fieldOf("z_frequency").forGetter(GridStructurePlacement::zFrequency)
    ).apply(instance, GridStructurePlacement::new));

    @Override
    public boolean isStructureChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        return x % xFrequency == 0 && z % zFrequency == 0;
    }

    @Override
    public MapCodec<GridStructurePlacement> codec() {
        return CODEC;
    }
}
