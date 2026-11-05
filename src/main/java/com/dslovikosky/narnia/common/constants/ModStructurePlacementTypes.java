package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.world.structure.GridStructurePlacement;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructurePlacementTypes {
    public static final DeferredRegister<MapCodec<? extends StructurePlacement>> STRUCTURE_PLACEMENTS = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, Constants.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends StructurePlacement>, MapCodec<GridStructurePlacement>> GRID = STRUCTURE_PLACEMENTS.register("grid", id -> GridStructurePlacement.CODEC);
}
