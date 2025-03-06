package com.igteam.immersiveenergy.common.block.multiblocks.recipe.serializer;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import com.google.gson.JsonObject;
import com.igteam.immersiveenergy.common.block.multiblocks.recipe.BurnerFuel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;

public class BurnerFuelSerializer extends IERecipeSerializer<BurnerFuel>
{
    public BurnerFuelSerializer()
    {

    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.COAL);
    }

    public BurnerFuel readFromJson(ResourceLocation recipeId, JsonObject json, ICondition.IContext context)
    {
        Ingredient input = Ingredient.fromJson(json.getAsJsonObject("input"));
        int time = GsonHelper.getAsInt(json, "time", 800);
        int energy = GsonHelper.getAsInt(json, "energy", 512);
        return new BurnerFuel(recipeId, input, time, energy);
    }

    @Nullable
    public BurnerFuel fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
    {
        Ingredient input = Ingredient.fromNetwork(buffer);
        int time = buffer.readInt();
        int energy = buffer.readInt();
        return new BurnerFuel(recipeId, input, time, energy);
    }

    public void toNetwork(FriendlyByteBuf buffer, BurnerFuel recipe)
    {
        recipe.fuel.toNetwork(buffer);
        buffer.writeInt(recipe.burnTime);
        buffer.writeInt(recipe.output);
    }
}
