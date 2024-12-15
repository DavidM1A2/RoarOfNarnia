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
    }
}
