package crimson_twilight.immersive_energy.common.entities;

import blusunrize.immersiveengineering.common.util.Utils;
import crimson_twilight.immersive_energy.INail;
import crimson_twilight.immersive_energy.common.util.IEnDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityIEnNail extends Entity
{
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;
    private boolean inGround;
    public EntityLivingBase shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private float movementDecay = 0;
    private float gravity = 0;

    private int tickLimit = 40;
    private ItemStack nailStack;
    protected int damage = 0;
    boolean resetHurt = false;
    boolean setFire = false;

    private static final DataParameter<String> dataMarker_shooter = EntityDataManager.createKey(EntityIEnNail.class, DataSerializers.STRING);

    public EntityIEnNail(World world)
    {
        super(world);
        this.setSize(.125f, .125f);
    }

    public EntityIEnNail(World world, ItemStack stack, int damage)
    {
        super(world);
        this.setSize(.125f, .125f);
        this.nailStack = stack;
        this.damage = damage;
    }

    public EntityIEnNail(World world, double x, double y, double z, ItemStack stack, int damage)
    {
        super(world);
        this.setSize(0.125F, 0.125F);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        this.nailStack = stack;
        this.damage = damage;
    }

    public EntityIEnNail(World world, EntityLivingBase shooter, double ax, double ay, double az, ItemStack stack, int damage)
    {
        super(world);
        this.shootingEntity = shooter;
        setShooterSynced();
        this.setSize(0.125F, 0.125F);
        this.setLocationAndAngles(shooter.posX+ax, shooter.posY+shooter.getEyeHeight()+ay, shooter.posZ+az, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.nailStack = stack;
        this.damage = damage;
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(dataMarker_shooter, "");
    }

    public Entity setDamage(int damage) {
        this.damage = damage;
        return this;
    }

    public void setTickLimit(int limit)
    {
        this.tickLimit = limit;
    }

    public void setMovementDecay(float f)
    {
        this.movementDecay = f;
    }

    public void setGravity(float f)
    {
        this.gravity = f;
    }

    public void setShooterSynced()
    {
        this.dataManager.set(dataMarker_shooter, this.shootingEntity.getName());
    }

    public EntityLivingBase getShooterSynced()
    {
        return this.world.getPlayerEntityByName(this.dataManager.get(dataMarker_shooter));
    }

    public EntityLivingBase getShooter()
    {
        return this.shootingEntity;
    }

    @Override
    public void onUpdate() {
        if(this.getShooter() == null && this.world.isRemote)
            this.shootingEntity = getShooterSynced();
        if(!this.world.isRemote&&(this.shootingEntity!=null&&this.shootingEntity.isDead)) {
            this.setDead();
            return;
        }

        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        if(iblockstate.getMaterial()!= Material.AIR)
        {
            AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);

            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        super.onUpdate();

        if(this.inGround)
        {
            this.onExpire();
            this.setDead();
            return;
        }
        ++this.ticksInAir;

        if(ticksInAir >= tickLimit)
        {
            this.onExpire();
            this.setDead();
            return;
        }

        Vec3d vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec31 = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3, vec31);
        vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        vec31 = new Vec3d(this.posX+this.motionX, this.posY+this.motionY, this.posZ+this.motionZ);

        if(movingobjectposition!=null)
            vec31 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);


        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double d0 = 0.0D;

        for(int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);
            if(entity1.canBeCollidedWith()&&(!entity1.isEntityEqual(this.shootingEntity)))
            {
                float f = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(f);
                RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                if(movingobjectposition1!=null)
                {
                    double d1 = vec3.distanceTo(movingobjectposition1.hitVec);
                    if(d1 < d0||d0==0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if(entity!=null)
            movingobjectposition = new RayTraceResult(entity);

        if(movingobjectposition!=null)
            this.onImpact(movingobjectposition);


        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f1 = MathHelper.sqrt(this.motionX*this.motionX+this.motionZ*this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX)*180.0D/Math.PI)+90.0F;

        for(this.rotationPitch = (float)(Math.atan2((double)f1, this.motionY)*180.0D/Math.PI)-90.0F; this.rotationPitch-this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
            ;

        while(this.rotationPitch-this.prevRotationPitch >= 180.0F)
            this.prevRotationPitch += 360.0F;
        while(this.rotationYaw-this.prevRotationYaw < -180.0F)
            this.prevRotationYaw -= 360.0F;
        while(this.rotationYaw-this.prevRotationYaw >= 180.0F)
            this.prevRotationYaw += 360.0F;

        this.rotationPitch = this.prevRotationPitch+(this.rotationPitch-this.prevRotationPitch)*0.2F;
        this.rotationYaw = this.prevRotationYaw+(this.rotationYaw-this.prevRotationYaw)*0.2F;

        float decay = movementDecay;
        if(this.isInWater())
        {
            float f3 = 0.25F;
            for(int j = 0; j < 4; ++j)
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX-this.motionX*(double)f3, this.posY-this.motionY*(double)f3, this.posZ-this.motionZ*(double)f3, this.motionX, this.motionY, this.motionZ);
            decay *= .9;
        }

        if(decay!=0)
        {
            this.motionX *= decay;
            this.motionY *= decay;
            this.motionZ *= decay;
        }
        if(gravity!=0)
            this.motionY -= gravity;

        if(ticksExisted%4==0)
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected void onImpact(RayTraceResult mop) {
        boolean headshot = false;
        if(mop.entityHit instanceof EntityLivingBase)
            headshot = Utils.isVecInEntityHead((EntityLivingBase) mop.entityHit, new Vec3d(posX, posY, posZ));

        if(this.nailStack != ItemStack.EMPTY) {
            INail nail = (INail) this.nailStack.getItem();
            if(!world.isRemote&&mop.entityHit!=null&&this.shootingEntity!=null)
            {
                int damage = headshot ? this.damage * 2 : this.damage;
                if (mop.entityHit.attackEntityFrom(IEnDamageSources.causePenetratingNailDamage(this, this.shootingEntity), damage))
                {
                    if(resetHurt) mop.entityHit.hurtResistantTime = 0;
                    if(setFire) mop.entityHit.setFire(2);
                }
            }
        }
        if(mop.typeOfHit== RayTraceResult.Type.BLOCK)
        {
            IBlockState state = this.world.getBlockState(mop.getBlockPos());
            if(state.getBlock().getMaterial(state)!=Material.AIR)
                state.getBlock().onEntityCollidedWithBlock(this.world, mop.getBlockPos(), state, this);
        }
        this.setDead();
    }

    public void onExpire()
    {

    }

    protected float getMotionFactor()
    {
        return 0.95F;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.xTile = nbt.getShort("xTile");
        this.yTile = nbt.getShort("yTile");
        this.zTile = nbt.getShort("zTile");
        this.inTile = Block.getBlockById(nbt.getByte("inTile")&255);
        this.inData = nbt.getInteger("inData");
        this.inGround = nbt.getByte("inGround")==1;
        this.nailStack = new ItemStack(nbt.getCompoundTag("nailStack"));

        if(nbt.hasKey("direction", 9))
        {
            NBTTagList nbttaglist = nbt.getTagList("direction", 6);
            this.motionX = nbttaglist.getFloatAt(0);
            this.motionY = nbttaglist.getFloatAt(1);
            this.motionZ = nbttaglist.getFloatAt(2);
        }
        else
        {
            this.setDead();
        }

        if(this.world!=null)
            this.shootingEntity = this.world.getPlayerEntityByName(nbt.getString("shootingEntity"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setShort("xTile", (short)this.xTile);
        nbt.setShort("yTile", (short)this.yTile);
        nbt.setShort("zTile", (short)this.zTile);
        nbt.setByte("inTile", (byte)Block.getIdFromBlock(this.inTile));
        nbt.setInteger("inData", this.inData);
        nbt.setByte("inGround", (byte)(this.inGround?1: 0));
        nbt.setTag("direction", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
        nbt.setTag("nailStack", this.nailStack.writeToNBT(new NBTTagCompound()));

    }
}
