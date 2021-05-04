package crimson_twilight.immersive_energy.common.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.Config;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;

public class ImmersiveIntelligenceHelper extends IEnCompatModule
{
	@Override
	public void preInit()
	{
		IEnCompatModule.ii=true;
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