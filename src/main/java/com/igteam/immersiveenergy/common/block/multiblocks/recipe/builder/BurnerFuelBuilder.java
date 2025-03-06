package com.igteam.immersiveenergy.common.block.multiblocks.recipe.builder;

import blusunrize.immersiveengineering.api.crafting.builders.IEFinishedRecipe;
import com.igteam.immersiveenergy.common.block.multiblocks.recipe.BurnerFuel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class BurnerFuelBuilder extends IEFinishedRecipe<BurnerFuelBuilder>
{
    private BurnerFuelBuilder()
    {
        super(BurnerFuel.SERIALIZER.get());
        this.maxResultCount=0;
    }

    public static BurnerFuelBuilder builder(ItemLike input) {
        return new BurnerFuelBuilder().addInput(new ItemLike[]{input});
    }

    public static BurnerFuelBuilder builder(ItemStack input) {
        return new BurnerFuelBuilder().addInput(new ItemStack[]{input});
    }

    public static BurnerFuelBuilder builder(TagKey<Item> input) {
        return new BurnerFuelBuilder().addInput(Ingredient.of(input));
    }
}
