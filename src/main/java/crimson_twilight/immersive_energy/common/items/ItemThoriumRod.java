package crimson_twilight.immersive_energy.common.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import crimson_twilight.immersive_energy.common.Config.IEnConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemThoriumRod extends ItemIEnBase
{
	public static int thoriumRodMaxDamage;

	public ItemThoriumRod()
	{
		super("stick_thorium", 16);
		thoriumRodMaxDamage = IEnConfig.Machines.thoriumRodMaxDamage;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		float integrity = 100-(float)getDurabilityForDisplay(stack)*100f;
		list.add(String.format("%s %.2f %%", I18n.format(Lib.DESC_INFO+"rodFuel"), integrity));
		if(super.getDamage(stack)!=0)
			list.add("This item is deprecated. Hold it in your inventory to update it.");
		if(super.getDamage(stack) == thoriumRodMaxDamage)
			list.add("Depleted");
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity ent, int slot, boolean hand)
	{
		Random rand = new Random();
		if(ent instanceof EntityLiving)
			if(super.getDamage(stack) != thoriumRodMaxDamage)
				if(rand.nextInt(Math.round(IEnConfig.machines.thoriumRodDecay)/stack.getCount()) == 0)
				{
					//TODO check for hazmat suit
					((EntityLivingBase)ent).setHealth(((EntityLivingBase)ent).getHealth() - 1);
					ent.performHurtAnimation();
				}
		
		if(ent instanceof EntityPlayer)
			if(super.getDamage(stack)!=0)
			{
				ItemStack fixed = new ItemStack(this);
				ItemNBTHelper.setInt(fixed, "graphDmg", stack.getItemDamage());
				((EntityPlayer)ent).inventory.setInventorySlotContents(slot, fixed);
			}
	}

	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "graphDmg")/(double)thoriumRodMaxDamage;
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return thoriumRodMaxDamage;
	}

	@Override
	public boolean isDamaged(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "graphDmg") > 0;
	}

	@Override
	public int getDamage(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "graphDmg");
	}

	@Override
	public void setDamage(ItemStack stack, int damage)
	{
		ItemNBTHelper.setInt(stack, "graphDmg", damage);
	}
}
