package crimson_twilight.immersive_energy.common.items;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import crimson_twilight.immersive_energy.common.Config.IEnConfig.Tools;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemToolUpgradeIEn extends ItemIEnBase implements IUpgrade
{

	public enum ToolUpgrades
	{

		WRENCH_ELECTRIC(ImmutableSet.of("HEFTYWRENCH_HEAD"), 1, (target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("thermal")&&!((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("magic"), (upgrade, modifications) -> modifications.setBoolean("electro", true)),
		WRENCH_THERMAL(ImmutableSet.of("HEFTYWRENCH_HEAD"), 1, (target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("electro")&&!((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("magic"), (upgrade, modifications) -> modifications.setBoolean("thermal", true)),
		WRENCH_MAGIC(ImmutableSet.of("HEFTYWRENCH_HEAD"), 1, (target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("thermal")&&!((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("electro"), (upgrade, modifications) -> modifications.setBoolean("magic", true)),
		WRENCH_CAPACITOR(ImmutableSet.of("HEFTYWRENCH_HANDLE"), (upgrade, modifications) -> modifications.setBoolean("capacitor", true)),

		ARMOR_PROTECTION_PLATES(ImmutableSet.of("POWERSUIT_HELMET", "POWERSUIT_CHEST", "POWERSUIT_LEGGS", "POWERSUIT_BOOTS"), 3, (upgrade, modifications) -> {
			int armor = modifications.getInteger("armor_increase");
			modifications.setInteger("armor_increase", armor+upgrade.getCount()*Tools.armor_plates_upgrade_resist);
		}),
		ARMOR_HEAT_PROTECTION(ImmutableSet.of("POWERSUIT_HELMET", "POWERSUIT_CHEST", "POWERSUIT_LEGGS", "POWERSUIT_BOOTS"), 3, (upgrade, modifications) -> modifications.setInteger("heat_protection", upgrade.getCount()*Tools.heat_upgrade_resist)),
		ARMOR_HELMET_VOLTMETER(ImmutableSet.of("POWERSUIT_HELMET"), 1, (upgrade, modifications) -> modifications.setBoolean("voltmeter", true));

		//CROSSBOW_ELECTRIC(ImmutableSet.of("CROSSBOW"), (upgrade, modifications) -> modifications.setBoolean("electro", true) ),
		//CROSSBOW_FAST(ImmutableSet.of("CROSSBOW"), (upgrade, modifications) -> modifications.setBoolean("fast", true) );
		//CROSSBOW_HOWITZER_CONVERSION_KIT(ImmutableSet.of("CROSSBOW"), (upgrade, modifications) -> modifications.setBoolean("howitzer", true) ); //pls add

		private ImmutableSet<String> toolset;
		private int stackSize = 1;
		private BiPredicate<ItemStack, ItemStack> applyCheck;
		private BiConsumer<ItemStack, NBTTagCompound> function;

		ToolUpgrades(ImmutableSet<String> toolset, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, 1, function);
		}

		ToolUpgrades(ImmutableSet<String> toolset, int stackSize, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, stackSize, null, function);
		}

		ToolUpgrades(ImmutableSet<String> toolset, int stackSize, BiPredicate<ItemStack, ItemStack> applyCheck, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this.toolset = toolset;
			this.stackSize = stackSize;
			this.applyCheck = applyCheck;
			this.function = function;
		}

		static String[] parse()
		{
			String[] ret = new String[values().length];
			for(int i = 0; i < ret.length; i++)
				ret[i] = values()[i].toString().toLowerCase(Locale.US);
			return ret;
		}

		static ToolUpgrades get(int meta)
		{
			if(meta >= 0&&meta < values().length)
				return values()[meta];
			return WRENCH_ELECTRIC;
		}

	}

	public ItemToolUpgradeIEn()
	{
		super("toolupgrade", 1, ToolUpgrades.parse());
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		if(stack.getItemDamage() < getSubNames().length)
		{
			String[] flavour = ImmersiveEngineering.proxy.splitStringOnWidth(I18n.format(Lib.DESC_FLAVOUR+"toolupgrade."+this.getSubNames()[stack.getItemDamage()]), 200);
			for(String s : flavour)
				list.add(s);
		}
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return ToolUpgrades.get(stack.getMetadata()).stackSize;
	}

	@Override
	public Set<String> getUpgradeTypes(ItemStack upgrade)
	{
		return ToolUpgrades.get(upgrade.getMetadata()).toolset;
	}

	@Override
	public boolean canApplyUpgrades(ItemStack target, ItemStack upgrade)
	{
		BiPredicate<ItemStack, ItemStack> check = ToolUpgrades.get(upgrade.getMetadata()).applyCheck;
		if(check!=null&&target.getItem() instanceof IUpgradeableTool)
			return check.test(target, upgrade);
		return true;
	}

	@Override
	public void applyUpgrades(ItemStack target, ItemStack upgrade, NBTTagCompound modifications)
	{
		ToolUpgrades.get(upgrade.getMetadata()).function.accept(upgrade, modifications);
	}

}
