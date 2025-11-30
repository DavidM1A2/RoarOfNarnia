package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(final PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    private static ExtendedModelTemplate withRenderType(final ModelTemplate modelTemplate, final String renderType) {
        return modelTemplate.extend().renderType(renderType).build();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.woodProvider(ModBlocks.WORLD_WOOD_LOG.get()).logWithHorizontal(ModBlocks.WORLD_WOOD_LOG.get()).wood(ModBlocks.WORLD_WOOD.get());
        blockModels.woodProvider(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get()).logWithHorizontal(ModBlocks.STRIPPED_WORLD_WOOD_LOG.get()).wood(ModBlocks.STRIPPED_WORLD_WOOD.get());
        blockModels.createTintedLeaves(ModBlocks.WORLD_WOOD_LEAVES.get(), TexturedModel.LEAVES, 0xFF28DC00);
        createSapling(blockModels, ModBlocks.WORLD_WOOD_SAPLING.get(), BlockModelGenerators.PlantType.TINTED);
        blockModels.family(ModBlocks.WORLD_WOOD_PLANKS.get())
                .generateFor(new BlockFamily.Builder(ModBlocks.WORLD_WOOD_PLANKS.get())
                        .button(ModBlocks.WORLD_WOOD_BUTTON.get())
                        .fence(ModBlocks.WORLD_WOOD_FENCE.get())
                        .fenceGate(ModBlocks.WORLD_WOOD_FENCE_GATE.get())
                        .slab(ModBlocks.WORLD_WOOD_SLAB.get())
                        .stairs(ModBlocks.WORLD_WOOD_STAIR.get())
                        .pressurePlate(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get())
                        .sign(ModBlocks.WORLD_WOOD_WALL_SIGN.get(), ModBlocks.WORLD_WOOD_STANDING_SIGN.get())
                        .getFamily());
        createOrientableTrapdoor(blockModels, ModBlocks.WORLD_WOOD_TRAP_DOOR.get());
        createDoor(blockModels, ModBlocks.WORLD_WOOD_DOOR.get());
        blockModels.createHangingSign(ModBlocks.WORLD_WOOD_PLANKS.get(), ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get());

        blockModels.createTrivialCube(ModBlocks.DARK_CITY_COBBLESTONE.get());
        blockModels.createAxisAlignedPillarBlock(ModBlocks.DARK_CITY_SMOOTH_STONE.get(), TexturedModel.COLUMN);
        createStandaloneSlab(blockModels, ModBlocks.DARK_CITY_SMOOTH_STONE.get(), ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB.get());
        blockModels.createTrivialBlock(ModBlocks.DARK_CITY_WINDOW.get(), TexturedModel.CUBE.updateTemplate(template ->
                template.extend().renderType(RenderType.CUTOUT.getName()).build()));
        createDoor(blockModels, ModBlocks.DARK_CITY_DOOR.get());

        blockModels.family(ModBlocks.DARK_CITY_STONE_BRICKS.get())
                .generateFor(new BlockFamily.Builder(ModBlocks.DARK_CITY_STONE_BRICKS.get())
                        .stairs(ModBlocks.DARK_CITY_STONE_BRICK_STAIRS.get())
                        .slab(ModBlocks.DARK_CITY_STONE_BRICK_SLAB.get())
                        .wall(ModBlocks.DARK_CITY_STONE_BRICK_WALL.get())
                        .trapdoor(ModBlocks.DARK_CITY_STONE_BRICK_TRAPDOOR.get())
                        .getFamily());
        blockModels.createTrivialCube(ModBlocks.MOSSY_DARK_CITY_STONE_BRICKS.get());
        blockModels.createAxisAlignedPillarBlock(ModBlocks.CHISELED_DARK_CITY_STONE_BRICKS.get(), TexturedModel.COLUMN);

        blockModels.createAirLikeBlock(ModBlocks.CHARN_BELL.get(), ModBlocks.CHARN_BELL.asItem());
        blockModels.createAirLikeBlock(ModBlocks.CHARN_IMAGE_HALL_STATUE.get(), ModBlocks.CHARN_IMAGE_HALL_STATUE.asItem());

        blockModels.family(ModBlocks.DARK_CITY_SLATE.get())
                .generateFor(new BlockFamily.Builder(ModBlocks.DARK_CITY_SLATE.get())
                        .stairs(ModBlocks.DARK_CITY_SLATE_STAIRS.get())
                        .slab(ModBlocks.DARK_CITY_SLATE_SLAB.get())
                        .getFamily());

        itemModels.generateFlatItem(ModItems.YELLOW_RING.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_RING.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DEBUG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ASLANS_CHRONICLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SPARKLING_DUST.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.WORLD_WOOD_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WORLD_WOOD_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.ANCIENT_METAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateTrimmableItem(ModItems.ANCIENT_HELMET.get(), EquipmentAssets.DIAMOND, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ModItems.ANCIENT_CHESTPLATE.get(), EquipmentAssets.DIAMOND, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ModItems.ANCIENT_LEGGINGS.get(), EquipmentAssets.DIAMOND, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ModItems.ANCIENT_BOOTS.get(), EquipmentAssets.DIAMOND, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateFlatItem(ModItems.ANCIENT_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateFlatItem(ModBlocks.CHARN_BELL.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.CHARN_IMAGE_HALL_STATUE.asItem(), ModelTemplates.FLAT_ITEM);
    }

    private void createSapling(final BlockModelGenerators blockModels, final Block block, final BlockModelGenerators.PlantType plantType) {
        blockModels.registerSimpleItemModel(block, plantType.createItemModel(blockModels, block));
        final TextureMapping textureMapping = plantType.getTextureMapping(block);
        final MultiVariant multiVariant = BlockModelGenerators.plainVariant(plantType.getCross()
                .extend()
                .renderType(RenderType.cutout().getName())
                .build()
                .create(block, textureMapping, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, multiVariant));
    }

    private void createOrientableTrapdoor(final BlockModelGenerators blockModels, final Block trapdoorBlock) {
        final TextureMapping textureMapping = TextureMapping.defaultTexture(trapdoorBlock);
        final MultiVariant multiVariantTop = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.ORIENTABLE_TRAPDOOR_TOP, RenderType.cutout().getName()).create(trapdoorBlock, textureMapping, blockModels.modelOutput));
        final ResourceLocation resourceLocation = withRenderType(ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM, RenderType.cutout().getName()).create(trapdoorBlock, textureMapping, blockModels.modelOutput);
        final MultiVariant multiVariantOpen = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN, RenderType.cutout().getName()).create(trapdoorBlock, textureMapping, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createOrientableTrapdoor(trapdoorBlock, multiVariantTop, BlockModelGenerators.plainVariant(resourceLocation), multiVariantOpen));
        blockModels.registerSimpleItemModel(trapdoorBlock, resourceLocation);
    }

    private void createDoor(final BlockModelGenerators blockModels, final Block doorBlock) {
        final TextureMapping textureMapping = TextureMapping.door(doorBlock);
        final MultiVariant multiVariantBottomLeft = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_BOTTOM_LEFT, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantBottomLeftOpen = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantBottomRight = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_BOTTOM_RIGHT, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantBottomRightOpen = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopLeft = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_TOP_LEFT, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopLeftOpen = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_TOP_LEFT_OPEN, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopRight = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_TOP_RIGHT, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopRightOpen = BlockModelGenerators.plainVariant(withRenderType(ModelTemplates.DOOR_TOP_RIGHT_OPEN, RenderType.cutout().getName()).create(doorBlock, textureMapping, blockModels.modelOutput));
        blockModels.registerSimpleFlatItemModel(doorBlock.asItem());
        blockModels.blockStateOutput.accept(BlockModelGenerators.createDoor(doorBlock, multiVariantBottomLeft, multiVariantBottomLeftOpen, multiVariantBottomRight, multiVariantBottomRightOpen, multiVariantTopLeft, multiVariantTopLeftOpen, multiVariantTopRight, multiVariantTopRightOpen));
    }

    private void createStandaloneSlab(final BlockModelGenerators blockModels, final Block baseBlock, final Block slabBlock) {
        final TextureMapping baseTextureMapping = TextureMapping.cube(TextureMapping.getBlockTexture(baseBlock, "_top"));
        final MultiVariant slabBottomVariant = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(slabBlock, baseTextureMapping, blockModels.modelOutput));
        final MultiVariant slabTopVariant = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(slabBlock, baseTextureMapping, blockModels.modelOutput));
        final MultiVariant slabDoubleVariant = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN.createWithOverride(slabBlock, "_double", baseTextureMapping, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSlab(slabBlock, slabBottomVariant, slabTopVariant, slabDoubleVariant));
    }
}
