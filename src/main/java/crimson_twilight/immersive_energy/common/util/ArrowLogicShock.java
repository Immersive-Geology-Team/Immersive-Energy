package crimson_twilight.immersive_energy.common.util;

import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import crimson_twilight.immersive_energy.common.entities.EntityIEnArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

/*
Thanks to Darkhax's Simply Arrows for the basis of this.
*/
public class ArrowLogicShock implements IArrowLogic
{
	private final float electric_damage;
	
	public ArrowLogicShock(float electric_damage) 
	{
		this.electric_damage = electric_damage;
	}
	
	@Override
	public void onEntityHit(EntityIEnArrow arrow, EntityLivingBase target) 
	{
		IArrowLogic.super.onEntityHit(arrow, target);
		target.hurtResistantTime = 0;
		ElectricDamageSource dmgsrc = IEDamageSources.causeTeslaDamage(this.electric_damage, true);
		if(dmgsrc.apply(target))
		{
			target.addPotionEffect(new PotionEffect(IEPotions.stunned, 128));
		}
	}
}
