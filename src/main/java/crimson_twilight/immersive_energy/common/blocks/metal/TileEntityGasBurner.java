package crimson_twilight.immersive_energy.common.blocks.metal;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlacementInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import crimson_twilight.immersive_energy.api.crafting.GasBurnerRecipe;
import crimson_twilight.immersive_energy.api.energy.FuelHandler;
import crimson_twilight.immersive_energy.common.Config.IEnConfig.Machines;
import crimson_twilight.immersive_energy.common.IEnGUIList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityGasBurner extends TileEntityIEBase implements IIEInventory, ITickable, ITileDrop, IGuiTile, IPlayerInteraction, IPlacementInteraction
{
	public boolean active;
	public int cookTime;
	public int burnTime;
	public int cookMax = 160;
	public double heat;
	public double heatMax = 2400;
	public double heatMin = 450;
	public int cookNeeded = cookMax;
	public FluidTank tank = new FluidTank(Machines.burnerCapacity)
	{
		@Override
		public boolean canFillFluidType(FluidStack fluid)
		{
			return fluid!=null&&FuelHandler.isValidFuel(fluid.getFluid());
		}

		;
	};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

	public static boolean isCookableFood(ItemStack stack)
	{
		ItemStack result = GasBurnerRecipe.getResult(stack);
		if(!result.isEmpty())
			return true;
		result = FurnaceRecipes.instance().getSmeltingResult(stack);
		return !result.isEmpty()&&(result.getItem() instanceof ItemFood||result.getItem() instanceof ItemPotion);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY||capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY||super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		return super.getCapability(capability, facing);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		active = nbt.getBoolean("active");
		tank.readFromNBT(nbt.getCompoundTag("tank"));
		burnTime=nbt.getInteger("burnTime");
		cookTime=nbt.getInteger("cookTime");
		cookNeeded=nbt.getInteger("cookNeeded");
		heat=nbt.getInteger("heat");

		if(!descPacket)
		{
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 2);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(!descPacket)
		{
			nbt.setTag("inventory", Utils.writeInventory(inventory));
		}
		nbt.setBoolean("active", active);
		nbt.setInteger("burnTime", burnTime);
		nbt.setInteger("cookTime", cookTime);
		nbt.setInteger("cookNeeded", cookNeeded);
		nbt.setDouble("heat", heat);
		writeTank(nbt,false);

	}

	@Override
	public void update()
	{
		this.active = false;
		if(!world.isRemote)
		{
			if(burnTime > 0)
			{
				this.heat = Math.min(this.heat + 1, this.heatMax);
				active = true;
				this.burnTime = Math.max(this.burnTime - 1, 0);
				markContainingBlockForUpdate(null);
			}
			else if(this.heat > 0)
			{
				this.heat = Math.max(this.heat - 1, 0);
				markContainingBlockForUpdate(null);
			}
			
		}
		if(!this.world.isRemote)
		{
			if(this.tank.getFluid() != null)
			{
				Fluid fuel = tank.getFluid().getFluid();
				if(FuelHandler.isValidFuel(fuel))
				{
					if(this.tank.getFluidAmount() > 0)
					{
						if(burnTime==0 && canCook())
						{
							burnTime = FuelHandler.getTickPermb(fuel);
							this.tank.drain(new FluidStack(fuel, 1), true);
							active = true;
						}
					}
				}
			}
		}
		if(!this.world.isRemote)
		{
			if(active)
			{
				if(canCook() && this.heat >= this.heatMin)
				{
					this.cookTime++;
					ItemStack stack = inventory.get(0);
					ItemStack result = GasBurnerRecipe.getResult(stack).copy();
					if(result.isEmpty())
						result = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
					ItemStack stack1 = inventory.get(1);
					this.cookNeeded = (this.cookMax - (int) (152 * (this.heat-this.heatMin) / (this.heatMax-this.heatMin) ) );
					if(this.cookTime >= cookNeeded)
					{
						this.cookTime = 0;
						if(stack1.isEmpty())
						{
							this.inventory.set(1, result);
						}
						else
						{
							stack1.grow(result.getCount());
						}
						stack.shrink(1);
					}
				}
			}
			else
			{
				if(canCook() && this.cookTime > 0)
				{
					this.cookTime--;
				}
				else
				{
					this.cookTime = 0;
				}
			} 
		}
	}

	private boolean canCook()
	{
		ItemStack stack = this.inventory.get(0);
		if(stack.isEmpty())
		{
			return false;
		}
		else
		{
			if(!isCookableFood(stack))
			{
				return false;
			}
			else
			{
				ItemStack result = GasBurnerRecipe.getResult(stack).copy();
				if(result.isEmpty())
					result = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
				ItemStack stack1 = this.inventory.get(1);
				if(stack1.isEmpty())
				{
					return true;
				}
				else if(!result.isItemEqual(stack1))
				{
					return false;
				}
				else
				{
					return stack1.getCount()+result.getCount() <= result.getMaxStackSize();
				}
			}
		}
	}

	public void readTank(NBTTagCompound nbt)
	{
		this.tank.readFromNBT(nbt.getCompoundTag("tank"));
	}

	public void writeTank(NBTTagCompound nbt, boolean toItem)
	{
		boolean write = this.tank.getFluidAmount() > 0;
		NBTTagCompound tankTag = this.tank.writeToNBT(new NBTTagCompound());
		if(write)
			nbt.setTag(toItem?"Fluid":"tank", tankTag);
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			readTank(stack.getTagCompound());
		}
	}

	@Override
	public ItemStack getTileDrop(EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		NBTTagCompound tag = new NBTTagCompound();
		writeTank(tag, true);
		if(!tag.hasNoTags())
			stack.setTagCompound(tag);
		return stack;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IEnGUIList.GUI_GAS_BURNER;
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return this;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return this.inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot == 0)
			return canCook();
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return this.inventory.get(slot).getMaxStackSize();
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	@Override
	public void onTilePlaced(World world, BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase placer, ItemStack stack)
	{
		FluidStack fs = FluidUtil.getFluidContained(stack);
		if(fs!=null)
			this.tank.setFluid(fs);
	}
}
