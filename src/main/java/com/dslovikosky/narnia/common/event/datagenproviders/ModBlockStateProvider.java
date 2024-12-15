package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        woodBlock(ModBlocks.WORLD_WOOD.get(), ModBlocks.WORLD_WOOD_LOG.get());
        woodBlock(ModBlocks.STRIPPED_WORLD_WOOD.get(), ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
        logBlock(ModBlocks.WORLD_WOOD_LOG.get());
        logBlock(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
        simpleBlock(ModBlocks.WORLD_WOOD_PLANKS.get());
        buttonBlock(ModBlocks.WORLD_WOOD_BUTTON.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
    }

    private void woodBlock(final RotatedPillarBlock block, final RotatedPillarBlock logBlock) {
        final ResourceLocation logTexture = blockTexture(logBlock);
        final String blockTexturePath = blockTexture(block).getPath();
        axisBlock(block, models().cubeColumn(blockTexturePath, logTexture, logTexture), models().cubeColumn(blockTexturePath, logTexture, logTexture));
    }
}
