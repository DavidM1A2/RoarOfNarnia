package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block.worldwood.StrippedWorldWoodBlock;
import com.dslovikosky.narnia.common.block.worldwood.StrippedWorldWoodLogBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodButtonBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodDoorBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodFenceBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodFenceGateBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodLeavesBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodLogBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodPlanksBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodPressurePlateBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodSaplingBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodSlabBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodStairBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodTrapDoorBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    public static final Supplier<WorldWoodBlock> WORLD_WOOD = BLOCKS.register("world_wood", WorldWoodBlock::new);
    public static final Supplier<StrippedWorldWoodBlock> STRIPPED_WORLD_WOOD = BLOCKS.register("stripped_world_wood", StrippedWorldWoodBlock::new);
    public static final Supplier<WorldWoodLogBlock> WORLD_WOOD_LOG = BLOCKS.register("world_wood_log", WorldWoodLogBlock::new);
    public static final Supplier<StrippedWorldWoodLogBlock> STRIPPED_WORLD_WOOD_LOG = BLOCKS.register("stripped_world_wood_log", StrippedWorldWoodLogBlock::new);
    public static final Supplier<WorldWoodPlanksBlock> WORLD_WOOD_PLANKS = BLOCKS.register("world_wood_planks", WorldWoodPlanksBlock::new);
    public static final Supplier<WorldWoodDoorBlock> WORLD_WOOD_DOOR = BLOCKS.register("world_wood_door", WorldWoodDoorBlock::new);
    public static final Supplier<WorldWoodFenceBlock> WORLD_WOOD_FENCE = BLOCKS.register("world_wood_fence", WorldWoodFenceBlock::new);
    public static final Supplier<WorldWoodFenceGateBlock> WORLD_WOOD_FENCE_GATE = BLOCKS.register("world_wood_fence_gate", WorldWoodFenceGateBlock::new);
    public static final Supplier<WorldWoodStairBlock> WORLD_WOOD_STAIR = BLOCKS.register("world_wood_stair", WorldWoodStairBlock::new);
    public static final Supplier<WorldWoodPressurePlateBlock> WORLD_WOOD_PRESSURE_PLATE = BLOCKS.register("world_wood_pressure_plate", WorldWoodPressurePlateBlock::new);
    public static final Supplier<WorldWoodButtonBlock> WORLD_WOOD_BUTTON = BLOCKS.register("world_wood_button", WorldWoodButtonBlock::new);
    public static final Supplier<WorldWoodLeavesBlock> WORLD_WOOD_LEAVES = BLOCKS.register("world_wood_leaves", WorldWoodLeavesBlock::new);
    public static final Supplier<WorldWoodSaplingBlock> WORLD_WOOD_SAPLING = BLOCKS.register("world_wood_sapling", WorldWoodSaplingBlock::new);
    public static final Supplier<WorldWoodSlabBlock> WORLD_WOOD_SLAB = BLOCKS.register("world_wood_slab", WorldWoodSlabBlock::new);
    public static final Supplier<WorldWoodTrapDoorBlock> WORLD_WOOD_TRAP_DOOR = BLOCKS.register("world_wood_trap_door", WorldWoodTrapDoorBlock::new);
}
