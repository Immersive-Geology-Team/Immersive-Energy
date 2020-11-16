package crimson_twilight.immersive_energy.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageBlueprint;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.ShaderHelper;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.render.ItemRendererIEOBJ;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IIEMetaBlock;
import blusunrize.immersiveengineering.common.util.IELogger;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import blusunrize.immersiveengineering.common.util.compat.IECompatModule;
import blusunrize.lib.manual.ManualPages;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.client.gui.GUIGasBurner;
import crimson_twilight.immersive_energy.client.model.EvenMoreImmersiveModelRegistry;
import crimson_twilight.immersive_energy.client.render.EmergencyLightRenderer;
import crimson_twilight.immersive_energy.client.render.FluidBatteryRenderer;
import crimson_twilight.immersive_energy.client.shader.ShaderHandler;
import crimson_twilight.immersive_energy.common.CommonProxy;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.IEnGUIList;
import crimson_twilight.immersive_energy.common.blocks.BlockTypes_OresIEn;
import crimson_twilight.immersive_energy.common.blocks.metal.BlockTypes_Generators0;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityEmergencyLight;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityGasBurner;
import crimson_twilight.immersive_energy.common.blocks.multiblock.MultiblockFluidBattery;
import crimson_twilight.immersive_energy.common.blocks.multiblock.TileEntityFluidBattery;
import crimson_twilight.immersive_energy.common.compat.IEnCompatModule;
import crimson_twilight.immersive_energy.common.items.ItemIEnBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ImmersiveEnergy.MODID)
public class ClientProxy extends CommonProxy
{
	public static final String CAT_IEN = "ien";
	
