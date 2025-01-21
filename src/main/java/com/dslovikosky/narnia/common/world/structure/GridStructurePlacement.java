package com.dslovikosky.narnia.common.world.structure;

import com.dslovikosky.narnia.common.constants.ModStructurePlacementTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GridStructurePlacement extends StructurePlacement {
    public static final MapCodec<GridStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("x_frequency").forGetter(GridStructurePlacement::getXFrequency),
            Codec.INT.fieldOf("z_frequency").forGetter(GridStructurePlacement::getZFrequency)
    ).apply(instance, GridStructurePlacement::new));

    private final int xFrequency;
    private final int zFrequency;

    public GridStructurePlacement(final int xFrequency, final int zFrequency) {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 0f, 0, Optional.empty());
        this.xFrequency = xFrequency;
        this.zFrequency = zFrequency;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState pStructureState, int pX, int pZ) {
        return pX % xFrequency == 0 && pZ % zFrequency == 0;
    }

    @Override
    public boolean isStructureChunk(ChunkGeneratorStructureState pStructureState, int pX, int pZ) {
        return isPlacementChunk(pStructureState, pX, pZ);
    }

    @Override
    public StructurePlacementType<?> type() {
        return ModStructurePlacementTypes.GRID.get();
    }

    public int getXFrequency() {
        return xFrequency;
    }

    public int getZFrequency() {
        return zFrequency;
    }
}
