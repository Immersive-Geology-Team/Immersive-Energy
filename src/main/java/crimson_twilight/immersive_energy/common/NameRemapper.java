package crimson_twilight.immersive_energy.common;

import java.util.HashMap;
import java.util.Map;
import blusunrize.immersiveengineering.common.util.IELogger;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class NameRemapper 
{
	private static final Map<String, String> nameMap = new HashMap<>();

	static
	{
		nameMap.put("metalgenerator0", "metal_generator0");
	}

	@SuppressWarnings("unchecked")
	public static void remap(MissingMappings<?> ev)
	{
		for(MissingMappings.Mapping miss : ev.getMappings())
		{
			String newName = nameMap.get(miss.key.getResourcePath());
			if(newName!=null)
			{
				ResourceLocation newLoc = new ResourceLocation(ImmersiveEnergy.MODID, newName);

				IForgeRegistryEntry newTarget = miss.registry.getValue(newLoc);
				if(newTarget!=null)
				{
					IELogger.info("Successfully remapped RegistryEntry "+miss.key);
					miss.remap(newTarget);
				}
				else
					miss.warn();

			}
			else
			{
				//IEnLogger.error("Couldn't remap "+miss.key);
			}
		}
	}

}
