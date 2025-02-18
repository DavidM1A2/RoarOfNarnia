package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block.PositionalMarkerBlock;
import com.dslovikosky.narnia.common.block.RingBoxBlock;
import com.dslovikosky.narnia.common.block.worldwood.StrippedWorldWoodBlock;
import com.dslovikosky.narnia.common.block.worldwood.StrippedWorldWoodLogBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodButtonBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodCeilingHangingSignBlock;
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
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodStandingSignBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodTrapDoorBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodWallHangingSignBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodWallSignBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    public static final DeferredBlock<PositionalMarkerBlock> POSITIONAL_MARKER = BLOCKS.register("positional_marker", PositionalMarkerBlock::new);
    public static final DeferredBlock<RingBoxBlock> RING_BOX = BLOCKS.register("ring_box", RingBoxBlock::new);

    public static final DeferredBlock<WorldWoodBlock> WORLD_WOOD = BLOCKS.register("world_wood", WorldWoodBlock::new);
    public static final DeferredBlock<StrippedWorldWoodBlock> STRIPPED_WORLD_WOOD = BLOCKS.register("stripped_world_wood", StrippedWorldWoodBlock::new);
    public static final DeferredBlock<WorldWoodLogBlock> WORLD_WOOD_LOG = BLOCKS.register("world_wood_log", WorldWoodLogBlock::new);
    public static final DeferredBlock<StrippedWorldWoodLogBlock> STRIPPED_WORLD_WOOD_LOG = BLOCKS.register("stripped_world_wood_log", StrippedWorldWoodLogBlock::new);
    public static final DeferredBlock<WorldWoodPlanksBlock> WORLD_WOOD_PLANKS = BLOCKS.register("world_wood_planks", WorldWoodPlanksBlock::new);
    public static final DeferredBlock<WorldWoodDoorBlock> WORLD_WOOD_DOOR = BLOCKS.register("world_wood_door", WorldWoodDoorBlock::new);
    public static final DeferredBlock<WorldWoodFenceBlock> WORLD_WOOD_FENCE = BLOCKS.register("world_wood_fence", WorldWoodFenceBlock::new);
    public static final DeferredBlock<WorldWoodFenceGateBlock> WORLD_WOOD_FENCE_GATE = BLOCKS.register("world_wood_fence_gate", WorldWoodFenceGateBlock::new);
    public static final DeferredBlock<WorldWoodStairBlock> WORLD_WOOD_STAIR = BLOCKS.register("world_wood_stair", WorldWoodStairBlock::new);
    public static final DeferredBlock<WorldWoodPressurePlateBlock> WORLD_WOOD_PRESSURE_PLATE = BLOCKS.register("world_wood_pressure_plate", WorldWoodPressurePlateBlock::new);
    public static final DeferredBlock<WorldWoodStandingSignBlock> WORLD_WOOD_STANDING_SIGN = BLOCKS.register("world_wood_standing_sign", WorldWoodStandingSignBlock::new);
    public static final DeferredBlock<WorldWoodWallSignBlock> WORLD_WOOD_WALL_SIGN = BLOCKS.register("world_wood_wall_sign", WorldWoodWallSignBlock::new);
    public static final DeferredBlock<WorldWoodWallHangingSignBlock> WORLD_WOOD_WALL_HANGING_SIGN = BLOCKS.register("world_wood_wall_hanging_sign", WorldWoodWallHangingSignBlock::new);
    public static final DeferredBlock<WorldWoodCeilingHangingSignBlock> WORLD_WOOD_CEILING_HANGING_SIGN = BLOCKS.register("world_wood_ceiling_hanging_sign", WorldWoodCeilingHangingSignBlock::new);
    public static final Set<DeferredBlock<? extends Block>> SPECIAL_ITEM_BLOCKS = Set.of(WORLD_WOOD_STANDING_SIGN, WORLD_WOOD_WALL_SIGN, WORLD_WOOD_WALL_HANGING_SIGN, WORLD_WOOD_CEILING_HANGING_SIGN);
    public static final DeferredBlock<WorldWoodButtonBlock> WORLD_WOOD_BUTTON = BLOCKS.register("world_wood_button", WorldWoodButtonBlock::new);
    public static final DeferredBlock<WorldWoodLeavesBlock> WORLD_WOOD_LEAVES = BLOCKS.register("world_wood_leaves", WorldWoodLeavesBlock::new);
    public static final DeferredBlock<WorldWoodSaplingBlock> WORLD_WOOD_SAPLING = BLOCKS.register("world_wood_sapling", WorldWoodSaplingBlock::new);
    public static final DeferredBlock<WorldWoodSlabBlock> WORLD_WOOD_SLAB = BLOCKS.register("world_wood_slab", WorldWoodSlabBlock::new);
    public static final DeferredBlock<WorldWoodTrapDoorBlock> WORLD_WOOD_TRAP_DOOR = BLOCKS.register("world_wood_trap_door", WorldWoodTrapDoorBlock::new);
}
