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
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
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
        stairsFromPlanks(output, ModBlocks.WORLD_WOOD_STAIR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        trapDoor(output, ModBlocks.WORLD_WOOD_TRAP_DOOR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        pressurePlate(ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        sign(output, ModItems.WORLD_WOOD_STANDING_SIGN.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        hangingSign(ModItems.WORLD_WOOD_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        woodenBoat(ModItems.WORLD_WOOD_BOAT.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        chestBoat(ModItems.WORLD_WOOD_CHEST_BOAT.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
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

    private void stairsFromPlanks(final RecipeOutput recipeOutput, final StairBlock stairBlock, final Block planks) {
        stairBuilder(stairBlock, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
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
