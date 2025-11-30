package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block.CharnBellBlock;
import com.dslovikosky.narnia.common.block.CharnImageHallStatueBlock;
import com.dslovikosky.narnia.common.block.RuneBlock;
import com.dslovikosky.narnia.common.block.dark_city.ChiseledDarkCityStoneBricksBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityCobblestoneBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityDoorBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCitySlateBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCitySlateSlabBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCitySlateStairsBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCitySmoothStoneBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCitySmoothStoneSlabBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityStoneBrickSlabBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityStoneBrickStairsBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityStoneBrickTrapDoorBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityStoneBrickWallBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityStoneBricksBlock;
import com.dslovikosky.narnia.common.block.dark_city.DarkCityWindowBlock;
import com.dslovikosky.narnia.common.block.dark_city.MossyDarkCityStoneBricksBlock;
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

    public static final DeferredBlock<DarkCityCobblestoneBlock> DARK_CITY_COBBLESTONE = BLOCKS.register("dark_city_cobblestone", DarkCityCobblestoneBlock::new);
    public static final DeferredBlock<DarkCityDoorBlock> DARK_CITY_DOOR = BLOCKS.register("dark_city_door", DarkCityDoorBlock::new);
    public static final DeferredBlock<DarkCitySmoothStoneBlock> DARK_CITY_SMOOTH_STONE = BLOCKS.register("dark_city_smooth_stone", DarkCitySmoothStoneBlock::new);
    public static final DeferredBlock<DarkCitySmoothStoneSlabBlock> DARK_CITY_SMOOTH_STONE_SLAB = BLOCKS.register("dark_city_smooth_stone_slab", DarkCitySmoothStoneSlabBlock::new);
    public static final DeferredBlock<DarkCityWindowBlock> DARK_CITY_WINDOW = BLOCKS.register("dark_city_window", DarkCityWindowBlock::new);

    public static final DeferredBlock<DarkCityStoneBricksBlock> DARK_CITY_STONE_BRICKS = BLOCKS.register("dark_city_stone_bricks", DarkCityStoneBricksBlock::new);
    public static final DeferredBlock<DarkCityStoneBrickStairsBlock> DARK_CITY_STONE_BRICK_STAIRS = BLOCKS.register("dark_city_stone_brick_stairs", DarkCityStoneBrickStairsBlock::new);
    public static final DeferredBlock<DarkCityStoneBrickWallBlock> DARK_CITY_STONE_BRICK_WALL = BLOCKS.register("dark_city_stone_brick_wall", DarkCityStoneBrickWallBlock::new);
    public static final DeferredBlock<DarkCityStoneBrickSlabBlock> DARK_CITY_STONE_BRICK_SLAB = BLOCKS.register("dark_city_stone_brick_slab", DarkCityStoneBrickSlabBlock::new);
    public static final DeferredBlock<MossyDarkCityStoneBricksBlock> MOSSY_DARK_CITY_STONE_BRICKS = BLOCKS.register("mossy_dark_city_stone_bricks", MossyDarkCityStoneBricksBlock::new);
    public static final DeferredBlock<ChiseledDarkCityStoneBricksBlock> CHISELED_DARK_CITY_STONE_BRICKS = BLOCKS.register("chiseled_dark_city_stone_bricks", ChiseledDarkCityStoneBricksBlock::new);
    public static final DeferredBlock<DarkCityStoneBrickTrapDoorBlock> DARK_CITY_STONE_BRICK_TRAPDOOR = BLOCKS.register("dark_city_stone_brick_trapdoor", DarkCityStoneBrickTrapDoorBlock::new);

    public static final DeferredBlock<DarkCitySlateBlock> DARK_CITY_SLATE = BLOCKS.register("dark_city_slate", DarkCitySlateBlock::new);
    public static final DeferredBlock<DarkCitySlateStairsBlock> DARK_CITY_SLATE_STAIRS = BLOCKS.register("dark_city_slate_stairs", DarkCitySlateStairsBlock::new);
    public static final DeferredBlock<DarkCitySlateSlabBlock> DARK_CITY_SLATE_SLAB = BLOCKS.register("dark_city_slate_slab", DarkCitySlateSlabBlock::new);

    public static final DeferredBlock<CharnBellBlock> CHARN_BELL = BLOCKS.register("charn_bell", CharnBellBlock::new);
    public static final DeferredBlock<CharnImageHallStatueBlock> CHARN_IMAGE_HALL_STATUE = BLOCKS.register("charn_image_hall_statue", CharnImageHallStatueBlock::new);
    public static final DeferredBlock<RuneBlock> BLUE_RUNE = BLOCKS.register("blue_rune", it -> new RuneBlock(RuneBlock.Color.BLUE));
    public static final DeferredBlock<RuneBlock> GREEN_RUNE = BLOCKS.register("green_rune", it -> new RuneBlock(RuneBlock.Color.GREEN));
    public static final DeferredBlock<RuneBlock> RED_RUNE = BLOCKS.register("red_rune", it -> new RuneBlock(RuneBlock.Color.RED));
    public static final DeferredBlock<RuneBlock> YELLOW_RUNE = BLOCKS.register("yellow_rune", it -> new RuneBlock(RuneBlock.Color.YELLOW));
}
