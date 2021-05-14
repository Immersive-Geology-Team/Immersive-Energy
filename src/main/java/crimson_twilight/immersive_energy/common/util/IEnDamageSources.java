package crimson_twilight.immersive_energy.common.util;

import javax.annotation.Nullable;

import crimson_twilight.immersive_energy.common.entities.EntityIEnNail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class IEnDamageSources 
{	
	public static DamageSource causePenetratingArrowDamage(EntityArrow arrow, @Nullable Entity indirectEntityIn)
	{
		return (new EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn)).setDamageBypassesArmor().setProjectile();
	}

	public static DamageSource causePenetratingNailDamage(EntityIEnNail nail, @Nullable Entity indirectEntityIn)
	{
		return (new EntityDamageSourceIndirect("nail", nail, indirectEntityIn)).setDamageBypassesArmor().setProjectile();
	}
	
	public static DamageSource causeBurningSuitDamage()
	{
		return (new DamageSource("melting_suit")).setDamageIsAbsolute().setDamageBypassesArmor().setFireDamage();
	}
}
