package crimson_twilight.immersive_energy.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 10.07.2020
 */
public interface IHeatableArmor
{
	void modifyHeat(ItemStack stack, float amount);
	
	float getHeatCap(ItemStack stack);

	float getHeat(ItemStack stack);

	float getMaxHeat(ItemStack stack);

	void performOverheatEffects(ItemStack stack, EntityLivingBase holder);
}
