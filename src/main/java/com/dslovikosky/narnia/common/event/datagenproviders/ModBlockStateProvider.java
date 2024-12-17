package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
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
        doorBlock(ModBlocks.WORLD_WOOD_DOOR.get());
        fenceBlock(ModBlocks.WORLD_WOOD_FENCE.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
        fenceGateBlock(ModBlocks.WORLD_WOOD_FENCE_GATE.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
        leavesBlock(ModBlocks.WORLD_WOOD_LEAVES.get());
        saplingBlock(ModBlocks.WORLD_WOOD_SAPLING.get());
        slabBlock(ModBlocks.WORLD_WOOD_SLAB.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
        stairsBlock(ModBlocks.WORLD_WOOD_STAIR.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
        trapdoorBlockWithRenderType(ModBlocks.WORLD_WOOD_TRAP_DOOR.get(), blockTexture(ModBlocks.WORLD_WOOD_TRAP_DOOR.get()), true, RenderType.cutout().name);
        pressurePlateBlock(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
        signBlock(ModBlocks.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_SIGN.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
        hangingSignBlock(ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
    }

    private void woodBlock(final RotatedPillarBlock block, final RotatedPillarBlock logBlock) {
        final ResourceLocation logTexture = blockTexture(logBlock);
        final String blockTexturePath = blockTexture(block).getPath();
        axisBlock(block, models().cubeColumn(blockTexturePath, logTexture, logTexture), models().cubeColumn(blockTexturePath, logTexture, logTexture));
    }

    private void doorBlock(final DoorBlock doorBlock) {
        doorBlockWithRenderType(doorBlock, blockTexture(doorBlock).withSuffix("_lower"), blockTexture(doorBlock).withSuffix("_upper"), RenderType.cutout().name);
    }

    private void leavesBlock(final LeavesBlock leavesBlock) {
        final ResourceLocation leavesTexture = blockTexture(leavesBlock);
        simpleBlock(leavesBlock, models().leaves(leavesTexture.getPath(), leavesTexture).renderType(RenderType.cutoutMipped().name));
    }

    private void saplingBlock(final SaplingBlock saplingBlock) {
        final ResourceLocation leavesTexture = blockTexture(saplingBlock);
        simpleBlock(saplingBlock, models().cross(leavesTexture.getPath(), leavesTexture).renderType(RenderType.cutout().name));
    }

    private void slabBlock(final SlabBlock slabBlock, final ResourceLocation slabTexture) {
        slabBlock(slabBlock, slabTexture, slabTexture);
    }

    private void hangingSignBlock(final WallHangingSignBlock wallHangingSignBlock, final CeilingHangingSignBlock ceilingHangingSignBlock, final ResourceLocation hangingSignTexture) {
        final ResourceLocation wallHangingSignTexture = blockTexture(wallHangingSignBlock);
        final ResourceLocation ceilingHangingSignTexture = blockTexture(ceilingHangingSignBlock);
        simpleBlock(wallHangingSignBlock, models().sign(wallHangingSignTexture.getPath(), hangingSignTexture));
        simpleBlock(ceilingHangingSignBlock, models().sign(ceilingHangingSignTexture.getPath(), hangingSignTexture));
    }
}
