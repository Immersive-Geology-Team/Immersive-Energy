package com.igteam.immersiveenergy.common.block.multiblocks.recipe;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import blusunrize.immersiveengineering.common.util.CachedRecipe;
import com.igteam.immersiveenergy.core.registration.IENRecipeTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BurnerFuel extends IESerializableRecipe
{
    public static RegistryObject<IERecipeSerializer<BurnerFuel>> SERIALIZER;
    public static final CachedRecipeList<BurnerFuel> RECIPES = new CachedRecipeList<>(IENRecipeTypes.BURNER);
    public final Ingredient fuel;
    public final int burnTime;
    public final int output;
    public BurnerFuel(ResourceLocation id, Ingredient fuel, int burnTime, int output)
    {
        super(LAZY_EMPTY, IENRecipeTypes.BURNER, id);
        this.fuel=fuel;
        this.burnTime=burnTime;
        this.output=output;
    }

    public boolean matches(ItemStack in)
    {
        return this.fuel.test(in);
    }

    public static BurnerFuel getRecipeFor(Level level, ItemStack in)
    {
        for (BurnerFuel fuel : RECIPES.getRecipes(level))
        {
            if (fuel.matches(in)) return fuel;
        }
        return null;
    }

    @Override
    protected IERecipeSerializer<?> getIESerializer()
    {
        return (IERecipeSerializer)SERIALIZER.get();
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(RegistryAccess access)
    {
        return ItemStack.EMPTY;
    }
}
