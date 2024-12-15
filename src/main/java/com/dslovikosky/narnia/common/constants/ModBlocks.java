package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block.worldwood.StrippedWorldWoodBlock;
import com.dslovikosky.narnia.common.block.worldwood.StrippedWorldWoodLogBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodButtonBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodLogBlock;
import com.dslovikosky.narnia.common.block.worldwood.WorldWoodPlanksBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    public static final Supplier<WorldWoodBlock> WORLD_WOOD = BLOCKS.register("world_wood", WorldWoodBlock::new);
    public static final Supplier<StrippedWorldWoodBlock> STRIPPED_WORLD_WOOD = BLOCKS.register("stripped_world_wood", StrippedWorldWoodBlock::new);
    public static final Supplier<WorldWoodLogBlock> WORLD_WOOD_LOG = BLOCKS.register("world_wood_log", WorldWoodLogBlock::new);
    public static final Supplier<StrippedWorldWoodLogBlock> STRIPPED_WORLD_WOOD_LOG = BLOCKS.register("stripped_world_wood_log", StrippedWorldWoodLogBlock::new);
    public static final Supplier<WorldWoodPlanksBlock> WORLD_WOOD_PLANKS = BLOCKS.register("world_wood_planks", WorldWoodPlanksBlock::new);
    public static final Supplier<WorldWoodButtonBlock> WORLD_WOOD_BUTTON = BLOCKS.register("world_wood_button", WorldWoodButtonBlock::new);
}
