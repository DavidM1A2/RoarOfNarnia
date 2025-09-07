package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

public class ModModelProvider extends ModelProvider {
    //    private final Set<Block> blocksWithCustomItemRenderer = Set.of(ModBlocks.RING_BOX.get());
    private final Map<String, ResourceLocation> materialToTexture = new HashMap<>();

    public ModModelProvider(final PackOutput output) {
        super(output, Constants.MOD_ID);
//        materialToTexture.put("world_wood", modLocation(BLOCK_FOLDER + "/world_wood_planks"));
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
//        final ModelFile emptyModel = models().getExistingFile(mcLoc("block/air"));
//
//        simpleBlock(ModBlocks.POSITIONAL_MARKER.get());
//        axisBlock(ModBlocks.RING_BOX.get(), emptyModel, emptyModel);
//
//        woodBlock(ModBlocks.WORLD_WOOD.get(), ModBlocks.WORLD_WOOD_LOG.get());
//        woodBlock(ModBlocks.STRIPPED_WORLD_WOOD.get(), ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
//        logBlock(ModBlocks.WORLD_WOOD_LOG.get());
//        logBlock(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get());
//        simpleBlock(ModBlocks.WORLD_WOOD_PLANKS.get());
//        buttonBlock(ModBlocks.WORLD_WOOD_BUTTON.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
//        doorBlock(ModBlocks.WORLD_WOOD_DOOR.get());
//        fenceBlock(ModBlocks.WORLD_WOOD_FENCE.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
//        fenceGateBlock(ModBlocks.WORLD_WOOD_FENCE_GATE.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
//        leavesBlock(ModBlocks.WORLD_WOOD_LEAVES.get());
//        saplingBlock(ModBlocks.WORLD_WOOD_SAPLING.get());
//        slabBlock(ModBlocks.WORLD_WOOD_SLAB.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
//        stairsBlock(ModBlocks.WORLD_WOOD_STAIR.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
//        trapdoorBlockWithRenderType(ModBlocks.WORLD_WOOD_TRAP_DOOR.get(), blockTexture(ModBlocks.WORLD_WOOD_TRAP_DOOR.get()), true, RenderType.cutout().name);
//        pressurePlateBlock(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
//        signBlock(ModBlocks.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_SIGN.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));
//        hangingSignBlock(ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get(), blockTexture(ModBlocks.WORLD_WOOD_PLANKS.get()));

        ModItems.ITEMS.getEntries().forEach(deferredItem -> {
            final Item item = deferredItem.get();
//            if (item instanceof BlockItem blockItem) {
//                if (blocksWithCustomItemRenderer.contains(blockItem.getBlock())) {
//                    itemModels.itemModelOutput.accept(item, ItemModelUtils.specialModel(deferredItem.getId(), ));
//                    itemModels.getBuilder(item.toString())
//                            .parent(new ModelFile.UncheckedModelFile("builtin/entity"));
//                } else if (blockItem.getBlock() instanceof ButtonBlock) {
//                    buttonInventory(deferredItem.getRegisteredName(), determineTexture(deferredItem));
//                } else if (blockItem.getBlock() instanceof DoorBlock) {
//                    itemModels.generate
//                    basicItem(item);
//                } else if (blockItem.getBlock() instanceof SaplingBlock) {
//                    getBuilder(item.toString())
//                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
//                            .texture("layer0", modLoc(BLOCK_FOLDER + "/" + deferredItem.getId().getPath()));
//                } else if (blockItem.getBlock() instanceof FenceBlock) {
//                    fenceInventory(deferredItem.getRegisteredName(), determineTexture(deferredItem));
//                } else if (blockItem.getBlock() instanceof FenceGateBlock) {
//                    fenceGate(deferredItem.getRegisteredName(), determineTexture(deferredItem));
//                } else if (blockItem.getBlock() instanceof SlabBlock) {
//                    slab(deferredItem.getRegisteredName(), determineTexture(deferredItem), determineTexture(deferredItem), determineTexture(deferredItem));
//                } else if (blockItem.getBlock() instanceof StairBlock) {
//                    stairs(deferredItem.getRegisteredName(), determineTexture(deferredItem), determineTexture(deferredItem), determineTexture(deferredItem));
//                } else if (blockItem.getBlock() instanceof TrapDoorBlock) {
//                    trapdoorBottom(deferredItem.getRegisteredName(), modLoc(BLOCK_FOLDER + "/" + deferredItem.getId().getPath()));
//                } else if (blockItem.getBlock() instanceof PressurePlateBlock) {
//                    pressurePlate(deferredItem.getRegisteredName(), determineTexture(deferredItem));
//                } else if (item instanceof SignItem || item instanceof HangingSignItem) {
//                    basicItem(item);
//                } else {
//                    withExistingParent(deferredItem.getRegisteredName(), modLoc(BLOCK_FOLDER + "/" + deferredItem.getId().getPath()));
//                }
//            } else {
//                basicItem(item);
//            }
        });
    }

    private ResourceLocation determineTexture(final DeferredHolder<Item, ? extends Item> item) {
        final String itemName = item.getId().getPath();
        for (final Map.Entry<String, ResourceLocation> entry : materialToTexture.entrySet()) {
            if (itemName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("No material found for " + itemName);
    }

//    private void woodBlock(final RotatedPillarBlock block, final RotatedPillarBlock logBlock) {
//        final ResourceLocation logTexture = blockTexture(logBlock);
//        final String blockTexturePath = blockTexture(block).getPath();
//        axisBlock(block, models().cubeColumn(blockTexturePath, logTexture, logTexture), models().cubeColumn(blockTexturePath, logTexture, logTexture));
//    }
//
//    private void doorBlock(final DoorBlock doorBlock) {
//        doorBlockWithRenderType(doorBlock, blockTexture(doorBlock).withSuffix("_lower"), blockTexture(doorBlock).withSuffix("_upper"), RenderType.cutout().name);
//    }
//
//    private void leavesBlock(final LeavesBlock leavesBlock) {
//        final ResourceLocation leavesTexture = blockTexture(leavesBlock);
//        simpleBlock(leavesBlock, models().leaves(leavesTexture.getPath(), leavesTexture).renderType(RenderType.cutoutMipped().name));
//    }
//
//    private void saplingBlock(final SaplingBlock saplingBlock) {
//        final ResourceLocation leavesTexture = blockTexture(saplingBlock);
//        simpleBlock(saplingBlock, models().cross(leavesTexture.getPath(), leavesTexture).renderType(RenderType.cutout().name));
//    }
//
//    private void slabBlock(final SlabBlock slabBlock, final ResourceLocation slabTexture) {
//        slabBlock(slabBlock, slabTexture, slabTexture);
//    }
//
//    private void hangingSignBlock(final WallHangingSignBlock wallHangingSignBlock, final CeilingHangingSignBlock ceilingHangingSignBlock, final ResourceLocation hangingSignTexture) {
//        final ResourceLocation wallHangingSignTexture = blockTexture(wallHangingSignBlock);
//        final ResourceLocation ceilingHangingSignTexture = blockTexture(ceilingHangingSignBlock);
//        simpleBlock(wallHangingSignBlock, models().sign(wallHangingSignTexture.getPath(), hangingSignTexture));
//        simpleBlock(ceilingHangingSignBlock, models().sign(ceilingHangingSignTexture.getPath(), hangingSignTexture));
//    }
}
