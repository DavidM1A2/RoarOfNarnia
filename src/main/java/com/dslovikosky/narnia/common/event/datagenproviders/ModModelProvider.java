package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

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
        blockModels.createTrivialCube(ModBlocks.POSITIONAL_MARKER.get());

        blockModels.woodProvider(ModBlocks.WORLD_WOOD_LOG.get()).logWithHorizontal(ModBlocks.WORLD_WOOD_LOG.get()).wood(ModBlocks.WORLD_WOOD.get());
        blockModels.woodProvider(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get()).logWithHorizontal(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get()).wood(ModBlocks.STRIPPED_WORLD_WOOD.get());
        blockModels.createTintedLeaves(ModBlocks.WORLD_WOOD_LEAVES.get(), TexturedModel.LEAVES, -12012264);
        blockModels.createCrossBlockWithDefaultItem(ModBlocks.WORLD_WOOD_SAPLING.get(), BlockModelGenerators.PlantType.TINTED);
        blockModels.family(ModBlocks.WORLD_WOOD_PLANKS.get()).generateFor(new BlockFamily.Builder(ModBlocks.WORLD_WOOD_PLANKS.get())
                .button(ModBlocks.WORLD_WOOD_BUTTON.get())
                .door(ModBlocks.WORLD_WOOD_DOOR.get())
                .fence(ModBlocks.WORLD_WOOD_FENCE.get())
                .fenceGate(ModBlocks.WORLD_WOOD_FENCE_GATE.get())
                .slab(ModBlocks.WORLD_WOOD_SLAB.get())
                .stairs(ModBlocks.WORLD_WOOD_STAIR.get())
                .pressurePlate(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get())
                .sign(ModBlocks.WORLD_WOOD_WALL_SIGN.get(), ModBlocks.WORLD_WOOD_STANDING_SIGN.get())
                .trapdoor(ModBlocks.WORLD_WOOD_TRAP_DOOR.get())
                .getFamily());
        blockModels.createHangingSign(ModBlocks.WORLD_WOOD_PLANKS.get(), ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get());

        itemModels.generateFlatItem(ModItems.YELLOW_RING.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_RING.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DEBUG.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.WORLD_WOOD_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WORLD_WOOD_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);
    }
}
