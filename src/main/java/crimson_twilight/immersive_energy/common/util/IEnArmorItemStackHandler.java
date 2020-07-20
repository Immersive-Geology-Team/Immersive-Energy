package crimson_twilight.immersive_energy.common.util;

import crimson_twilight.immersive_energy.common.items.ItemUpgradeableArmor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 09.07.2020
 *
 * but actually
 * @author BluSunrize
 */
public class IEnArmorItemStackHandler extends ItemStackHandler implements ICapabilityProvider
{
	private boolean first = true;
	private ItemStack stack;

	public IEnArmorItemStackHandler(ItemStack stack)
	{
		super();
		this.stack = stack;
	}

	@Nonnull
	private Runnable onChange = () -> {
	};

	public void setTile(TileEntity tile)
	{
		if(tile!=null)
		{
			onChange = tile::markDirty;
		}
		else
		{
			onChange = () -> {
			};
		}
	}

	public void setInventoryForUpdate(IInventory inv)
	{
		if(inv!=null)
		{
			onChange = inv::markDirty;
		}
		else
		{
			onChange = () -> {
			};
		}
	}

	@Override
	protected void onContentsChanged(int slot)
	{
		super.onContentsChanged(slot);
		onChange.run();
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(first)
		{
			int idealSize = ((ItemUpgradeableArmor)stack.getItem()).getSlotCount();
			NonNullList<ItemStack> newList = NonNullList.withSize(idealSize, ItemStack.EMPTY);
			for(int i = 0; i < Math.min(stacks.size(), idealSize); i++)
				newList.set(i, stacks.get(i));
			stacks = newList;
			stack = ItemStack.EMPTY;
			first = false;
		}
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)this;
		return null;
	}

	public NonNullList<ItemStack> getContainedItems()
	{
		return stacks;
	}
}
