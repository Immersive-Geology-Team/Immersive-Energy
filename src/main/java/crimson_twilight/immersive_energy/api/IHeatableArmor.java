package crimson_twilight.immersive_energy.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 10.07.2020
 */
public interface IHeatableArmor
{
	void modifyHeat(ItemStack stack, int amount);

	int getHeat(ItemStack stack);

	int getMaxHeat(ItemStack stack);

	void performOverheatEffects(ItemStack stack, EntityLivingBase holder);
}
