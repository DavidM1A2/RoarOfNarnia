package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModEnglishLanguageProvider extends LanguageProvider {
    public ModEnglishLanguageProvider(final PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.YELLOW_RING, "Yellow Ring");
        addItem(ModItems.GREEN_RING, "Green Ring");

        addBlock(ModBlocks.WORLD_WOOD, "World Wood");
        addBlock(ModBlocks.WORLD_WOOD_LOG, "World Wood Log");
        addBlock(ModBlocks.STRIPPED_WORLD_WOOD, "Stripped World Wood");
        addBlock(ModBlocks.STRIPPED_WORLD_WOOD_LOG, "Stripped World Wood Log");
        addBlock(ModBlocks.WORLD_WOOD_PLANKS, "World Wood Planks");
        addBlock(ModBlocks.WORLD_WOOD_BUTTON, "World Wood Button");
        addBlock(ModBlocks.WORLD_WOOD_DOOR, "World Wood Door");
        addBlock(ModBlocks.WORLD_WOOD_FENCE, "World Wood Fence");
        addBlock(ModBlocks.WORLD_WOOD_FENCE_GATE, "World Wood Fence Gate");
        addBlock(ModBlocks.WORLD_WOOD_LEAVES, "World Wood Leaves");
        addBlock(ModBlocks.WORLD_WOOD_SAPLING, "World Wood Sapling");
        addBlock(ModBlocks.WORLD_WOOD_SLAB, "World Wood Slab");
        addBlock(ModBlocks.WORLD_WOOD_STAIR, "World Wood Stair");
        addBlock(ModBlocks.WORLD_WOOD_TRAP_DOOR, "World Wood Trapdoor");
    }
}
