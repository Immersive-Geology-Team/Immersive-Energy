package crimson_twilight.immersive_energy.common.compat.JEI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.client.gui.GuiAlloySmelter;
import blusunrize.immersiveengineering.client.gui.GuiBlastFurnace;
import blusunrize.immersiveengineering.common.util.compat.jei.IEFluidTooltipCallback;
import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import crimson_twilight.immersive_energy.api.crafting.GasBurnerRecipe;
import crimson_twilight.immersive_energy.api.crafting.GasBurnerRecipe.GasBurnerFuel;
import crimson_twilight.immersive_energy.api.energy.FuelHandler;
import crimson_twilight.immersive_energy.client.gui.GUIGasBurner;
import crimson_twilight.immersive_energy.common.compat.JEI.gas_burner.GasBurnerRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;

@JEIPlugin
public class JEIHelper implements IModPlugin
{
	public static IJeiHelpers jeiHelpers;
	public static IModRegistry modRegistry;
	public static IDrawable slotDrawable;
	public static ITooltipCallback fluidTooltipCallback = new IEFluidTooltipCallback();

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
	{

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{

	}

	Map<Class, IEnRecipeCategory> categories = new LinkedHashMap<>();

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		jeiHelpers = registry.getJeiHelpers();
		//Recipes
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		slotDrawable = guiHelper.getSlotDrawable();
		categories.put(GasBurnerRecipe.class, new GasBurnerRecipeCategory(guiHelper));
		categories.put(GasBurnerFuel.class, new GasBurnerRecipeCategory(guiHelper));
	}

	@Override
	public void register(IModRegistry registryIn)
	{
		modRegistry = registryIn;
		
		for(IEnRecipeCategory<Object, IRecipeWrapper> cat : categories.values())
		{
			cat.addCatalysts(registryIn);
			modRegistry.handleRecipes(cat.getRecipeClass(), cat, cat.getRecipeCategoryUid());
		}

		//modRegistry.addRecipes(new ArrayList<>(GasBurnerRecipe.getRecipes()), "ienergy.gasburner");
		//modRegistry.addRecipes(new ArrayList<>(FuelHandler.getBurnerFuels()), "ienergy.gasburner.fuel");
		
		//modRegistry.addRecipeClickArea(GUIGasBurner.class, 76, 35, 22, 15, "ienergy.gasburner", "ienergy.gasburner.fuel");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
	}

	private IEnRecipeCategory getFactory(Class recipeClass)
	{
		IEnRecipeCategory factory = this.categories.get(recipeClass);

		if(factory==null&&recipeClass!=Object.class)
			factory = getFactory(recipeClass.getSuperclass());

		return factory;
	}
	
	
}
