package com.dslovikosky.narnia.common.event.datagenproviders;

import com.dslovikosky.narnia.common.constants.ModBlocks;
import com.dslovikosky.narnia.common.constants.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
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
    }

    private void buttonFromPlanks(final RecipeOutput recipeOutput, final ButtonBlock buttonBlock, final Block planks) {
        buttonBuilder(buttonBlock, Ingredient.of(planks)).unlockedBy(getHasName(planks), has(planks)).save(recipeOutput);
    }
}
