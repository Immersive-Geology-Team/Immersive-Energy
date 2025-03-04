package com.igteam.immersiveenergy.common.data.generators;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class IENRecipes extends RecipeProvider
{
    public IENRecipes(PackOutput pOutput)
    {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer)
    {
        multiblockRecipes(consumer);
        itemRecipes(consumer);
    }

    private void itemRecipes(Consumer<FinishedRecipe> consumer)
    {

    }


    private void multiblockRecipes(Consumer<FinishedRecipe> consumer)
    {
    }
}
