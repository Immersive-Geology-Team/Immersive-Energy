package crimson_twilight.immersive_energy.common.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemIEnMaterial extends ItemIEnBase
{
	public ItemIEnMaterial()
	{
		super("material", 64, "stick_tungsten", "solar_item");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
	}
}