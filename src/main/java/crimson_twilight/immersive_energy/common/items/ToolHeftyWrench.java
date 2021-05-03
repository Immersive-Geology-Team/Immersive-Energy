package crimson_twilight.immersive_energy.common.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IConfigurableSides;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityChargingStation;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IItemDamageableIE;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.RotationUtil;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.advancements.IEAdvancements;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.IEnContent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SuppressWarnings("deprecation")
public class ToolHeftyWrench extends ItemUpgradeableTool implements ITool, IItemDamageableIE, IIEEnergyItem, IOBJModelCallback<ItemStack>
{
	public static final int DURABILITY = 25600;
	public static final double ATTACK_DAMAGE = 6d;
	public static final double THERMAL_DAMAGE = 2d;
	public static final float ELECTRIC_DAMAGE = 6f;
	public static final double MAGICAL_DAMAGE = 4d;

	public static BaseAttribute basicDamage = new RangedAttribute(SharedMonsterAttributes.ATTACK_DAMAGE, "immersive_energy.basicdamage", ATTACK_DAMAGE, 0D, Double.MAX_VALUE);
	public static BaseAttribute thermalDamage = new RangedAttribute(null, "immersive_energy.thermaldamage", 0.0D, 0.0D, Double.MAX_VALUE);
	public static BaseAttribute electricDamage = new RangedAttribute(null, "immersive_energy.electricdamage", 0.0D, 0.0D, Double.MAX_VALUE);
	public static BaseAttribute magicalDamage = new RangedAttribute(null, "immersive_energy.magicaldamage", 0.0D, 0.0D, Double.MAX_VALUE);

	
	public static List<Predicate<TileEntity>> tileEntitiesToIgnore = new ArrayList<>();
	
	static
	{
		tileEntitiesToIgnore.add(tileEntity -> tileEntity instanceof TileEntityChargingStation);
	}
	
	public ToolHeftyWrench() 
	{
		super("hefty_wrench", 1, "HEFTYWRENCH");
		this.setHasSubtypes(false);
		this.setMaxDamage(DURABILITY);
		fixupItem();
	}
	
	@Override
	public boolean isRepairable() 
	{
		return super.isRepairable();
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) 
	{
		return EnumAction.BOW;
	}
	
	@Override
	public int getItemEnchantability() 
	{
		return 12;
	}

