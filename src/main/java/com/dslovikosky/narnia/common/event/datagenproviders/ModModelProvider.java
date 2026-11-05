package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class ModModelProvider extends ModelProvider {
    // Ancient armour takes the darker diamond trim overlay, the same substitution vanilla diamond armour uses
    private static final Map<TrimMaterials.Palette, TrimMaterials.Palette> ANCIENT_TRIM_PALETTES =
            Map.of(TrimMaterials.Palette.DIAMOND, TrimMaterials.Palette.DIAMOND_DARKER);

    public ModModelProvider(final PackOutput output) {
        super(output, Constants.MOD_ID);
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
                        .sign(ModBlocks.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_SIGN.get())
                        // customHangingSign uses the family's base block (planks) as the particle donor
                        .customHangingSign(ModBlocks.WORLD_WOOD_CEILING_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_WALL_HANGING_SIGN.get())
                        .getFamily());
        createOrientableTrapdoor(blockModels, ModBlocks.WORLD_WOOD_TRAP_DOOR.get());
        createDoor(blockModels, ModBlocks.WORLD_WOOD_DOOR.get());

        blockModels.createTrivialCube(ModBlocks.DARK_CITY_COBBLESTONE.get());
        blockModels.createAxisAlignedPillarBlock(ModBlocks.DARK_CITY_SMOOTH_STONE.get(), TexturedModel.COLUMN);
        createStandaloneSlab(blockModels, ModBlocks.DARK_CITY_SMOOTH_STONE.get(), ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB.get());
        createCutoutBlock(blockModels, ModBlocks.DARK_CITY_WINDOW.get());
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
        blockModels.createAirLikeBlock(ModBlocks.BLUE_RUNE.get(), ModBlocks.BLUE_RUNE.asItem());
        blockModels.createAirLikeBlock(ModBlocks.GREEN_RUNE.get(), ModBlocks.GREEN_RUNE.asItem());
        blockModels.createAirLikeBlock(ModBlocks.RED_RUNE.get(), ModBlocks.RED_RUNE.asItem());
        blockModels.createAirLikeBlock(ModBlocks.YELLOW_RUNE.get(), ModBlocks.YELLOW_RUNE.asItem());

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
        itemModels.generateTrimmableItem(ModItems.ANCIENT_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET, false, ANCIENT_TRIM_PALETTES);
        itemModels.generateTrimmableItem(ModItems.ANCIENT_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false, ANCIENT_TRIM_PALETTES);
        itemModels.generateTrimmableItem(ModItems.ANCIENT_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false, ANCIENT_TRIM_PALETTES);
        itemModels.generateTrimmableItem(ModItems.ANCIENT_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS, false, ANCIENT_TRIM_PALETTES);
        itemModels.generateFlatItem(ModItems.ANCIENT_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateFlatItem(ModBlocks.CHARN_BELL.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.CHARN_IMAGE_HALL_STATUE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.BLUE_RUNE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.GREEN_RUNE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.RED_RUNE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModBlocks.YELLOW_RUNE.asItem(), ModelTemplates.FLAT_ITEM);
    }

    private void createSapling(final BlockModelGenerators blockModels, final Block block, final BlockModelGenerators.PlantType plantType) {
        blockModels.registerSimpleItemModel(block, plantType.createItemModelUsingBlockTexture(blockModels, block));
        final TextureMapping textureMapping = plantType.getTextureMapping(block);
        final MultiVariant multiVariant = BlockModelGenerators.plainVariant(plantType.getCross()
                .create(block, textureMapping, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, multiVariant));
    }

    private void createOrientableTrapdoor(final BlockModelGenerators blockModels, final Block trapdoorBlock) {
        final TextureMapping textureMapping = TextureMapping.defaultTexture(trapdoorBlock);
        final MultiVariant multiVariantTop = BlockModelGenerators.plainVariant(ModelTemplates.ORIENTABLE_TRAPDOOR_TOP.create(trapdoorBlock, textureMapping, blockModels.modelOutput));
        final Identifier resourceLocation = ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.create(trapdoorBlock, textureMapping, blockModels.modelOutput);
        final MultiVariant multiVariantOpen = BlockModelGenerators.plainVariant(ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN.create(trapdoorBlock, textureMapping, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createOrientableTrapdoor(trapdoorBlock, multiVariantTop, BlockModelGenerators.plainVariant(resourceLocation), multiVariantOpen));
        blockModels.registerSimpleItemModel(trapdoorBlock, resourceLocation);
    }

    private void createDoor(final BlockModelGenerators blockModels, final Block doorBlock) {
        final TextureMapping textureMapping = TextureMapping.door(doorBlock);
        final MultiVariant multiVariantBottomLeft = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantBottomLeftOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantBottomRight = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantBottomRightOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopLeft = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT.create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopLeftOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopRight = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT.create(doorBlock, textureMapping, blockModels.modelOutput));
        final MultiVariant multiVariantTopRightOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(doorBlock, textureMapping, blockModels.modelOutput));
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

    private void createCutoutBlock(final BlockModelGenerators blockModels, final Block block) {
        blockModels.createTrivialBlock(block, TexturedModel.CUBE);
    }

}
