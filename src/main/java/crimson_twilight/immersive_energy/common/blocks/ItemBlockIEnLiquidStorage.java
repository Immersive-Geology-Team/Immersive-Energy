package crimson_twilight.immersive_energy.common.blocks;

import crimson_twilight.immersive_energy.api.energy.FuelHandler;
import crimson_twilight.immersive_energy.common.Config.IEnConfig.Machines;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class ItemBlockIEnLiquidStorage extends ItemBlockIEnBase
{

	public ItemBlockIEnLiquidStorage(Block b)
	{
		super(b);
		this.maxStackSize = 16;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		if(!stack.isEmpty())
			return new FluidHandlerItemStack(stack, Machines.burnerCapacity)
			{
				@Override
				public boolean canFillFluidType(FluidStack fluid)
				{
					return FuelHandler.isValidFuel(fluid.getFluid());
				}
			};
		return null;
	}

}
