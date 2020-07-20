package crimson_twilight.immersive_energy.common.items;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.client.models.ModelPowerpack;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.items.ItemPowerpack;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import com.google.common.collect.Multimap;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.util.IEnArmorItemStackHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ImmersiveEnergy.MODID)
public class ItemPowerArmorChestplate extends ItemUpgradeableArmor implements IElectricEquipment, IIEEnergyItem
{
	private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
	public ItemPowerArmorChestplate()
	{
		super(IEnContent.powerArmor, EntityEquipmentSlot.CHEST, "POWERSUIT_CHEST");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		float integrity = 100-(float)getDurabilityForDisplay(stack)*100f;
		list.add(String.format("%s %.2f %%", I18n.format(Lib.DESC_INFO+"integrity"), integrity));
		String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
		list.add(I18n.format(Lib.DESC+"info.energyStored", stored));
		super.addInformation(stack,world,list,flag);
	}

	public static void playReducedSound(EntityLivingBase entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isRemote)
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.8f, 0.4f);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack)
	{
		return 0.1f;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		int energy = getEnergyStored(itemStack);
		if(energy > 0)
		{
			int pre = energy;
			for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				if(EnergyHelper.isFluxItem(player.getItemStackFromSlot(slot))&&!(player.getItemStackFromSlot(slot).getItem() instanceof ItemPowerArmorChestplate)&&!(player.getItemStackFromSlot(slot).getItem() instanceof ItemPowerpack))
					energy -= EnergyHelper.insertFlux(player.getItemStackFromSlot(slot), Math.min(energy, IEConfig.machines.capacitorHV_output), false);
			if(pre!=energy)
				EnergyHelper.extractFlux(itemStack, pre-energy, false);
		}
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return IEConfig.machines.capacitorHV_storage;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new IEnArmorItemStackHandler(stack)
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
	public void onStrike(ItemStack stack, EntityEquipmentSlot eqSlot, EntityLivingBase entity, Map<String, Object> cache,
						 @Nullable DamageSource dSource, ElectricSource eSource)
	{
		if(!(dSource instanceof ElectricDamageSource))
			return;
		ElectricDamageSource dmg = (ElectricDamageSource)dSource;
		if(dmg.source.level <= 2)
		{
			if(cache.containsKey("power_armor_suit_chest"))
			{
				cache.put("power_armor_suit_chest", (1<<armorType.getIndex())|((Integer)cache.get("power_armor_suit_chest")));
			}
			else
			{
				cache.put("power_armor_suit_chest", 1<<armorType.getIndex());
			}
			if(dmg.dmg*100 <= IEConfig.machines.capacitorHV_input&&cache.containsKey("power_armor_suit_helmet")&&cache.containsKey("power_armor_suit_chest")&&cache.containsKey("power_armor_suit_legs")&&cache.containsKey("power_armor_suit_boots"))
			{

			}
			else
			{
				dmg.dmg *= 1.2;
				if((!(entity instanceof EntityPlayer)||!((EntityPlayer)entity).capabilities.isCreativeMode)&&stack.attemptDamageItem(2, itemRand, (dmg.getTrueSource() instanceof EntityPlayerMP)?(EntityPlayerMP)dmg.getTrueSource(): null))
					stack.damageItem((int)(dmg.dmg*500), entity);
			}
		}
		else
		{
			dmg.dmg *= 1.2;
			if((!(entity instanceof EntityPlayer)||!((EntityPlayer)entity).capabilities.isCreativeMode)&&stack.attemptDamageItem(2, itemRand, (dmg.getTrueSource() instanceof EntityPlayerMP)?(EntityPlayerMP)dmg.getTrueSource(): null))
				stack.damageItem((int)(dmg.dmg*500), entity);
		}
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return "immersiveengineering:textures/models/powerpack.png";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
	{
		return ModelPowerpack.getModel();
	}
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
		
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == this.armorType)
        {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Knockback Resistance Major", 3, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Attack Speed Debuff", -.05, 2));
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.1, 2));
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Attack Damage Boost", .2, 2));
        }
        
        return multimap;
    }
}
