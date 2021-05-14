package crimson_twilight.immersive_energy.common;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.IERecipes;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class IEnRecipes 
{
	public static void initCraftingRecipes(IForgeRegistry<IRecipe> registry)
	{
		
	}

	public static void initBlueprintRecipes()
	{
		
		BlueprintCraftingRecipe.addRecipe("powerarmor", new ItemStack(IEnContent.itemPowerArmorHelmet), new ItemStack(IEContent.itemsSteelArmor[3]), "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "wool", "wool", new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.INSULATING_GLASS.getMeta()));
		BlueprintCraftingRecipe.addRecipe("powerarmor", new ItemStack(IEnContent.itemPowerArmorChestplate), new ItemStack(IEContent.itemsSteelArmor[2]), "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "wool", "wool", "wool", "wool", new ItemStack(IEContent.itemWireCoil, 4, 2), new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.CAPACITOR_HV.getMeta()), new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.COIL_HV.getMeta()));
		BlueprintCraftingRecipe.addRecipe("powerarmor", new ItemStack(IEnContent.itemPowerArmorLegs), new ItemStack(IEContent.itemsSteelArmor[1]), "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", "wool", "wool", "wool", new ItemStack(IEContent.itemToolUpgrades, 2, 2));
		BlueprintCraftingRecipe.addRecipe("powerarmor", new ItemStack(IEnContent.itemPowerArmorBoots), new ItemStack(IEContent.itemsSteelArmor[0]), "plateSteel", "plateSteel", "plateSteel", "plateSteel", "wool", "wool", "blockSlime", "blockSlime");
		
		BlueprintCraftingRecipe.addRecipe("solar", new ItemStack(IEnContent.itemMaterial, 1, 1), "blockGlassColorless", "gemLapis", "plateSteel", "plateAluminum", "plateAluminum", new ItemStack(IEContent.itemMaterial, 1, 9));
		BlueprintCraftingRecipe.addRecipe("solar", new ItemStack(IEnContent.blockGenerators0, 1, 0), new ItemStack(IEnContent.itemMaterial, 1, 1), new ItemStack(IEContent.itemMaterial, 1, 9), "dustRedstone", "plateSteel", "plateSteel", "blockSteel");
		
		BlueprintCraftingRecipe.addRecipe("advanced_tool", new ItemStack(IEnContent.toolHeftyWrench), "plateSteel", "plateSteel", "blockSteel", "stickSteel", "stickSteel", "leather");
		BlueprintCraftingRecipe.addRecipe("advanced_tool", new ItemStack(IEnContent.itemUpgrades, 1, 0), new ItemStack(IEContent.itemMaterial, 1, 26), new ItemStack(IEContent.itemMaterial, 1, 26), "wireAluminum", new ItemStack(IEContent.itemMaterial, 1, 9), new ItemStack(IEContent.itemWireCoil, 1, 7), new ItemStack(IEContent.itemWireCoil, 1, 7));
		BlueprintCraftingRecipe.addRecipe("advanced_tool", new ItemStack(IEnContent.itemUpgrades, 1, 1), new ItemStack(IEContent.itemMaterial, 1, 26), new ItemStack(IEContent.itemMaterial, 1, 26), Items.BLAZE_POWDER, new ItemStack(IEContent.itemMaterial, 1, 9), new ItemStack(IEContent.itemWireCoil, 1, 7), new ItemStack(IEContent.itemWireCoil, 1, 7));
		BlueprintCraftingRecipe.addRecipe("advanced_tool", new ItemStack(IEnContent.itemUpgrades, 1, 2), new ItemStack(IEContent.itemMaterial, 1, 26), new ItemStack(IEContent.itemMaterial, 1, 26), "gemLapis", new ItemStack(IEContent.itemMaterial, 1, 9), new ItemStack(IEContent.itemWireCoil, 1, 7), new ItemStack(IEContent.itemWireCoil, 1, 7));
		BlueprintCraftingRecipe.addRecipe("advanced_tool", new ItemStack(IEnContent.itemUpgrades, 1, 3), new ItemStack(IEContent.itemMaterial, 1, 9), new ItemStack(IEContent.itemWireCoil, 1, 7), "dustLead", Items.REDSTONE, "plateCopper", "plateCopper");
		BlueprintCraftingRecipe.addRecipe("advanced_tool", new ItemStack(IEnContent.itemUpgrades, 1, 4), new ItemStack(IEContent.itemMaterial, 1, 10), new ItemStack(IEContent.itemWireCoil, 1, 2), "dustLead", Items.REDSTONE, "plateGold", "plateGold");

		BlueprintCraftingRecipe.addRecipe("molds", new ItemStack(IEnContent.itemMold, 1, 0), "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", new ItemStack(IEnContent.itemNail, 1));
	}

	public static void initFurnaceRecipes()
	{
		//Ores
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(IEnContent.blockOre, 1, 0), new ItemStack(IEnContent.itemMetal, 1, 0), 0.7f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(IEnContent.blockOre, 1, 1), new ItemStack(IEnContent.itemMetal, 1, 1), 1.3f);
		
		//Dusts
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(IEnContent.itemMetal, 1, 2), new ItemStack(IEnContent.itemMetal, 1, 0), 0.3f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(IEnContent.itemMetal, 1, 3), new ItemStack(IEnContent.itemMetal, 1, 1), 0.3f);
		
	}
	
	public static void initExcavatorOres()
	{
		ExcavatorHandler.addMineral("Thorite", 13, .33f, new String[] {"oreThorium", "oreUranium", "oreLead", "denseoreThorium"}, new float[] {.65f, .25f, .05f, .05f}).addReplacement("oreUranium", "oreYellorium");
		ExcavatorHandler.addMineral("Uraninite", 8, .37f, new String[] {"oreUranium", "oreThorium", "oreLead", "orePlutonium", "denseoreUranium"}, new float[] {.60f, .25f, .05f, .05f, .05f}).addReplacement("oreUranium", "oreYellorium");
		ExcavatorHandler.addMineral("Wolframite", 8, .30f, new String[] {"oreIron", "oreManganese", "oreTungsten", "denseoreIron", "denseoreManganese"}, new float[] {.40f, .40f, .1f, .05f, .05f});
		ExcavatorHandler.addMineral("Ferberite", 5, .30f, new String[] {"oreIron", "oreTungsten", "denseoreIron"}, new float[] {.5f, .45f, .05f});
		if(OreDictionary.doesOreNameExist("oreManganese"))
			ExcavatorHandler.addMineral("H�bnerite", 5, .30f, new String[] {"oreManganese", "oreTungsten", "denseoreManganese"}, new float[] {.5f, .45f, .05f});
	}

	public static void initBlastFurnaceRecipes()
	{
		
	}

	public static void initMetalPressRecipes()
	{
		MetalPressRecipe.addRecipe(new ItemStack(IEnContent.itemNail, 1, 0), "nuggetIron", new ItemStack(IEnContent.itemMold, 1, 0), 100);
		MetalPressRecipe.addRecipe(new ItemStack(IEnContent.itemNail, 1, 1), "nuggetSteel", new ItemStack(IEnContent.itemMold, 1, 0), 100);
	}

	public static void initCrusherRecipes()
	{
		IERecipes.addOreDictCrusherRecipe("Thorium", "Uranium", 0.15f);
		IERecipes.addOreDictCrusherRecipe("Tungsten", "Iron", 0.25f);
	}

	public static void postInitOreDictRecipes()
	{
		
	}

	public static void initAlloySmeltingRecipes()
	{
		
	}
	
	public static void initMixerRecipies()
	{
		MixerRecipe.addRecipe(new FluidStack(IEnContent.fluidCharging, 1000), new FluidStack(FluidRegistry.WATER, 1000), new IngredientStack[] {new IngredientStack(new ItemStack(Items.REDSTONE, 4)), new IngredientStack(new ItemStack(IEContent.itemMetal, 4, 16)) }, 8192);
	}

	public static void initArcSmeltingRecipes()
	{
		IERecipes.addArcOreSmelting(new ItemStack(IEnContent.itemMetal, 1, 2), "Thorium");
		IERecipes.addArcOreSmelting(new ItemStack(IEnContent.itemMetal, 1, 3), "Tungsten");
	}
	
	public static void initGasBurnerRecipes()
	{
		
	}
}
