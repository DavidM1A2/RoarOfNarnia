package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.world.structure.UncleAndrewsHouseStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<UncleAndrewsHouseStructure>> UNCLE_ANDREWS_HOUSE = STRUCTURE_TYPES.register("uncle_andrews_house", id -> () -> UncleAndrewsHouseStructure.CODEC);
}
