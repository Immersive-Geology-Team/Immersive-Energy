package crimson_twilight.immersive_energy.common.util.network;

import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.util.MessageItemNightVisionSwitch;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class IEnPacketHandler 
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ImmersiveEnergy.MODID);
	
	public static void preInit()
	{
		INSTANCE.registerMessage(MessageItemNightVisionSwitch.Handler.class, MessageItemNightVisionSwitch.class, 1, Side.SERVER);
	}
	
	public static void init()
	{
		
	}
	
}
