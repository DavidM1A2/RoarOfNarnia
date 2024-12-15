package com.dslovikosky.narnia.common.constants;

import com.dslovikosky.narnia.common.block.worldwood.WorldWoodLog;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    public static final Supplier<WorldWoodLog> WORLD_WOOD_LOG = BLOCKS.register("world_wood_log", WorldWoodLog::new);
}
