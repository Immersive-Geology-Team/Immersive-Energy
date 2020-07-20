package crimson_twilight.immersive_energy.common.compat.JEI.gas_burner;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import crimson_twilight.immersive_energy.api.crafting.GasBurnerRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GasBurnerRecipeWrapper implements IRecipeWrapper
{
	private final List<ItemStack> inputs;
	private final ItemStack output;
	
	public GasBurnerRecipeWrapper(GasBurnerRecipe recipe)
	{
		this.inputs = (List<ItemStack>)(recipe.input instanceof List?recipe.input: Arrays.asList((ItemStack)recipe.input));
		this.output = recipe.output;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}

	public ItemStack getSmeltingOutput()
	{
		return output;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
	}
}
