package crimson_twilight.immersive_energy.common.compat.jei.gas_burner;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nonnull;

import blusunrize.immersiveengineering.common.util.Utils;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GasBurnerFuelWrapper implements IRecipeWrapper
{
	private final List<FluidStack> fuel;
	private static final String S_BURNTIME = I18n.format("desc.immersive_energy.info.burnerFuelTime");
	private final String burnTime;
	private final IDrawableAnimated flame;
	
	public GasBurnerFuelWrapper(IGuiHelper guiHelper, List<FluidStack> fuel, int burnTime)
	{
		this.fuel = fuel;
		this.burnTime = I18n.format("desc.immersiveengineering.info.seconds", Utils.formatDouble(burnTime/20f, "0.##"));
		
		ResourceLocation burnerBackgroundLocation = new ResourceLocation(ImmersiveEnergy.MODID, "textures/gui/gas_burner.png");
		IDrawableStatic flameDrawable = guiHelper.createDrawable(burnerBackgroundLocation, 234, 32, 9, 12);
		this.flame = guiHelper.createAnimatedDrawable(flameDrawable, burnTime, IDrawableAnimated.StartDirection.TOP, true);
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(FluidStack.class, fuel);
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		minecraft.fontRenderer.drawString(S_BURNTIME, 24, 8, Color.gray.getRGB());
		minecraft.fontRenderer.drawString(burnTime, 24, 18, Color.gray.getRGB());
	}
}
