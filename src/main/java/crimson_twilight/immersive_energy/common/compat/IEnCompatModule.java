package crimson_twilight.immersive_energy.common.compat;

import crimson_twilight.immersive_energy.common.Config.IEnConfig;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 20.09.2020
 */
public abstract class IEnCompatModule
{
	public static HashMap<String, Class<? extends IEnCompatModule>> moduleClasses = new HashMap<>();
	public static Set<IEnCompatModule> modules = new HashSet<>();

	public static boolean serene=false;
	public static boolean ii=false;

	static
	{
		moduleClasses.put("sereneseasons", SereneSeasonsHelper.class);
		moduleClasses.put("immersiveintelligence", ImmersiveIntelligenceHelper.class);
		moduleClasses.put("immersivepetroleum", ImmersivePetroleumHelper.class);
	}

	public static void doModulesPreInit()
	{
		for(Entry<String, Class<? extends IEnCompatModule>> e : moduleClasses.entrySet())
			if(Loader.isModLoaded(e.getKey()))
				try
				{
					Boolean enabled = IEnConfig.compat.get(e.getKey());
					if(enabled==null||!enabled)
						continue;
					IEnCompatModule m = e.getValue().newInstance();
					modules.add(m);
					m.preInit();
				} catch(Exception exception)
				{
					//IEnLogger.error("Compat module for "+e.getKey()+" could not be preInitialized. Report this and include the error message below!", exception);
				}
	}

	public static void doModulesRecipes()
	{
		for(IEnCompatModule compat : IEnCompatModule.modules)
			try
			{
				compat.registerRecipes();
			} catch(Exception exception)
			{
				//IEnLogger.error("Compat module for "+compat+" could not register recipes. Report this and include the error message below!", exception);
			}
	}

	public static void doModulesInit()
	{
		for(IEnCompatModule compat : IEnCompatModule.modules)
			try
			{
				compat.init();
			} catch(Exception exception)
			{
				//IEnLogger.error("Compat module for "+compat+" could not be initialized. Report this and include the error message below!", exception);
			}
	}

	public static void doModulesPostInit()
	{
		for(IEnCompatModule compat : IEnCompatModule.modules)
			try
			{
				compat.postInit();
			} catch(Exception exception)
			{
				//IEnLogger.error("Compat module for "+compat+" could not be postInitialized. Report this and include the error message below!", exception);
			}
	}

	//We don't want this to happen multiple times after all >_>
	public static boolean serverStartingDone = false;

	public static void doModulesLoadComplete()
	{
		if(!serverStartingDone)
		{
			serverStartingDone = true;
			for(IEnCompatModule compat : IEnCompatModule.modules)
				try
				{
					compat.loadComplete();

				} catch(Exception exception)
				{
					//IEnLogger.error("Compat module for "+compat+" could not be initialized. Report this and include the error message below!", exception);
				}
		}
	}

	public abstract void preInit();

	public abstract void registerRecipes();

	public abstract void init();

	public abstract void postInit();

	public void loadComplete()
	{
	}

	@SideOnly(Side.CLIENT)
	public void clientPreInit()
	{
	}

	@SideOnly(Side.CLIENT)
	public void clientInit()
	{
	}

	@SideOnly(Side.CLIENT)
	public void clientPostInit()
	{
	}
}
