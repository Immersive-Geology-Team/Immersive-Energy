package crimson_twilight.immersive_energy.common.util.network;

import crimson_twilight.immersive_energy.ImmersiveEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class IEnPacketHandler 
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ImmersiveEnergy.MODID);
	
	public static void preInit()
	{
		
	}
	
}
