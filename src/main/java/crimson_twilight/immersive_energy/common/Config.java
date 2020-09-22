package crimson_twilight.immersive_energy.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.Maps;

import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.world.IEWorldGen;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.api.energy.FuelHandler;
import crimson_twilight.immersive_energy.common.compat.IEnCompatModule;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Config 
{

	public static HashMap<String, Boolean> manual_bool = new HashMap<String, Boolean>();
	public static HashMap<String, Integer> manual_int = new HashMap<String, Integer>();
	public static HashMap<String, int[]> manual_intA = new HashMap<String, int[]>();
	
	@net.minecraftforge.common.config.Config(modid = ImmersiveEnergy.MODID)
	public static class IEnConfig
	{
		@Comment({"A list of all mods that IEn has integrated compatability for", "Setting any of these to false disables the respective compat"})
		public static Map<String, Boolean> compat = Maps.newHashMap(Maps.toMap(IEnCompatModule.moduleClasses.keySet(), (s) -> Boolean.TRUE));

		@SubConfig
		public static Machines machines;
		@SubConfig
		public static Ores ores;
		@SubConfig
		public static Tools tools;
		
		public static class Machines
		{
			@Comment({"Power config for Solar Panels.", "Parameters: Base gen"})
			@Mapped(mapClass = Config.class, mapName = "manual_int")
			public static int base_solar = 5;
			@SuppressWarnings("static-access")
			@Comment({"Power storage config for Solar Panels.", "Parameters: Storage"})
			@Mapped(mapClass = Config.class, mapName = "manual_int")
			public static int storage_solar = IEConfig.machines.capacitorLV_storage/4;
			@Comment({"Durability of Thorium Rods.", "Parameters: durability"})
			@RangeInt(min = 1)
			public static int thoriumRodMaxDamage = 32600;
			@Comment({"Decay chance of Thorium Rods.", "Parameters: chance"})
			@RangeInt(min = 1)
			public static int thoriumRodDecay = 6538;
			@Comment({"Durability of Thorium Rods.", "Parameters: durability"})
			@RangeInt(min = 1)
			public static int uraniumRodMaxDamage = 31800;
			@Comment({"Decay chance of Uranium Rods.", "Parameters: chance"})
			@RangeInt(min = 1)
			public static int uraniumRodDecay = 5338;
			@Comment({"Fluid capacity of the Gas Burner (in mB)"})
			@RangeInt(min = 1)
			public static int burnerCapacity = 2000;
			@Comment({"List of Gas Burner fuels. Format: fluid_name, tick_per_mB_used"})
			public static String[] burner_fuels = new String[]{
					"biodiesel, 16",
					"ethanol, 8",
					"creosote, 4",
					"gasoline, 20",
					"methanol, 20"
			};
		}
		
		public static class Ores
		{
			@Comment({"Generation config for Thorium Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_thorium = new int[]{5, 8, 24, 2, 80};
			@Comment({"Generation config for Tungsten Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_tungsten = new int[]{2, 8, 24, 2, 30};
			@Comment({"Set this to false to disable the logging of the chunks that were flagged for retrogen."})
			public static boolean retrogen_log_flagChunk = true;
			@Comment({"Set this to false to disable the logging of the chunks that are still left to retrogen."})
			public static boolean retrogen_log_remaining = true;
			@Comment({"The retrogeneration key. Basically IEn checks if this key is saved in the chunks data. If it isn't, it will perform retrogen on all ores marked for retrogen.", "Change this in combination with the retrogen booleans to regen only some of the ores."})
			public static String retrogen_key = "DEFAULT";
			@Comment({"Set this to true to allow retro-generation of Thorium Ore."})
			@Mapped(mapClass = IEWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_thorium = false;
			@Comment({"Set this to true to allow retro-generation of Tungsten Ore."})
			@Mapped(mapClass = IEWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_tungsten = false;
		}
		
		public static class Tools
		{
			@Comment({"Base damage of Shocking Arrow.", "Default: 2"})
			public static int shock_arrow_regular_damage = 2;
			@Comment({"Electric damage of Shocking Arrow.", "Default: 3"})
			public static int shock_arrow_electric_damage = 3;
			@Comment({"Base knockback of Shocking Arrow.", "Default: 0"})
			public static int shock_arrow_knockback = 0;
			@Comment({"Does the Shocking Arrow ignore invunrability frames.", "Default: false"})
			public static boolean shock_arrow_ignore = false;
			@Comment({"Base damage of Piercing Arrow.", "Default: 2"})
			public static int penetrating_arrow_regular_damage = 2;
			@Comment({"Electric damage of Piercing Arrow.", "Default: 3"})
			public static int penetrating_arrow_penetrating_damage = 3;
			@Comment({"Base knockback of Piercing Arrow.", "Default: 1"})
			public static int penetrating_arrow_knockback = 1;
			@Comment({"Does the Piercing Arrow ignore invunrability frames.", "Default: true"})
			public static boolean penetrating_arrow_ignore = true;

			@Comment({"Resistance to damage added by Additional Armor Plates upgrade", "Default: 3"})
			public static int armor_plates_upgrade_resist = 3;
			@Comment({"Resistance to damage added by Additional Armor Plates upgrade", "Default: 1000"})
			public static int heat_base_resist = 1000;
			@Comment({"Resistance to heat added by each Heat Resistant Plates upgrade", "Default: 1000"})
			public static int heat_upgrade_resist = 1000;
		}

		public static void preInit(FMLPreInitializationEvent event)
		{
			onConfigUpdate();
		}
		
		private static void onConfigUpdate() 
		{
			
		}

		public static void validateAndMapValues(Class confClass)
		{
			for(Field f : confClass.getDeclaredFields())
			{
				if(!Modifier.isStatic(f.getModifiers()))
					continue;
				Mapped mapped = f.getAnnotation(Mapped.class);
				if(mapped!=null)
					try
					{
						Class c = mapped.mapClass();
						if(c!=null)
						{
							Field mapField = c.getDeclaredField(mapped.mapName());
							if(mapField!=null)
							{
								Map map = (Map)mapField.get(null);
								if(map!=null)
									map.put(f.getName(), f.get(null));
							}
						}
					} catch(Exception e)
					{
						e.printStackTrace();
					}
				else if(f.getAnnotation(SubConfig.class)!=null)
					validateAndMapValues(f.getType());
				else if(f.getAnnotation(RangeDouble.class)!=null)
					try
					{
						RangeDouble range = f.getAnnotation(RangeDouble.class);
						Object valObj = f.get(null);
						double val;
						if(valObj instanceof Double)
							val = (double)valObj;
						else
							val = (float)valObj;
						if(val < range.min())
							f.set(null, range.min());
						else if(val > range.max())
							f.set(null, range.max());
					} catch(IllegalAccessException e)
					{
						e.printStackTrace();
					}
				else if(f.getAnnotation(RangeInt.class)!=null)
					try
					{
						RangeInt range = f.getAnnotation(RangeInt.class);
						int val = (int)f.get(null);
						if(val < range.min())
							f.set(null, range.min());
						else if(val > range.max())
							f.set(null, range.max());
					} catch(IllegalAccessException e)
					{
						e.printStackTrace();
					}
			}
		}

		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.FIELD)
		public @interface Mapped
		{
			
			Class mapClass();

			String mapName();
		}

		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.FIELD)
		public @interface SubConfig
		{
		}

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent ev)
		{
			if(ev.getModID().equals(ImmersiveEnergy.MODID))
			{
				ConfigManager.sync(ImmersiveEnergy.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
				onConfigUpdate();
			}
		}
		
		public static void addBurnerFuel(String[] fuels)
		{
			for (int i = 0; i < fuels.length; i++)
			{
				String str = fuels[i];

				if (str.isEmpty()) continue;

				String fluid = null;
				int amount = 0;

				String remain = str;

				int index = 0;

				while (remain.indexOf(",") != -1)
				{
					int endPos = remain.indexOf(",");

					String current = remain.substring(0, endPos).trim();

					if (index == 0) fluid = current;

					remain = remain.substring(endPos + 1);
					index++;
				}
				String current = remain.trim();

				try
				{
					amount = Integer.parseInt(current);
					if (amount <= 0)
					{
						throw new RuntimeException("Negative value for fuel tick/mB for gas burner fuel " + (i + 1));
					}
					else
					{
						fluid = fluid.toLowerCase(Locale.ENGLISH);
						if (FluidRegistry.getFluid(fluid) != null)
						{
							FuelHandler.registerGasBurnerFuel(FluidRegistry.getFluid(fluid), amount);
						}
						else 
						{
							new RuntimeException("Invalid fluid name for gas burner fuel " + (i + 1));
						}
					}
				} catch (NumberFormatException e)
				{
					throw new RuntimeException("Invalid value for fuel tick/mB for gas burner fuel " + (i + 1));
				}
			}

		}
	}
}