	//Borrowed from II, which borrowed it from Alternating Flux - Pabilo8
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

	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.8D, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ATTACK_DAMAGE, 0));
		}

		return multimap;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) 
	{
		return true;
	}

	@Override
	public boolean shouldRenderGroup(ItemStack stack, String group)
	{
		if(group.contains("UpgradeCapacitor"))
			return hasCapacitor(stack) ||hasHVCapacitor(stack);
		else if(group.equals("Z_TubeMagic"))
			return hasMagic(stack);
		else if(group.equals("Z_TubePowered"))
			return hasElectro(stack);
		else if(group.equals("Z_TubeThermal"))
			return hasThermal(stack);
		else if (group.contains("UpgradeElectronTubes") || group.contains("A_ElectronTube"))
		{
			return hasMagic(stack)||hasElectro(stack)||hasThermal(stack);
		}
		return true;
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) 
	{
		if(attacker.limbSwingAmount == 0)
			return true;
		else 
			return false;
	}
	
    public double getAttackDamage()
    {
        return ToolHeftyWrench.ATTACK_DAMAGE;
    }

	private boolean hasElectro(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("electro");
	}

	private boolean hasThermal(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("thermal");
	}

	private boolean hasMagic(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("magic");
	}

	private boolean hasCapacitor(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("capacitor");
	}

	private boolean hasHVCapacitor(ItemStack stack)
	{
		return getUpgrades(stack).hasKey("hv_capacitor");
	}
	
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
    	int drain_mult = hasHVCapacitor(stack) ? 2 : 1;
    	double hv_bonus = hasHVCapacitor(stack) ? 1.5 : 1;
    	int drain = (1600 - 300 * EnchantmentHelper.getEfficiencyModifier(attacker)) * drain_mult;
		if(drain < 0) drain = 0;
		if(drain > 1600 * drain_mult) drain = 1600 * drain_mult;
    	Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
		if(hasElectro(stack) && this.extractEnergy(stack, drain, true) >= drain)
		{
    		ItemNBTHelper.setInt(stack, "energy", this.getEnergyStored(stack)-drain);
			target.hurtResistantTime = 0;
			ElectricDamageSource dmgsrc = IEDamageSources.causeTeslaDamage(ToolHeftyWrench.ELECTRIC_DAMAGE * (float)hv_bonus, true);
			if(dmgsrc.apply(target))
			{
				target.addPotionEffect(new PotionEffect(IEPotions.stunned, 128));
			}
		}
		if(hasThermal(stack) && this.extractEnergy(stack, drain, true) >= drain)
		{
    		ItemNBTHelper.setInt(stack, "energy", this.getEnergyStored(stack)-drain);
			int duration = 4 + 4 * EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
			target.setFire((int)(duration * hv_bonus));
			attack(stack, target, multimap, thermalDamage, DamageSource.IN_FIRE, attacker);
		}
		if(hasMagic(stack) && this.extractEnergy(stack, drain, true) >= drain)
		{
    		ItemNBTHelper.setInt(stack, "energy", this.getEnergyStored(stack)-drain);
			attack(stack, target, multimap, magicalDamage, DamageSource.MAGIC, attacker);
			target.addPotionEffect(new PotionEffect(IEPotions.flashed, 128));
		}
        stack.damageItem(1, attacker);
        return true;
    }
    
    public void attack(ItemStack stack, EntityLivingBase target, Multimap<String, AttributeModifier> multimap, BaseAttribute attribute, DamageSource source, EntityLivingBase attacker) 
    {
		float amount = getAmount(stack, target, multimap, attribute);
		if (amount > 0) 
		{
			amount *= ((EntityPlayer) attacker).getCooledAttackStrength(0);
			target.hurtResistantTime = 0;
			target.attackEntityFrom(source, amount);
		}
	}
    
    public void normalAttack(ItemStack stack, EntityLivingBase target, BaseAttribute attribute, EntityLivingBase attacker, float mult) 
    {
		float amount = (float)(ToolHeftyWrench.ATTACK_DAMAGE * mult);
		if (amount > 0) 
		{
			target.hurtResistantTime = 0;
			target.attackEntityFrom(DamageSource.GENERIC, amount);
		}
	}

	public float getAmount(ItemStack stack, EntityLivingBase target, Multimap<String, AttributeModifier> multimap, BaseAttribute attribute) 
	{
		Collection<AttributeModifier> gsd;

		float amount = 0;
		gsd = multimap.get(attribute.getName());

		if (gsd != null)
		{
			for (AttributeModifier t : gsd) 
			{
				float d0 = (float) t.getAmount();
				if (t.getID() == Item.ATTACK_DAMAGE_MODIFIER) 
				{
					d0 += (double) EnchantmentHelper.getModifierForCreature(stack, target.getCreatureAttribute());
				}
				amount += d0;
			}
		}
		return amount;
	}
    
	@Override
	public boolean canModify(ItemStack stack) 
	{
		return stack.getMetadata()!=1;
	}

	@Override
	public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
	{
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		return new Slot[]
				{
						new IESlot.Upgrades(container, inv, 0, 100, 26, "HEFTYWRENCH_HEAD", stack, true),
						new IESlot.Upgrades(container, inv, 1, 100, 48, "HEFTYWRENCH_HANDLE", stack, true)
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
	public int getSlotCount(ItemStack stack) 
	{
		return 2;
	}

	@Override
	public void recalculateUpgrades(ItemStack stack)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		clearUpgrades(stack);
		IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		NBTTagCompound upgradeTag = getUpgradeBase(stack).copy();
		if(inv!=null)
		{
			for(int i = 0; i < inv.getSlots(); i++)
			{
				ItemStack u = inv.getStackInSlot(i);
				if(!u.isEmpty()&&u.getItem() instanceof IUpgrade)
				{
					IUpgrade upg = (IUpgrade)u.getItem();
					upg.applyUpgrades(stack, u, upgradeTag);
				}
			}
			ItemNBTHelper.setTagCompound(stack, "upgrades", upgradeTag);
			if(this.getEnergyStored(stack) > this.getMaxEnergyStored(stack))
				ItemNBTHelper.setInt(stack, "energy", this.getMaxEnergyStored(stack));
			finishUpgradeRecalculation(stack);
		}
	}

	@Override
	public void clearUpgrades(ItemStack stack)
	{
		super.clearUpgrades(stack);
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{

	}

	@Override
	public void finishUpgradeRecalculation(ItemStack stack) 
	{
		super.finishUpgradeRecalculation(stack);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		int drain_mult = hasHVCapacitor(stack) ? 2 : 1;
		double hv_bonus = hasHVCapacitor(stack) ? 1.5 : 1;
		int drain = (1600 - 300 * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) * drain_mult;
		if(drain < 0) drain = 0;
		if(drain > 1600 * drain_mult) drain = 1600 * drain_mult;
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		if(slot==EntityEquipmentSlot.MAINHAND && this.getEnergyStored(stack) >= drain)
		{
			if(hasMagic(stack))
			{
				multimap.put(magicalDamage.getName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", MAGICAL_DAMAGE * hv_bonus, 0));
			}
			if(hasElectro(stack))
			{
				multimap.put(electricDamage.getName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ELECTRIC_DAMAGE * hv_bonus, 0));
			}
			if(hasThermal(stack))
			{
				multimap.put(thermalDamage.getName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (THERMAL_DAMAGE+EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)) * hv_bonus, 0));
			}
		}
		return multimap;

	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		int i = ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE);
		if(i > stack.getItemDamage())
			ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, stack.getItemDamage());
		
		double randomNum = Math.random();
		if(EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0 && randomNum > 0.96d)
		{
			if(i > 0)
			{
				ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, i - 1);
				stack.setItemDamage(ItemNBTHelper.getInt(stack, Lib.NBT_DAMAGE));
			}
			else if(this.getEnergyStored(stack) < this.getMaxEnergyStored(stack) && this.getEnergyStored(stack) < 1600)
			{
				ItemNBTHelper.setInt(stack, "energy", this.getEnergyStored(stack)+1);
			}
		}
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

		if(curDamage >= DURABILITY)
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
		stack.setItemDamage(curDamage);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if(stack.isItemEnchanted()) return false;
		return enchantment==Enchantments.EFFICIENCY||enchantment==Enchantments.UNBREAKING||enchantment==Enchantments.MENDING||enchantment==Enchantments.FIRE_ASPECT;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		player.swingArm(hand);
		if(performHammerFunctions(player,world,pos,side,hitX,hitY,hitZ,hand))
		{
		    return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	//Shares code with II, long live IEn-II Cooperation!
	boolean performHammerFunctions(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		String[] permittedMultiblocks = null;
		String[] interdictedMultiblocks = null;
		if(ItemNBTHelper.hasKey(stack, "multiblockPermission"))
		{
			NBTTagList list = stack.getTagCompound().getTagList("multiblockPermission", 8);
			permittedMultiblocks = new String[list.tagCount()];
			for(int i = 0; i < permittedMultiblocks.length; i++)
				permittedMultiblocks[i] = list.getStringTagAt(i);
		}
		if(ItemNBTHelper.hasKey(stack, "multiblockInterdiction"))
		{
			NBTTagList list = stack.getTagCompound().getTagList("multiblockInterdiction", 8);
			interdictedMultiblocks = new String[list.tagCount()];
			for(int i = 0; i < interdictedMultiblocks.length; i++)
				interdictedMultiblocks[i] = list.getStringTagAt(i);
		}
		for(MultiblockHandler.IMultiblock mb : MultiblockHandler.getMultiblocks())
			if(mb.isBlockTrigger(world.getBlockState(pos)))
			{
				boolean b = permittedMultiblocks==null;
				if(permittedMultiblocks!=null)
					for(String s : permittedMultiblocks)
						if(mb.getUniqueName().equalsIgnoreCase(s))
						{
							b = true;
							continue;
						}
				if(!b)
					break;
				if(interdictedMultiblocks!=null)
					for(String s : interdictedMultiblocks)
						if(mb.getUniqueName().equalsIgnoreCase(s))
						{
							b = false;
							continue;
						}
				if(!b)
					break;
				if(MultiblockHandler.fireMultiblockFormationEventPre(player, mb, pos, stack).isCanceled())
					continue;
				if(mb.createStructure(world, pos, side, player))
				{
					if(player instanceof EntityPlayerMP)
						IEAdvancements.TRIGGER_MULTIBLOCK.trigger((EntityPlayerMP)player, mb, stack);
					return true;
				}
			}
		
		TileEntity tile = world.getTileEntity(pos);
		IBlockState state = world.getBlockState(pos);
		if(tile==null)
			return false;


		if(tile instanceof IConfigurableSides&&!world.isRemote)
		{
			int iSide = player.isSneaking()?side.getOpposite().ordinal(): side.ordinal();
			if(((IConfigurableSides)tile).toggleSide(iSide, player))
				return true;
		}
		else if(tileEntitiesToIgnore.stream().noneMatch(tileEntityPredicate -> tileEntityPredicate.test(tile))
				&&tile instanceof IDirectionalTile&&((IDirectionalTile)tile).canHammerRotate(side, hitX, hitY, hitZ, player)&&!world.isRemote)
		{
			EnumFacing f = ((IDirectionalTile)tile).getFacing();
			EnumFacing oldF = f;
			int limit = ((IDirectionalTile)tile).getFacingLimitation();

			if(limit==0)
				f = EnumFacing.VALUES[(f.ordinal()+1)%EnumFacing.VALUES.length];
			else if(limit==1)
				f = player.isSneaking()?f.rotateAround(side.getAxis()).getOpposite(): f.rotateAround(side.getAxis());
			else if(limit==2||limit==5)
				f = player.isSneaking()?f.rotateYCCW(): f.rotateY();
			((IDirectionalTile)tile).setFacing(f);
			((IDirectionalTile)tile).afterRotation(oldF, f);
			tile.markDirty();
			world.notifyBlockUpdate(pos, state, state, 3);
			world.addBlockEvent(tile.getPos(), tile.getBlockType(), 255, 0);
			return true;
		}
		else if(tile instanceof IHammerInteraction&&!world.isRemote)
		{
			boolean b = ((IHammerInteraction)tile).hammerUseSide(side, player, hitX, hitY, hitZ);
			if(b)
				return b;
		}

		return false;
	}

	public int getMaxDamageIE(ItemStack stack)
	{
		return DURABILITY;
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

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		if(ItemNBTHelper.hasKey(stack, "linkingPos"))
		{
			int[] link = ItemNBTHelper.getIntArray(stack, "linkingPos");
			if(link!=null&&link.length > 3)
				list.add(I18n.format(Lib.DESC_INFO+"attachedToDim", link[1], link[2], link[3], link[0]));
		}
		if(ItemNBTHelper.hasKey(stack, "multiblockPermission"))
		{
			NBTTagList tagList = stack.getTagCompound().getTagList("multiblockPermission", 8);
			String s = I18n.format(Lib.DESC_INFO+"multiblocksAllowed");
			if(!GuiScreen.isShiftKeyDown())
				list.add(s+" "+I18n.format(Lib.DESC_INFO+"holdShift"));
			else
			{
				list.add(s);
				for(int i = 0; i < tagList.tagCount(); i++)
					list.add(TextFormatting.DARK_GRAY+" "+I18n.format(Lib.DESC_INFO+"multiblock."+tagList.getStringTagAt(i)));
			}
		}
		if(ItemNBTHelper.hasKey(stack, "multiblockInterdiction"))
		{
			NBTTagList tagList = stack.getTagCompound().getTagList("multiblockInterdiction", 8);
			String s = I18n.format(Lib.DESC_INFO+"multiblockForbidden");
			if(!GuiScreen.isShiftKeyDown())
				list.add(s+" "+I18n.format(Lib.DESC_INFO+"holdShift"));
			else
			{
				list.add(s);
				for(int i = 0; i < tagList.tagCount(); i++)
					list.add(TextFormatting.DARK_GRAY+" "+I18n.format(Lib.DESC_INFO+"multiblock."+tagList.getStringTagAt(i)));
			}
		}

		if(hasElectro(stack)) 
		{
			list.add(TextFormatting.AQUA+I18n.format(Lib.DESC+"electro"));
		}
		if(hasThermal(stack)) 
		{
			list.add(TextFormatting.RED+I18n.format(Lib.DESC+"thermal"));
		}
		if(hasMagic(stack)) 
		{
			list.add(TextFormatting.LIGHT_PURPLE+I18n.format(Lib.DESC+"magic"));
		}
		if(this.getMaxEnergyStored(stack) > 0) 
		{
			String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
			list.add(I18n.format(Lib.DESC+"info.energyStored", stored));
		}
		float integrity = 100-(float)getDurabilityForDisplay(stack)*100f;
		list.add(String.format("%s %.2f %%", I18n.format(Lib.DESC_INFO+"integrity"), integrity));
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
	
	public static float getChargeUpDamageModifyer(int charge)
    {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.5F)
        {
            f = 1.5F;
        }

        return f;
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) 
	{
		if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if(getEntityPlayerLookedAt((float) entityplayer.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()) != null)
            {
            	Entity entity = getEntityPlayerLookedAt((float) entityplayer.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());
            	if(entity instanceof EntityLivingBase)
            	{
            		Multimap<String, AttributeModifier> multimap = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            		EntityLivingBase target = (EntityLivingBase) entity;
            		float j = getChargeUpDamageModifyer(i);
            		if(ToolHeftyWrench.ATTACK_DAMAGE * j >= 1)
            		{
                    	normalAttack(stack, target, basicDamage, entityLiving, j);
            		}
            	}
            }
        }
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		player.setActiveHand(hand);
		if(performHammerFunctions(player,world,pos,side,hitX,hitY,hitZ,hand))
		{
			System.out.print("hammer");
		    return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		return !player.world.isRemote&RotationUtil.rotateEntity(entity, player);
	}

	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
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
	public boolean getIsRepairable(ItemStack stack, ItemStack material)
	{
		return Utils.compareToOreName(material, "ingotSteel");
	}
	
	@Override
	public int getMaxEnergyStored(ItemStack stack) 
	{
		int storage = 0;
		if(hasElectro(stack)||hasThermal(stack)||hasMagic(stack)) storage += 1600;
		if(hasHVCapacitor(stack)) storage += 1600;
		if(hasCapacitor(stack)) storage += 28800;
		return storage;
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

	@Override
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("II_ADVANCED_HAMMER");
	}

	@Override
	public boolean isTool(ItemStack item)
	{
		return true;
	}
	
	public static Entity getEntityPlayerLookedAt(float dist)
	{
	    Minecraft mc = FMLClientHandler.instance().getClient();
	    Entity theRenderViewEntity = mc.getRenderViewEntity();
	    AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
	            theRenderViewEntity.posX-0.5D,
	            theRenderViewEntity.posY-0.0D,
	            theRenderViewEntity.posZ-0.5D,
	            theRenderViewEntity.posX+0.5D,
	            theRenderViewEntity.posY+1.5D,
	            theRenderViewEntity.posZ+0.5D
	            );
	    RayTraceResult returnMOP = null;
	    if (mc.world != null)
	    {
	        double var2 = dist;
	        returnMOP = theRenderViewEntity.rayTrace(var2, 0);
	        double calcdist = var2;
	        Vec3d pos = theRenderViewEntity.getPositionEyes(0);
	        var2 = calcdist;
	        if (returnMOP != null)
	        {
	            calcdist = returnMOP.hitVec.distanceTo(pos);
	        }
	         
	        Vec3d lookvec = theRenderViewEntity.getLook(0);
	        Vec3d var8 = pos.addVector(lookvec.x * var2, 
	              lookvec.y * var2, 
	              lookvec.z * var2);
	        Entity pointedEntity = null;
	        float var9 = 1.0F;
	        List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(
	              theRenderViewEntity, theViewBoundingBox.grow(
	                    lookvec.x * var2, 
	                    lookvec.y * var2, 
	                    lookvec.z * var2).expand(var9, var9, var9));
	        double d = calcdist;

	        for (Entity entity : list)
	        {
	            if (entity.canBeCollidedWith())
	            {
	                float bordersize = entity.getCollisionBorderSize();
	                AxisAlignedBB aabb = new AxisAlignedBB(
	                      entity.posX-entity.width/2, 
	                      entity.posY, 
	                      entity.posZ-entity.width/2, 
	                      entity.posX+entity.width/2, 
	                      entity.posY+entity.height, 
	                      entity.posZ+entity.width/2);
	                aabb.expand(bordersize, bordersize, bordersize);
	                RayTraceResult mop0 = aabb.calculateIntercept(pos, var8);
	                    
	                if (aabb.contains(pos))
	                {
	                    if (0.0D < d || d == 0.0D)
	                    {
	                        pointedEntity = entity;
	                        d = 0.0D;
	                    }
	                } else if (mop0 != null)
	                {
	                    double d1 = pos.distanceTo(mop0.hitVec);
	                        
	                    if (d1 < d || d == 0.0D)
	                    {
	                        pointedEntity = entity;
	                        d = d1;
	                    }
	                }
	            }
	        }
	           
	        if (pointedEntity != null && (d < calcdist || returnMOP == null))
	        {
	            return pointedEntity;
	        }
	    }
	    return null;
	}
}
