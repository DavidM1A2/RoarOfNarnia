package com.dslovikosky.narnia.common.world.structure;

import com.dslovikosky.narnia.common.constants.ModSchematics;
import com.dslovikosky.narnia.common.constants.ModStructureTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class UncleAndrewsHouseStructure extends Structure {
    public static final MapCodec<UncleAndrewsHouseStructure> CODEC = simpleCodec(UncleAndrewsHouseStructure::new);

    protected UncleAndrewsHouseStructure(final StructureSettings pSettings) {
        super(pSettings);
    }

    @Override
    protected @NotNull Optional<GenerationStub> findGenerationPoint(final GenerationContext pContext) {
        final ChunkPos chunkPos = pContext.chunkPos();
        final int xPos = chunkPos.getMinBlockX();
        final int zPos = chunkPos.getMinBlockZ();
        final BlockPos blockPos = new BlockPos(xPos, 64, zPos);

        return Optional.of(new GenerationStub(blockPos, builder ->
                builder.addPiece(new SchematicStructurePiece(blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSchematics.UNCLE_ANDREWS_HOUSE.get(), Direction.NORTH))));
    }

    @Override
    public @NotNull StructureType<?> type() {
        return ModStructureTypes.UNCLE_ANDREWS_HOUSE.get();
    }
}
