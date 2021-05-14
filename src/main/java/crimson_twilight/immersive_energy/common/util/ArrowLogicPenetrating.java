package crimson_twilight.immersive_energy.common.util;

import crimson_twilight.immersive_energy.common.entities.EntityIEnArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/*
Thanks to Darkhax's Simply Arrows for the basis of this.
*/
public class ArrowLogicPenetrating implements IArrowLogic
{
	private final float penetrating_damage;
	
	public ArrowLogicPenetrating(float penetrating_damage) 
	{
		this.penetrating_damage = penetrating_damage;
	}
	
	@Override
	public void onEntityHit(EntityIEnArrow arrow, EntityLivingBase target) 
	{
		IArrowLogic.super.onEntityHit(arrow, target);
		if(arrow.getIgnoreInvulnerability())
			target.hurtResistantTime = 0;
		if(arrow.shootingEntity == null)
			target.attackEntityFrom(IEnDamageSources.causePenetratingArrowDamage(arrow, arrow), penetrating_damage);
		else
			target.attackEntityFrom(IEnDamageSources.causePenetratingArrowDamage(arrow, arrow.shootingEntity), penetrating_damage);
		for(ItemStack armor : target.getArmorInventoryList())
		{
			armor.damageItem((int) this.penetrating_damage * 4, target);
		}
	}
}