	@Override
	public void preInit()
	{
		OBJLoader.INSTANCE.addDomain(ImmersiveEnergy.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveEnergy.MODID);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(EvenMoreImmersiveModelRegistry.instance);

		super.preInit();
		for(IEnCompatModule compat : IEnCompatModule.modules)
			try
			{
				compat.clientPreInit();
			} catch(Exception exception)
			{
				IELogger.error("Compat module for "+compat+" could not be client pre-initialized");
			}

		EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IEnContent.toolHeftyWrench, 1, 0), new ImmersiveModelRegistry.ItemModelReplacement_OBJ(ImmersiveEnergy.MODID+":models/item/hefty_wrench.obj", true)
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, new Matrix4().scale(.0625, .0625, .0625).translate(12, 0, -8.25))
				.setTransformations(TransformType.FIRST_PERSON_LEFT_HAND, new Matrix4().scale(-.0625, .0625, .0625).translate(-12, 0, -8.25))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, new Matrix4().scale(.0625, .0625, .0625).translate(0.25, 4, 1.5).scale(1.1, 1.1, 1.1))
				.setTransformations(TransformType.THIRD_PERSON_LEFT_HAND, new Matrix4().scale(.0625, .0625, .0625).translate(-0.25, 4, 1.5).scale(1.1, 1.1, 1.1))
				.setTransformations(TransformType.FIXED, new Matrix4().scale(.0625, .0625, .0625).translate(.125, .125, -.25).scale(.3125, .3125, .3125).rotate(Math.PI, 0, 1, 0).rotate(Math.PI*.25, 0, 0, 1))
				.setTransformations(TransformType.GUI, new Matrix4().scale(.0625, .0625, .0625).translate(0,0.25,7.25).rotate(-Math.PI*52/180f, 1, 0, 0).rotate(Math.PI*31/180f, 0, 1, 0).rotate(Math.PI*46/180f, 0, 0, 1).scale(0.9,0.9,0.9))
				.setTransformations(TransformType.GROUND, new Matrix4().scale(.0625, .0625, .0625).translate(0.75, 6.75, -0.5)));
		IEnContent.toolHeftyWrench.setTileEntityItemStackRenderer(ItemRendererIEOBJ.INSTANCE);


		//45, -135, 45

		/*
		EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IEnContent.toolUpgradeableCrossbow, 1, 0), new EvenMoreImmersiveModelRegistry.ItemModelReplacement_OBJ("immersiveengineering:models/item/chemthrower.obj", true)
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, new Matrix4().scale(.375, .375, .375).translate(-.25, 1, .5).rotate(Math.PI*.5, 0, 1, 0))
				.setTransformations(TransformType.FIRST_PERSON_LEFT_HAND, new Matrix4().scale(-.375, .375, .375).translate(-.25, 1, .5).rotate(-Math.PI*.5, 0, 1, 0))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, new Matrix4().translate(0, .75, .1875).scale(.5, .5, .5).rotate(Math.PI*.75, 0, 1, 0).rotate(Math.PI*.375, 0, 0, 1).rotate(-Math.PI*.25, 1, 0, 0))
				.setTransformations(TransformType.THIRD_PERSON_LEFT_HAND, new Matrix4().translate(0, .75, .1875).scale(.5, -.5, .5).rotate(Math.PI*.75, 0, 1, 0).rotate(Math.PI*.625, 0, 0, 1).rotate(-Math.PI*.25, 1, 0, 0))
				.setTransformations(TransformType.FIXED, new Matrix4().translate(.125, .125, -.25).scale(.3125, .3125, .3125).rotate(Math.PI, 0, 1, 0).rotate(Math.PI*.25, 0, 0, 1))
				.setTransformations(TransformType.GUI, new Matrix4().translate(-.1875, .3125, 0).scale(.4375, .4375, .4375).rotate(-Math.PI*.6875, 0, 1, 0).rotate(-Math.PI*.125, 0, 0, 1))
				.setTransformations(TransformType.GROUND, new Matrix4().translate(0, .25, .125).scale(.25, .25, .25)));
		IEnContent.toolUpgradeableCrossbow.setTileEntityItemStackRenderer(ItemRendererIEOBJ.INSTANCE);
		 */

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidBattery.class, new FluidBatteryRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmergencyLight.class, new EmergencyLightRenderer());

	}
	
	@Override
	public void preInitEnd()
	{
		super.preInitEnd();
	}
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt)
	{
		WireApi.registerConnectorForRender("solar_panel", new ResourceLocation(ImmersiveEnergy.MODID+":block/solar_panel.obj"), null);
		WireApi.registerConnectorForRender("empty", new ResourceLocation(ImmersiveEnergy.MODID+":block/empty.obj"), null);

		for (Block block : IEnContent.registeredIEnBlocks)
		{
			Item blockItem = Item.getItemFromBlock(block);
			final ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
			if (loc != null)
			{
				if(block instanceof IIEMetaBlock)
				{
					IIEMetaBlock ieMetaBlock = (IIEMetaBlock)block;
					if(ieMetaBlock.useCustomStateMapper())
						ModelLoader.setCustomStateMapper(block, IECustomStateMapper.getStateMapper(ieMetaBlock));
					ModelLoader.setCustomMeshDefinition(blockItem, new ItemMeshDefinition()
					{
						@Override
						public ModelResourceLocation getModelLocation(ItemStack stack)
						{
							return new ModelResourceLocation(loc, "inventory");
						}
					});
					for(int meta = 0; meta < ieMetaBlock.getMetaEnums().length; meta++)
					{
						String location = loc.toString();
						String prop = ieMetaBlock.appendPropertiesToState()?("inventory,"+ieMetaBlock.getMetaProperty().getName()+"="+ieMetaBlock.getMetaEnums()[meta].toString().toLowerCase(Locale.US)): null;
						if(ieMetaBlock.useCustomStateMapper())
						{
							String custom = ieMetaBlock.getCustomStateMapping(meta, true);
							if(custom!=null)
								location += "_"+custom;
						}
						try
						{
							ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, prop));
						} catch(NullPointerException npe)
						{
							throw new RuntimeException("WELP! apparently "+ieMetaBlock+" lacks an item!", npe);
						}
					}
				}
				else
					ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
			}
		}

		for(Item item : IEnContent.registeredIEnItems)
		{
			if(item instanceof ItemIEnBase)
			{
				ItemIEnBase ienMetaItem = (ItemIEnBase) item;
				if(ienMetaItem.registerSubModels && ienMetaItem.getSubNames() != null && ienMetaItem.getSubNames().length > 0)
				{
					for(int meta = 0; meta < ienMetaItem.getSubNames().length; meta++)
					{
						ResourceLocation loc = new ResourceLocation("immersive_energy", ienMetaItem.itemName + "/" + ienMetaItem.getSubNames()[meta]);

						ModelBakery.registerItemVariants(ienMetaItem, loc);
						ModelLoader.setCustomModelResourceLocation(ienMetaItem, meta, new ModelResourceLocation(loc, "inventory"));
					}
				}
				else
				{
					final ResourceLocation loc = new ResourceLocation("immersive_energy", ienMetaItem.itemName);
					ModelBakery.registerItemVariants(ienMetaItem, loc);
					ModelLoader.setCustomMeshDefinition(ienMetaItem, new ItemMeshDefinition()
					{
						@Override
						public ModelResourceLocation getModelLocation(ItemStack stack)
						{
							return new ModelResourceLocation(loc, "inventory");
						}
					});
				}
				
			}
			else
			{
				final ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
				ModelBakery.registerItemVariants(item, loc);
				ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition()
				{
					@Override
					public ModelResourceLocation getModelLocation(ItemStack stack)
					{
						return new ModelResourceLocation(loc, "inventory");
					}
				});
			}
		}

		
	}

	@Override
	public void init()
	{
		super.init();
		for(IEnCompatModule compat : IEnCompatModule.modules)
			try
			{
				compat.clientInit();
			} catch(Exception exception)
			{
				IELogger.error("Compat module for "+compat+" could not be client initialized");
			}
	}

	public static void onModelBakeEvent(ModelBakeEvent event)
	{
		
	}

	@Override
	public void postInit()
	{
		super.postInit();
		for(IEnCompatModule compat : IEnCompatModule.modules)
			try
			{
				compat.clientPostInit();
			} catch(Exception exception)
			{
				IELogger.error("Compat module for "+compat+" could not be client post-initialized");
			}

		ManualHelper.addEntry("oresIEn", CAT_IEN, 
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "oresThorium", new ItemStack(IEnContent.blockOre, 1, BlockTypes_OresIEn.THORIUM.getMeta())),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "oresTungsten", new ItemStack(IEnContent.blockOre, 1, BlockTypes_OresIEn.TUNGSTEN.getMeta()))
				);
		HashMap<String, Float> solar_table = new HashMap<String, Float>();
		solar_table.put("Cold Biome", 0.7f);
		solar_table.put("Medium Biome", 1.0f);
		solar_table.put("Warm Biome", 1.3f);
		solar_table.put("Ocean Biome", 1.1f);
		String[][] table = formatToTable_ItemFloatHashmap(solar_table, "");
		ManualHelper.addEntry("solar_panel", CAT_IEN, 
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "solar_item", new ItemStack(IEnContent.itemMaterial, 1, 1)),
				new ManualPages.Crafting(ManualHelper.getManual(), "solar_blueprint", BlueprintCraftingRecipe.getTypedBlueprint("solar")),
				new ManualPageBlueprint(ManualHelper.getManual(), "solar_item1", new ItemStack(IEnContent.itemMaterial, 1, 1)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "portable_solar", new ItemStack(IEnContent.blockGenerators0, 1, BlockTypes_Generators0.SOLAR_PANEL.getMeta())),
				new ManualPageBlueprint(ManualHelper.getManual(), "portable_solar1", new ItemStack(IEnContent.blockGenerators0, 1, BlockTypes_Generators0.SOLAR_PANEL.getMeta())),
				new ManualPages.Table(ManualHelper.getManual(), "solar_table", table, false)
				);
		ManualHelper.addEntry("hefty_wrench", CAT_IEN, 
				new ManualPageBlueprint(ManualHelper.getManual(), "hefty_wrench1", new ItemStack(IEnContent.toolHeftyWrench)),
				new ManualPages.Crafting(ManualHelper.getManual(), "hefty_wrench_blueprint", BlueprintCraftingRecipe.getTypedBlueprint("advanced_tool")),
				new ManualPageBlueprint(ManualHelper.getManual(), "hefty_wrench_upgrades", new ItemStack(IEnContent.itemUpgrades, 1, 0), new ItemStack(IEnContent.itemUpgrades, 1, 1), new ItemStack(IEnContent.itemUpgrades, 1, 2)),
				new ManualPages.Text(ManualHelper.getManual(), "hefty_wrench_upgrades2"),
				new ManualPageBlueprint(ManualHelper.getManual(), "hefty_wrench_capacitor", new ItemStack(IEnContent.itemUpgrades, 1, 3)),
				new ManualPages.Text(ManualHelper.getManual(), "hefty_wrench_enchanting")
				);

		ManualHelper.addEntry("fluid_battery", CAT_IEN,
				new ManualPageMultiblock(ManualHelper.getManual(),"fluid_batter0y", MultiblockFluidBattery.instance)
		);
	}
	
	public void renderTile(TileEntity te)
	{
		GlStateManager.pushMatrix();
		GlStateManager.rotate(-90, 0, 1, 0);
		GlStateManager.translate(0, 1, -4);
		
		
		TileEntitySpecialRenderer<TileEntity> tesr = TileEntityRendererDispatcher.instance.getRenderer((TileEntity) te);
		
		tesr.render((TileEntity) te, 0, 0, 0, 0, 0, 0);
		GlStateManager.popMatrix();

	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack main = player.getHeldItemMainhand();
		ItemStack off = player.getHeldItemOffhand();
		if(tile instanceof IGuiTile)
		{
			Object gui = null;
			if (ID==IEnGUIList.GUI_GAS_BURNER && tile instanceof TileEntityGasBurner)
			{
				gui = new GUIGasBurner(player.inventory, (TileEntityGasBurner)tile);
			}
			
			((IGuiTile)tile).onGuiOpened(player, true);
			return gui;
		}
		return null;
	}

	static String[][] formatToTable_ItemIntHashmap(Map<String, Integer> map, String valueType)
	{
		@SuppressWarnings("unchecked")
		Entry<String, Integer>[] sortedMapArray = map.entrySet().toArray(new Entry[0]);
		ArrayList<String[]> list = new ArrayList<>();
		try
		{
			for(Entry<String, Integer> entry : sortedMapArray)
			{
				String item = entry.getKey();
				if(ApiUtils.isExistingOreName(entry.getKey()))
				{
					ItemStack is = OreDictionary.getOres(entry.getKey()).get(0);
					if(!is.isEmpty())
						item = is.getDisplayName();
				}

				if(item!=null)
				{
					int bt = entry.getValue();
					String am = bt+" "+valueType;
					list.add(new String[]{item, am});
				}
			}
		} catch(Exception e)
		{
		}
		return list.toArray(new String[0][]);
	}

	static String[][] formatToTable_ItemFloatHashmap(Map<String, Float> map, String valueType)
	{
		@SuppressWarnings("unchecked")
		Entry<String, Float>[] sortedMapArray = map.entrySet().toArray(new Entry[0]);
		ArrayList<String[]> list = new ArrayList<>();
		try
		{
			for(Entry<String, Float> entry : sortedMapArray)
			{
				String item = entry.getKey();
				if(ApiUtils.isExistingOreName(entry.getKey()))
				{
					ItemStack is = OreDictionary.getOres(entry.getKey()).get(0);
					if(!is.isEmpty())
						item = is.getDisplayName();
				}

				if(item!=null)
				{
					float bt = entry.getValue();
					String am = bt+" "+valueType;
					list.add(new String[]{item, am});
				}
			}
		} catch(Exception e)
		{
		}
		return list.toArray(new String[0][]);
	}

}
