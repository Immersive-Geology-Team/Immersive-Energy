package crimson_twilight.immersive_energy.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.ThermoelectricHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.Config.IEnConfig;
import crimson_twilight.immersive_energy.common.Config.IEnConfig.Tools;
import crimson_twilight.immersive_energy.common.blocks.BlockIEnBase;
import crimson_twilight.immersive_energy.common.blocks.BlockIEnSlab;
import crimson_twilight.immersive_energy.common.blocks.BlockTypes_MetalsIEn;
import crimson_twilight.immersive_energy.common.blocks.BlockTypes_OresIEn;
import crimson_twilight.immersive_energy.common.blocks.ItemBlockIEnBase;
import crimson_twilight.immersive_energy.common.blocks.TileEntityIEnSlab;
import crimson_twilight.immersive_energy.common.blocks.metal.BlockGenerators0;
import crimson_twilight.immersive_energy.common.blocks.metal.BlockMachines0;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityGasBurner;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntitySolarPanel;
import crimson_twilight.immersive_energy.common.compat.IEnCompatModule;
import crimson_twilight.immersive_energy.common.items.IEnArrowBase;
import crimson_twilight.immersive_energy.common.items.ItemIEnBase;
import crimson_twilight.immersive_energy.common.items.ItemIEnMaterial;
import crimson_twilight.immersive_energy.common.items.ItemPowerArmorBoots;
import crimson_twilight.immersive_energy.common.items.ItemPowerArmorChestplate;
import crimson_twilight.immersive_energy.common.items.ItemPowerArmorHelmet;
import crimson_twilight.immersive_energy.common.items.ItemPowerArmorLegs;
import crimson_twilight.immersive_energy.common.items.ItemThoriumRod;
import crimson_twilight.immersive_energy.common.items.ItemToolUpgradeIEn;
import crimson_twilight.immersive_energy.common.items.ItemUraniumRod;
import crimson_twilight.immersive_energy.common.items.ToolHeftyWrench;
import crimson_twilight.immersive_energy.common.util.ArrowLogicPenetrating;
import crimson_twilight.immersive_energy.common.util.ArrowLogicShock;
import crimson_twilight.immersive_energy.common.world.IEnWorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid=ImmersiveEnergy.MODID)
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class IEnContent 
{
	public static ArrayList<Block> registeredIEnBlocks = new ArrayList<Block>();
	public static ArrayList<Item> registeredIEnItems = new ArrayList<Item>();
	public static List<Class<? extends TileEntity>> registeredIEnTiles = new ArrayList<>();
	
	public static BlockIEnBase<BlockTypes_OresIEn> blockOre;
	public static BlockIEnBase<BlockTypes_MetalsIEn> blockStorage;
	
	public static BlockIEnBase<BlockTypes_MetalsIEn> blockSheetmetal;
	public static BlockIEnBase<BlockTypes_MetalsIEn> blockSheetmetalSlabs;
	
	public static BlockGenerators0 blockGenerators0;
	public static BlockMachines0 blockMachines0;
	
	public static ArmorMaterial powerArmor = EnumHelper.addArmorMaterial("power_armor_suit", "immersive_energy:power_armor_suit", 5260, new int[] {2, 7, 8, 3}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3);
	
	public static ItemPowerArmorChestplate itemPowerArmorChestplate;
	public static ItemPowerArmorHelmet itemPowerArmorHelmet;
	public static ItemPowerArmorLegs itemPowerArmorLegs;
	public static ItemPowerArmorBoots itemPowerArmorBoots;
	
	public static ItemIEnBase itemMetal;
	public static ItemIEnBase itemMaterial;
	public static ItemIEnBase itemThoriumRod;
	public static ItemIEnBase itemUraniumRod;
	
	public static IEnArrowBase itemArrow;
	public static ItemIEnBase itemUpgrades;
	public static ItemIEBase toolHeftyWrench;
	//public static ItemIEBase toolUpgradeableCrossbow;
	
	
	public static void preInit()
	{
		//Blocks
		blockOre = (BlockIEnBase)new BlockIEnBase("ore", Material.ROCK, PropertyEnum.create("type", BlockTypes_OresIEn.class), ItemBlockIEnBase.class).setOpaque(true).setHardness(3.0F).setResistance(5.0F);
		blockStorage = (BlockIEnBase)new BlockIEnBase("storage", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalsIEn.class), ItemBlockIEnBase.class).setOpaque(true).setHardness(5.0F).setResistance(10.0F);
	
		blockSheetmetal = (BlockIEnBase)new BlockIEnBase("sheetmetal", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalsIEn.class), ItemBlockIEnBase.class).setOpaque(true).setHardness(3.0F).setResistance(10.0F);
		blockSheetmetalSlabs = (BlockIEnSlab)new BlockIEnSlab("sheetmetal_slab", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalsIEn.class)).setHardness(3.0F).setResistance(10.0F);
		
		blockGenerators0 = new BlockGenerators0();
		blockMachines0 = new BlockMachines0();
		
		//Items
		itemMetal = new ItemIEnBase("metal", 64, 
				"ingot_thorium", "ingot_tungsten", 
				"dust_thorium", "dust_tungsten", 
				"nugget_thorium", "nugget_tungsten",
				"plate_thorium", "plate_tungsten");
		itemMaterial = new ItemIEnMaterial();
		itemThoriumRod = new ItemThoriumRod();
		itemUraniumRod = new ItemUraniumRod();
		
		
		//Armor 
		itemPowerArmorChestplate = new ItemPowerArmorChestplate();
		itemPowerArmorHelmet = new ItemPowerArmorHelmet();
		itemPowerArmorLegs = new ItemPowerArmorLegs();
		itemPowerArmorBoots = new ItemPowerArmorBoots(); 
		
		//Tools and Weapons
		itemArrow = new IEnArrowBase("arrow_shocking", "electricdamage", String.valueOf(Tools.shock_arrow_electric_damage)).setDamage(Tools.shock_arrow_regular_damage).setKnockback(Tools.shock_arrow_knockback).setIgnoreInvulnerability(Tools.shock_arrow_ignore).setLogic(new ArrowLogicShock(Tools.shock_arrow_electric_damage));
		itemArrow = new IEnArrowBase("arrow_penetrating", "penetratingdamage", String.valueOf(Tools.penetrating_arrow_regular_damage)).setDamage(Tools.penetrating_arrow_regular_damage).setKnockback(Tools.penetrating_arrow_knockback).setIgnoreInvulnerability(Tools.penetrating_arrow_ignore).setLogic(new ArrowLogicPenetrating(Tools.penetrating_arrow_penetrating_damage));
		itemUpgrades = new ItemToolUpgradeIEn();
		toolHeftyWrench = new ToolHeftyWrench();
		//toolUpgradeableCrossbow = new ToolUpgradeableCrossbow();
	}

	public static void preInitEnd()
	{
		
	}

	public static void registerOres()
	{
		registerToOreDict("ore", blockOre);
		registerToOreDict("block", blockStorage);
		registerToOreDict("", itemMetal);
		OreDictionary.registerOre("stickTungsten", new ItemStack(itemMaterial, 1, 0));
		OreDictionary.registerOre("stickThorium", new ItemStack(itemThoriumRod));
		OreDictionary.registerOre("stickUranium", new ItemStack(itemUraniumRod));
	}

	public static void init()
	{
		/*MINING LEVELS*/
		blockOre.setHarvestLevel("pickaxe", 2, blockOre.getStateFromMeta(BlockTypes_OresIEn.THORIUM.getMeta()));
		blockOre.setHarvestLevel("pickaxe", 3, blockOre.getStateFromMeta(BlockTypes_OresIEn.TUNGSTEN.getMeta()));

		/*WORLDGEN*/
		addConfiguredWorldgen(blockOre.getStateFromMeta(BlockTypes_OresIEn.THORIUM.getMeta()), "thorium", IEnConfig.Ores.ore_thorium);
		addConfiguredWorldgen(blockOre.getStateFromMeta(BlockTypes_OresIEn.TUNGSTEN.getMeta()), "tungsten", IEnConfig.Ores.ore_tungsten);
		
		/*TILEENTITIES*/
		registerTile(TileEntityIEnSlab.class);
		registerTile(TileEntitySolarPanel.class); 
		registerTile(TileEntityGasBurner.class); 
		
		//Railgun
		RailgunHandler.registerProjectileProperties(new IngredientStack("stickTungsten"), 32, 1.3).setColourMap(new int[][]{{0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0x9EA2A7, 0x9EA2A7}});
		
		//Thermoelectric
		ThermoelectricHandler.registerSourceInKelvin("blockThorium", 1800);
		
		IEnConfig.addBurnerFuel(IEnConfig.Machines.burner_fuels);
	}

	public static void initEnd()
	{
		
	}

	public static void postInit()
	{
		
	}

	public static void postInitEnd()
	{
		
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event)
	{
		/*POTIONS*/
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		/*CRAFTING*/
		IEnRecipes.initCraftingRecipes(event.getRegistry());

		/*FURNACE*/
		IEnRecipes.initFurnaceRecipes();

		/*BLUEPRINTS*/
		IEnRecipes.initBlueprintRecipes();
		
		//Excavator
		IEnRecipes.initExcavatorOres();
		
		//BlastFurnace
		IEnRecipes.initBlastFurnaceRecipes();

		//MetalPress
		IEnRecipes.initMetalPressRecipes();

		//AlloySmelting
		IEnRecipes.initAlloySmeltingRecipes();

		//Crusher
		IEnRecipes.initCrusherRecipes();

		//ArcFurnace
		IEnRecipes.initArcSmeltingRecipes();
		
		//ExtraOrDictRecipes
		IEnRecipes.postInitOreDictRecipes();
		
		//GasBurner
		IEnRecipes.initGasBurnerRecipes();

		IEnCompatModule.doModulesRecipes();
	}
	
	public static void registerTile(Class<? extends TileEntity> tile)
	{
		String s = tile.getSimpleName();
		s = s.substring(s.indexOf("TileEntity")+"TileEntity".length());
		GameRegistry.registerTileEntity(tile, ImmersiveEnergy.MODID+":"+ s);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for(Block block : registeredIEnBlocks)
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getUnlocalizedName())));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		for(Item item : registeredIEnItems)
			event.getRegistry().register(item.setRegistryName(createRegistryName(item.getUnlocalizedName())));
		
		registerOres();
	}

	private static ResourceLocation createRegistryName(String unlocalized)
	{
		unlocalized = unlocalized.substring(unlocalized.indexOf("immersive"));
		unlocalized = unlocalized.replaceFirst("\\.", ":");
		return new ResourceLocation(unlocalized);
	}

	public static Fluid setupFluid(Fluid fluid)
	{
		FluidRegistry.addBucketForFluid(fluid);
		if(!FluidRegistry.registerFluid(fluid))
			return FluidRegistry.getFluid(fluid.getName());
		return fluid;
	}

	public static void refreshFluidReferences()
	{
		
	}

	public static void registerToOreDict(String type, ItemIEnBase item, int... metas)
	{
		if(metas==null||metas.length < 1)
		{
			for(int meta = 0; meta < item.getSubNames().length; meta++)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getSubNames()[meta];
					name = createOreDictName(name);
					if(type!=null&&!type.isEmpty())
						name = name.substring(0, 1).toUpperCase()+name.substring(1);
					OreDictionary.registerOre(type+name, new ItemStack(item, 1, meta));
				}
		}
		else
		{
			for(int meta : metas)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getSubNames()[meta];
					name = createOreDictName(name);
					if(type!=null&&!type.isEmpty())
						name = name.substring(0, 1).toUpperCase()+name.substring(1);
					OreDictionary.registerOre(type+name, new ItemStack(item, 1, meta));
				}
		}
	}

	private static String createOreDictName(String name)
	{
		String upperName = name.toUpperCase();
		StringBuilder sb = new StringBuilder();
		boolean nextCapital = false;
		for(int i = 0; i < name.length(); i++)
		{
			if(name.charAt(i)=='_')
			{
				nextCapital = true;
			}
			else
			{
				char nextChar = name.charAt(i);
				if(nextCapital)
				{
					nextChar = upperName.charAt(i);
					nextCapital = false;
				}
				sb.append(nextChar);
			}
		}
		return sb.toString();
	}

	public static void registerToOreDict(String type, BlockIEnBase item, int... metas)
	{
		if(metas==null||metas.length < 1)
		{
			for(int meta = 0; meta < item.getMetaEnums().length; meta++)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getMetaEnums()[meta].toString();
					if(type!=null&&!type.isEmpty())
						name = name.substring(0, 1).toUpperCase(Locale.ENGLISH)+name.substring(1).toLowerCase(Locale.ENGLISH);
					OreDictionary.registerOre(type+name, new ItemStack(item, 1, meta));
				}
		}
		else
		{
			for(int meta : metas)
				if(!item.isMetaHidden(meta))
				{
					String name = item.getMetaEnums()[meta].toString();
					if(type!=null&&!type.isEmpty())
						name = name.substring(0, 1).toUpperCase(Locale.ENGLISH)+name.substring(1).toLowerCase(Locale.ENGLISH);
					OreDictionary.registerOre(type+name, new ItemStack(item, 1, meta));
				}
		}
	}

	public static void registerOre(String type, ItemStack ore, ItemStack ingot, ItemStack dust, ItemStack nugget, ItemStack plate, ItemStack block, ItemStack slab, ItemStack sheet, ItemStack slabSheet)
	{
		if(!ore.isEmpty())
			OreDictionary.registerOre("ore"+type, ore);
		if(!ingot.isEmpty())
			OreDictionary.registerOre("ingot"+type, ingot);
		if(!dust.isEmpty())
			OreDictionary.registerOre("dust"+type, dust);
		if(!nugget.isEmpty())
			OreDictionary.registerOre("nugget"+type, nugget);
		if(!plate.isEmpty())
			OreDictionary.registerOre("plate"+type, plate);
		if(!block.isEmpty())
			OreDictionary.registerOre("block"+type, block);
		if(!slab.isEmpty())
			OreDictionary.registerOre("slab"+type, slab);
		if(!sheet.isEmpty())
			OreDictionary.registerOre("blockSheetmetal"+type, sheet);
		if(!slabSheet.isEmpty())
			OreDictionary.registerOre("slabSheetmetal"+type, slabSheet);
	}

	public static void addConfiguredWorldgen(IBlockState state, String name, int[] config)
	{
		if(config!=null&&config.length >= 5 && config[0] > 0)
			IEnWorldGen.addOreGen(name, state, config[0], config[1], config[2], config[3], config[4]);
	}

}
