package crimson_twilight.immersive_energy.common.util;

import crimson_twilight.immersive_energy.common.entities.EntityIEnArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;

/*
Thanks to Darkhax's Simply Arrows for the basis of this.
*/
public interface IArrowLogic 
{

    default void onEntityHit (EntityIEnArrow arrow, EntityLivingBase entity) 
    {
    }

    default void onBlockHit (EntityIEnArrow arrow, RayTraceResult hit) 
    {
    }
}
