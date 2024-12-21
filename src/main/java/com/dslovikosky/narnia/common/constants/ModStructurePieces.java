package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.world.structure.SchematicStructurePiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructurePieces {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Constants.MOD_ID);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> SCHEMATIC = STRUCTURE_PIECES.register("schematic", id -> SchematicStructurePiece::new);
}
