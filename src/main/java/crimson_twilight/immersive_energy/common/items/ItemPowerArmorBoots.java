package crimson_twilight.immersive_energy.common.items;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import com.google.common.collect.Multimap;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.client.model.PowerArmorModel;
import crimson_twilight.immersive_energy.common.IEnContent;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ImmersiveEnergy.MODID)
public class ItemPowerArmorBoots extends ItemUpgradeableArmor implements IElectricEquipment, IUpgradeableTool
{
	private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
	public ItemPowerArmorBoots()
	{
		super(IEnContent.powerArmor, EntityEquipmentSlot.FEET, "POWERSUIT_BOOTS");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		float integrity = 100-(float)getDurabilityForDisplay(stack)*100f;
		list.add(String.format("%s %.2f %%", I18n.format(Lib.DESC_INFO+"integrity"), integrity));
		super.addInformation(stack,world,list,flag);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack)
	{
		return 0.1f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
									ModelBiped _default)
	{
		return getModel(armorSlot,itemStack);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return super.getArmorTexture(stack, entity, slot, type);
	}

	public static void playBlockedSound(EntityLivingBase entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isRemote)
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_CLOTH_STEP, SoundCategory.PLAYERS, 1, 1);
	}

	public static void playReducedSound(EntityLivingBase entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isRemote)
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.8f, 0.4f);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
	{

		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

		if(equipmentSlot==this.armorType)
		{
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Power Armor Movement Speed Debuff", -.02, 2));
		}
		return multimap;
	}

	@Override
	public void onStrike(ItemStack s, EntityEquipmentSlot eqSlot, EntityLivingBase p, Map<String, Object> cache,
						 @Nullable DamageSource dSource, ElectricSource eSource)
	{
		if(!(dSource instanceof ElectricDamageSource))
			return;
		ElectricDamageSource dmg = (ElectricDamageSource)dSource;
		if(dmg.source.level <= 2)
		{
			if(cache.containsKey("power_armor_suit_boots"))
			{
				cache.put("power_armor_suit_boots", (1<<armorType.getIndex())|((Integer)cache.get("powerpower_armor_suit_boots_armor_suit")));
			}
			else
			{
				cache.put("power_armor_suit_boots", 1<<armorType.getIndex());
			}
			if(dmg.dmg*100 <= IEConfig.machines.capacitorHV_input&&cache.containsKey("power_armor_suit_helmet")&&cache.containsKey("power_armor_suit_chest")&&cache.containsKey("power_armor_suit_legs")&&cache.containsKey("power_armor_suit_boots"))
			{

			}
			else
			{
				dmg.dmg *= 1.2;
				if((!(p instanceof EntityPlayer)||!((EntityPlayer)p).capabilities.isCreativeMode)&&s.attemptDamageItem(2, itemRand, (dmg.getTrueSource() instanceof EntityPlayerMP)?(EntityPlayerMP)dmg.getTrueSource(): null))
					s.damageItem((int)(dmg.dmg*50), p);
			}
		}
		else
		{
			dmg.dmg *= 1.2;
			if((!(p instanceof EntityPlayer)||!((EntityPlayer)p).capabilities.isCreativeMode)&&s.attemptDamageItem(2, itemRand, (dmg.getTrueSource() instanceof EntityPlayerMP)?(EntityPlayerMP)dmg.getTrueSource(): null))
				s.damageItem((int)(dmg.dmg*50), p);
		}
	}

	@Override
	public float getHeatCap(ItemStack stack) 
	{
		return 8000 + getUpgrades(stack).getFloat("heat_protection");
	}
}
