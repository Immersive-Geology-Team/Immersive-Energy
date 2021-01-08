package crimson_twilight.immersive_energy.common.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class IEnKeybinds 
{
	public static KeyBinding helmet_night_vision;

	@SideOnly(Side.CLIENT)
	public static void register()
	{
		helmet_night_vision = new KeyBinding("key.helmet_night_vision", Keyboard.KEY_G, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(helmet_night_vision);
	}
}
