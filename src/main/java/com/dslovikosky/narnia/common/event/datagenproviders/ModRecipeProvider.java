package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItemTags;
import com.dslovikosky.narnia.common.constants.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(final PackOutput pOutput, final CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(final @NotNull RecipeOutput recipeOutput) {
        woodFromLogs(recipeOutput, ModBlocks.WORLD_WOOD.get(), ModBlocks.WORLD_WOOD_LOG.get());
        planksFromLog(recipeOutput, ModBlocks.WORLD_WOOD_PLANKS.get(), ModItemTags.WORLD_WOOD, 4);
        buttonFromPlanks(recipeOutput, ModBlocks.WORLD_WOOD_BUTTON.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        doorFromPlanks(recipeOutput, ModBlocks.WORLD_WOOD_DOOR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        fenceFromPlanks(recipeOutput, ModBlocks.WORLD_WOOD_FENCE.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        fenceGateFromPlanks(recipeOutput, ModBlocks.WORLD_WOOD_FENCE_GATE.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WORLD_WOOD_SLAB.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        stairsFromPlanks(recipeOutput, ModBlocks.WORLD_WOOD_STAIR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        trapDoor(recipeOutput, ModBlocks.WORLD_WOOD_TRAP_DOOR.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        pressurePlate(recipeOutput, ModBlocks.WORLD_WOOD_PRESSURE_PLATE.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        sign(recipeOutput, ModItems.WORLD_WOOD_SIGN.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        hangingSign(recipeOutput, ModItems.WORLD_WOOD_HANGING_SIGN.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        woodenBoat(recipeOutput, ModItems.WORLD_WOOD_BOAT.get(), ModBlocks.WORLD_WOOD_PLANKS.get());
        chestBoat(recipeOutput, ModItems.WORLD_WOOD_CHEST_BOAT.get(), ModBlocks.WORLD_WOOD_PLANKS.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THE_MAGICIANS_NEPHEW.get())
                .pattern("P")
                .pattern("P")
                .pattern("P")
                .define('P', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(recipeOutput);
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
}
