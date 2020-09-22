package crimson_twilight.immersive_energy.common.util;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class IEnKeybinds 
{
	public static KeyBinding helmet_night_vision;
	
	public static void Register()
	{
		helmet_night_vision = new KeyBinding("key.helmet_night_vision", Keyboard.KEY_G, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(helmet_night_vision);
	}
}
