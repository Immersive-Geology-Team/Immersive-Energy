package crimson_twilight.immersive_energy.common.entities;

import crimson_twilight.immersive_energy.common.util.IArrowLogic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/*
Thanks to Darkhax's Simply Arrows for the basis of this.
 */
public class EntityIEnArrow extends EntityTippedArrow
{
	private ItemStack arrowStack;
    private IArrowLogic logic;
    private boolean ignore_invulnerability = false;
    
    public EntityIEnArrow(World world) 
    {
    	super(world);
	}
    
    public EntityIEnArrow(World world, ItemStack stack, EntityLivingBase shooter) 
    {
        super(world, shooter);
        this.arrowStack = stack;
    }

    public EntityIEnArrow(World worldIn, ItemStack stack, double x, double y, double z)
    {
        super(worldIn, x, y, z);
        this.arrowStack = stack;
    }

    
    @Override
    protected ItemStack getArrowStack() 
    {
        return this.arrowStack;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) 
    {
        super.writeToNBT(compound);
        compound.setTag("ArrowStack", this.arrowStack.writeToNBT(new NBTTagCompound()));
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) 
    {
        super.readFromNBT(compound);
        this.arrowStack = new ItemStack(compound.getCompoundTag("ArrowStack"));
    }

    @Override
    public void arrowHit(EntityLivingBase target) 
    {
    	if(this.ignore_invulnerability)
    	{
    		target.hurtResistantTime = 0;
    	}
        super.arrowHit(target);

        if (this.logic != null) 
        {
            this.logic.onEntityHit(this, target);
        }
    }

    @Override
    public void onUpdate() 
    {
        super.onUpdate();

        if(!this.getEntityWorld().isRemote && this.inGround && this.logic != null) 
        {
            final Vec3d vecCurrent = new Vec3d(this.posX, this.posY, this.posZ);
            final Vec3d vecProjected = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            final RayTraceResult hit = this.world.rayTraceBlocks(vecCurrent, vecProjected, false, true, false);

            if(hit != null && hit.typeOfHit == Type.BLOCK) 
            {
                this.logic.onBlockHit(this, hit);
            }
        }
    }

    public IArrowLogic getLogic() 
    {
        return this.logic;
    }

    public void setLogic(IArrowLogic logic) 
    {
        this.logic = logic;
    }

    public boolean getIgnoreInvulnerability() 
    {
        return this.ignore_invulnerability;
    }

    public EntityIEnArrow setIgnoreInvulnerability(boolean ignore) 
    {
        this.ignore_invulnerability = ignore;
        return this; 
    }
}
