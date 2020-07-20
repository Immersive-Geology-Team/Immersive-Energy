package crimson_twilight.immersive_energy.common.compat.JEI.gas_burner;

import crimson_twilight.immersive_energy.api.crafting.GasBurnerRecipe;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.blocks.metal.BlockTypes_Machines0;
import crimson_twilight.immersive_energy.common.compat.JEI.IEnRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GasBurnerFuelCategory extends IEnRecipeCategory<GasBurnerRecipe, GasBurnerRecipeWrapper>
{
	public static ResourceLocation background = new ResourceLocation("immersive_energy:textures/gui/gas_burner.png");

	public GasBurnerFuelCategory(IGuiHelper helper)
	{
		super("gasburner", "gui.immersive_energy.gasBurner", helper.createDrawable(background, 8, 8, 142, 65), GasBurnerRecipe.class, new ItemStack(IEnContent.blockMachines0, 1, BlockTypes_Machines0.GAS_BURNER.getMeta()));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, GasBurnerRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 69, 24);
		guiItemStacks.init(1, false, 91, 24);
		guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(1, recipeWrapper.getSmeltingOutput());
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(GasBurnerRecipe recipe)
	{
		return new GasBurnerRecipeWrapper(recipe);
	}
}
