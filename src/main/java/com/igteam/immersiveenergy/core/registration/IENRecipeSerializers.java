package com.igteam.immersiveenergy.core.registration;

import com.igteam.immersiveenergy.common.block.multiblocks.recipe.BurnerFuel;
import com.igteam.immersiveenergy.common.block.multiblocks.recipe.serializer.BurnerFuelSerializer;
import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IENRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, IENLib.MODID);

    static {
        BurnerFuel.SERIALIZER = RECIPE_SERIALIZERS.register("burner", BurnerFuelSerializer::new);
    }
}
