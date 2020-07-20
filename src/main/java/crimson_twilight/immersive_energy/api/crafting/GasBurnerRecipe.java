package crimson_twilight.immersive_energy.api.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class GasBurnerRecipe 
{
	public final Object input;
	public final ItemStack output;
	
	public GasBurnerRecipe(ItemStack output, Object input) 
	{
		this.output = output;
		this.input = input;
	}

	public static ArrayList<GasBurnerRecipe> recipeList = new ArrayList<GasBurnerRecipe>();

	public static void addRecipe(ItemStack output, Object input)
	{
		GasBurnerRecipe recipe = new GasBurnerRecipe(output, input);
		if(recipe.input!=null)
		{
			if(!recipeList.contains(input))
			{
				recipeList.add(recipe);
			}
			else
			{
				new Exception("Gas Burner Recipe Input Already Exists!");
			}
		}
		else
		{
			new Exception("Gas Burner Recipe Input Can't Be Null!");
		}
	}

	public static ItemStack getResult(ItemStack input)
	{
		for(GasBurnerRecipe recipe : recipeList)
			if(ApiUtils.stackMatchesObject(input, recipe.input))
				return recipe.output;
		if(isCookableFood(input))
			return FurnaceRecipes.instance().getSmeltingResult(input);

		return ItemStack.EMPTY;
	}

	public static List<GasBurnerRecipe> removeRecipes(ItemStack stack)
	{
		List<GasBurnerRecipe> list = new ArrayList();
		Iterator<GasBurnerRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			GasBurnerRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.output, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}
	
	public static List<GasBurnerRecipe> getRecipes()
	{
		List<GasBurnerRecipe> list = new ArrayList();
		list.addAll(recipeList);
		for (Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet())
		{
			if(isCookableFood(entry.getKey()))
			{
				list.add(new GasBurnerRecipe(entry.getValue(), entry.getKey()));
			}
		}
		return list;
	}

	public static boolean isCookableFood(ItemStack stack)
	{
		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		return !result.isEmpty()&&(result.getItem() instanceof ItemFood||result.getItem() instanceof ItemPotion);
	}
	
	public class GasBurnerFuel
	{
		
	}
}
