package crimson_twilight.immersive_energy.common.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment.ElectricSource;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IItemDamageableIE;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.IEnContent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ToolUpgradeableCrossbow extends ItemUpgradeableTool implements ITool, IItemDamageableIE, IIEEnergyItem 
{
	int durability;
	private boolean needsEnergy;
	private boolean isLoaded;
	private ItemStack ammo = ItemStack.EMPTY;
	private static final ElectricSource TC_FIELD = new ElectricSource(1);
	
	public ToolUpgradeableCrossbow() {
		super("upgradeable_crossbow", 1, "CROSSBOW");
		canRepair = false;
		this.setHasSubtypes(false);
		fixupItem();
	}

	//Stolen from II, which stole it from Alternating Flux
	public void fixupItem()
	{
		//First, get the item out of IE's registries.
		Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
		if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

		//Now, reconfigure the block to match our mod.
		this.setUnlocalizedName(ImmersiveEnergy.MODID+"."+this.itemName);
		this.setCreativeTab(ImmersiveEnergy.creativeTab);

		//And add it to our registries.
		IEnContent.registeredIEnItems.add(this);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		boolean electro = getUpgrades(stack).getBoolean("electro");
		boolean isFast = getUpgrades(stack).getBoolean("fast");
    	if(electro || isFast) 
    	{
    		needsEnergy = true;
    	} else { needsEnergy = false; }
	}
	
	protected ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
    
    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count) 
    {
    	boolean isFast = getUpgrades(stack).getBoolean("fast");
    	int recharge = isFast ? 16000 : 144000;
    	if(entity instanceof EntityPlayer && this.ammo == ItemStack.EMPTY)
    	{
    		EntityPlayer player = (EntityPlayer)entity;
    		if(count >= recharge)
    			this.ammo = this.findAmmo(player);
    	}
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        boolean flag = !this.findAmmo(playerIn).isEmpty();

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
        if (ret != null) return ret;

        if (!playerIn.capabilities.isCreativeMode && !flag)
        {
            return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
        }
        else
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 1;
    }

    public EntityArrow customizeArrow(EntityArrow arrow)
    {
        return arrow;
    }

    /**
     * Gets the velocity of the arrow entity from the bow's charge
     */
    public float getArrowVelocity(ItemStack stack)
    {
        float f = 30.0f;
        boolean isFast = getUpgrades(stack).getBoolean("fast");
		if(isFast)
			f = 50.0f;
		return f;
    }


    protected boolean isArrow(ItemStack stack)
    {
        return stack.getItem() instanceof ItemArrow;
    }

	@Override
	public boolean isTool(ItemStack arg0) 
	{
		return true;
	}

	@Override
	public boolean canModify(ItemStack stack) {
		return stack.getMetadata()!=3;
	}

	@Override
	public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
	{
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		return new Slot[]
				{
						new IESlot.Upgrades(container, inv, 1, 80, 32, "CROSSBOW", stack, true),
						new IESlot.Upgrades(container, inv, 2, 100, 48, "CROSSBOW", stack, true),
						new IESlot.Upgrades(container, inv, 3, 120, 32, "CROSSBOW", stack, true)
				};
	}

	@Nullable
	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack)
	{
		NBTTagCompound ret = super.getNBTShareTag(stack);
		if(ret==null)
			ret = new NBTTagCompound();
		else
			ret = ret.copy();
		return ret;
	}

	@Override
	public int getSlotCount(ItemStack stack) {
		return 4;
	}

	@Override
	public void recalculateUpgrades(ItemStack stack)
	{
		super.recalculateUpgrades(stack);
		if(this.getEnergyStored(stack) > this.getMaxEnergyStored(stack))
			ItemNBTHelper.setInt(stack, "energy", this.getMaxEnergyStored(stack));
	}

	@Override
	public void clearUpgrades(ItemStack stack)
	{
		super.clearUpgrades(stack);
		if(this.getEnergyStored(stack) > this.getMaxEnergyStored(stack))
			ItemNBTHelper.setInt(stack, "energy", this.getMaxEnergyStored(stack));
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		//IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}

	private void damageIETool(ItemStack stack, int amount, Random rand, @Nullable EntityPlayer player)
	{
		if(amount <= 0)
			return;

		int unbreakLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
		for(int i = 0; unbreakLevel > 0&&i < amount; i++)
			if(EnchantmentDurability.negateDamage(stack, unbreakLevel, rand))
				amount--;
		if(amount <= 0)
			return;

		int curDamage = ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE);
		curDamage += amount;

		if(player instanceof EntityPlayerMP)
			CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger((EntityPlayerMP)player, stack, curDamage);

		if(curDamage >= durability)
		{
			if(player!=null)
			{
				player.renderBrokenItemStack(stack);
				player.addStat(StatList.getObjectBreakStats(this));
			}
			stack.shrink(1);
			return;
		}
		ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, curDamage);
	}

	@Override
	public boolean isDamageable()
	{
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment==Enchantments.INFINITY||enchantment==Enchantments.UNBREAKING||enchantment==Enchantments.MENDING||enchantment==Enchantments.FLAME||enchantment==Enchantments.PUNCH||enchantment==Enchantments.POWER;
	}

	public int getMaxDamageIE(ItemStack stack)
	{
		return durability;
	}

	public int getItemDamageIE(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack stack)
	{
		ItemStack container = stack.copy();
		this.damageIETool(container, 1, Utils.RAND, null);
		return container;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
    	boolean electro = getUpgrades(stack).getBoolean("electro");
		if(electro) {
			String stored = this.getEnergyStored(stack)+"/"+1600;
			list.add(I18n.format(Lib.DESC+"info.energyStored", stored));
		}
		if(this.ammo != ItemStack.EMPTY)
		{
			list.add(I18n.format(Lib.DESC+"info.arrowType", this.ammo.getDisplayName()));
		}
		else
		{
			list.add(I18n.format(Lib.DESC+"info.empty"));
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE) > 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		double max = getMaxDamageIE(stack);
		return ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE)/max;
	}

	@Override
	public int getMaxEnergyStored(ItemStack arg0) {
		if(needsEnergy)
			return 1600;
		return 0;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IEItemStackHandler(stack)
			{
				final EnergyHelper.ItemEnergyStorage energyStorage = new EnergyHelper.ItemEnergyStorage(stack);

				@Override
				public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
				{
					return capability==CapabilityEnergy.ENERGY||super.hasCapability(capability, facing);
				}

				@SuppressWarnings("unchecked")
				@Override
				public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
				{
					if(capability==CapabilityEnergy.ENERGY)
						return (T)energyStorage;
					return super.getCapability(capability, facing);
				}
			};
		return null;
	}
}
