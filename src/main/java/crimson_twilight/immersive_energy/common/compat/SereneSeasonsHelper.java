package crimson_twilight.immersive_energy.common.compat;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import sereneseasons.api.season.SeasonHelper;

public class SereneSeasonsHelper extends IEnCompatModule
{
	public static float calculateModifier(World world, Biome biome, float modifier)
	{
		try
		{
			switch(SeasonHelper.getSeasonState(world).getSubSeason())
			{
				case EARLY_SPRING:
					modifier *= 0.8f;
					break;
				case MID_SPRING:
					modifier *= 0.9f;
					break;
				case LATE_SPRING:
					modifier *= 1f;
					break;
				case EARLY_SUMMER:
					modifier *= 1.1f;
					break;
				case MID_SUMMER:
					modifier *= 1.2f;
					break;
				case LATE_SUMMER:
					modifier *= 1.1f;
					break;
				case EARLY_AUTUMN:
					modifier *= 1f;
					break;
				case MID_AUTUMN:
					modifier *= 0.9f;
					break;
				case LATE_AUTUMN:
					modifier *= 0.8f;
					break;
				case EARLY_WINTER:
					modifier *= 0.7f;
					break;
				case MID_WINTER:
					modifier *= 0.6f;
					break;
				case LATE_WINTER:
					modifier *= 0.7f;
					break;
			}
		} catch(Exception ignored)
		{

		}

		switch(biome.getTempCategory())
		{
			case OCEAN:
				modifier *= 1.1f;
				break;
			case COLD:
				modifier *= 0.7f;
				break;
			case MEDIUM:
				modifier *= 1f;
				break;
			case WARM:
				modifier *= 1.3f;
				break;
		}
		return modifier;
	}

	@Override
	public void preInit()
	{
		IEnCompatModule.serene=true;
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		//for compat that requires events
		//MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void postInit()
	{

	}
}