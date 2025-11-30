package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItemTags;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
        super(registries, output);
    }


    @Override
    protected void buildRecipes() {
        woodFromLogs(ModBlocks.WORLD_WOOD.get(), ModBlocks.WORLD_WOOD_LOG.get());
        planksFromLog(ModBlocks.WORLD_WOOD_PLANKS.get(), ModItemTags.WORLD_WOOD, 4);
        buttonFromPlanks(output, ModBlocks.WORLD_WOOD_BUTTON.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        doorFromPlanks(output, ModBlocks.WORLD_WOOD_DOOR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        fenceFromPlanks(output, ModBlocks.WORLD_WOOD_FENCE.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        fenceGateFromPlanks(output, ModBlocks.WORLD_WOOD_FENCE_GATE.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WORLD_WOOD_SLAB.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        stairs(output, ModBlocks.WORLD_WOOD_STAIR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        trapDoor(output, ModBlocks.WORLD_WOOD_TRAP_DOOR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        pressurePlate(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        sign(output, ModItems.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        hangingSign(ModItems.WORLD_WOOD_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        woodenBoat(ModItems.WORLD_WOOD_BOAT.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        chestBoat(ModItems.WORLD_WOOD_CHEST_BOAT.get(), ModBlocks.WORLD_WOOD_PLANKS.get());

        shaped(RecipeCategory.TRANSPORTATION, ModItems.YELLOW_RING)
                .unlockedBy(getHasName(ModItems.SPARKLING_DUST), has(ModItems.SPARKLING_DUST))
                .define('G', Items.GOLD_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('D', ModItems.SPARKLING_DUST)
                .pattern(" G ")
                .pattern("IDI")
                .pattern(" I ")
                .save(output);
        shaped(RecipeCategory.TRANSPORTATION, ModItems.GREEN_RING)
                .unlockedBy(getHasName(ModItems.SPARKLING_DUST), has(ModItems.SPARKLING_DUST))
                .define('G', Items.EMERALD)
                .define('I', Items.IRON_INGOT)
                .define('D', ModItems.SPARKLING_DUST)
                .pattern(" G ")
                .pattern("IDI")
                .pattern(" I ")
                .save(output);

        shapeless(RecipeCategory.MISC, ModItems.ASLANS_CHRONICLE)
                .unlockedBy(getHasName(ModItems.SPARKLING_DUST), has(ModItems.SPARKLING_DUST))
                .requires(Items.PAPER, 3)
                .requires(Items.LEATHER)
                .requires(ModItems.SPARKLING_DUST)
                .save(output);

        smeltingResultFromBase(ModBlocks.DARK_CITY_SMOOTH_STONE.get(), ModBlocks.DARK_CITY_COBBLESTONE.get());
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB, ModBlocks.DARK_CITY_SMOOTH_STONE, 2);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_SMOOTH_STONE_SLAB, ModBlocks.DARK_CITY_SMOOTH_STONE);
        shaped(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.DARK_CITY_WINDOW, 4))
                .unlockedBy(getHasName(ModBlocks.DARK_CITY_COBBLESTONE), has(ModBlocks.DARK_CITY_COBBLESTONE))
                .define('S', ModBlocks.DARK_CITY_COBBLESTONE)
                .define('I', Blocks.IRON_BARS)
                .pattern(" I ")
                .pattern("ISI")
                .pattern(" I ")
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.DARK_CITY_STONE_BRICKS, 4))
                .unlockedBy(getHasName(ModBlocks.DARK_CITY_COBBLESTONE), has(ModBlocks.DARK_CITY_COBBLESTONE))
                .define('S', ModBlocks.DARK_CITY_COBBLESTONE)
                .pattern("SS")
                .pattern("SS")
                .save(output);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_STONE_BRICKS, ModBlocks.DARK_CITY_COBBLESTONE);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_STONE_BRICK_SLAB, ModBlocks.DARK_CITY_STONE_BRICKS, 2);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_STONE_BRICK_SLAB, ModBlocks.DARK_CITY_STONE_BRICKS);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_STONE_BRICK_WALL, ModBlocks.DARK_CITY_STONE_BRICKS);
        wall(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_STONE_BRICK_WALL, ModBlocks.DARK_CITY_STONE_BRICKS);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_STONE_BRICK_STAIRS, ModBlocks.DARK_CITY_STONE_BRICKS);
        stairs(output, ModBlocks.DARK_CITY_STONE_BRICK_STAIRS.get(), ModBlocks.DARK_CITY_STONE_BRICKS.get());
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_DARK_CITY_STONE_BRICKS)
                .unlockedBy(getHasName(ModBlocks.DARK_CITY_STONE_BRICKS), has(ModBlocks.DARK_CITY_STONE_BRICKS))
                .requires(ModBlocks.DARK_CITY_STONE_BRICKS)
                .requires(Items.VINE)
                .save(output);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DARK_CITY_STONE_BRICKS, ModBlocks.DARK_CITY_STONE_BRICKS);
        trapDoor(output, ModBlocks.DARK_CITY_STONE_BRICK_TRAPDOOR.get(), ModBlocks.DARK_CITY_STONE_BRICKS.get());

        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_SLATE_SLAB, ModBlocks.DARK_CITY_SLATE, 2);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_SLATE_SLAB, ModBlocks.DARK_CITY_SLATE);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_CITY_SLATE_STAIRS, ModBlocks.DARK_CITY_SLATE);
        stairs(output, ModBlocks.DARK_CITY_SLATE_STAIRS.get(), ModBlocks.DARK_CITY_SLATE.get());

        shaped(RecipeCategory.COMBAT, ModItems.ANCIENT_HELMET)
                .unlockedBy(getHasName(ModItems.ANCIENT_METAL), has(ModItems.ANCIENT_METAL))
                .define('A', ModItems.ANCIENT_METAL)
                .pattern("AAA")
                .pattern("A A")
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.ANCIENT_CHESTPLATE)
                .unlockedBy(getHasName(ModItems.ANCIENT_METAL), has(ModItems.ANCIENT_METAL))
                .define('A', ModItems.ANCIENT_METAL)
                .pattern("A A")
                .pattern("AAA")
                .pattern("AAA")
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.ANCIENT_LEGGINGS)
                .unlockedBy(getHasName(ModItems.ANCIENT_METAL), has(ModItems.ANCIENT_METAL))
                .define('A', ModItems.ANCIENT_METAL)
                .pattern("AAA")
                .pattern("A A")
                .pattern("A A")
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.ANCIENT_BOOTS)
                .unlockedBy(getHasName(ModItems.ANCIENT_METAL), has(ModItems.ANCIENT_METAL))
                .define('A', ModItems.ANCIENT_METAL)
                .pattern("A A")
                .pattern("A A")
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.ANCIENT_SWORD)
                .unlockedBy(getHasName(ModItems.ANCIENT_METAL), has(ModItems.ANCIENT_METAL))
                .define('S', Items.STICK)
                .define('A', ModItems.ANCIENT_METAL)
                .pattern("  A")
                .pattern(" A ")
                .pattern("S  ")
                .save(output);
    }

    private void buttonFromPlanks(final RecipeOutput recipeOutput, final ButtonBlock buttonBlock, final Block planks) {
        buttonBuilder(buttonBlock, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
    }

    private void doorFromPlanks(final RecipeOutput recipeOutput, final DoorBlock doorBlock, final Block planks) {
        doorBuilder(doorBlock, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
    }

    private void fenceFromPlanks(final RecipeOutput recipeOutput, final FenceBlock fenceBlock, final Block planks) {
        fenceBuilder(fenceBlock, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
    }

    private void fenceGateFromPlanks(final RecipeOutput recipeOutput, final FenceGateBlock fenceGateBlock, final Block planks) {
        fenceGateBuilder(fenceGateBlock, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
    }

    private void stairs(final RecipeOutput recipeOutput, final StairBlock stairBlock, final Block block) {
        stairBuilder(stairBlock, Ingredient.of(block)).unlockedBy(getHasName(block), has(block)).save(recipeOutput);
    }

    private void trapDoor(final RecipeOutput recipeOutput, final TrapDoorBlock trapDoorBlock, final Block planks) {
        trapdoorBuilder(trapDoorBlock, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
    }

    private void sign(final RecipeOutput recipeOutput, final SignItem signItem, final Block planks) {
        signBuilder(signItem, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return Constants.MOD_ID;
        }
    }
}
