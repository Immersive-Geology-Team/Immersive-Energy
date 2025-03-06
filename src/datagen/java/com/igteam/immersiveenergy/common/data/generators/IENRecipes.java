package com.igteam.immersiveenergy.common.data.generators;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.igteam.immersiveenergy.common.block.multiblocks.recipe.BurnerFuel;
import com.igteam.immersiveenergy.common.block.multiblocks.recipe.builder.BurnerFuelBuilder;
import com.igteam.immersiveenergy.core.lib.IENLib;
import com.igteam.immersiveenergy.core.lib.ResourceUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
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
        IENLib.IEN_LOGGER.info("Starting Multiblock Recipe Registration");
        BurnerFuelBuilder.builder(Items.CHARCOAL).setTime(800).setEnergy(512).build(consumer, ResourceUtils.ien("burner/burner_fuel_charcoal"));
        BurnerFuelBuilder.builder(Items.COAL).setTime(800).setEnergy(1024).build(consumer, ResourceUtils.ien("burner/burner_fuel_coal"));
        BurnerFuelBuilder.builder(IEItems.Ingredients.COAL_COKE).setTime(800).setEnergy(2048).build(consumer, ResourceUtils.ien("burner/burner_fuel_coal_coke"));
    }
}
