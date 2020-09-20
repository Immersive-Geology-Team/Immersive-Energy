/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package crimson_twilight.immersive_energy.common.blocks.multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MultiblockCrusher;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import crimson_twilight.immersive_energy.common.compat.ImmersiveIntelligenceHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TileEntityFluidBattery extends TileEntityMultiblockMetal<TileEntityFluidBattery, IMultiblockRecipe> implements IAdvancedSelectionBounds, IAdvancedCollisionBounds, IOBJModelCallback<IBlockState>
{

	@SideOnly(Side.CLIENT)
	private AxisAlignedBB renderAABB;

	public TileEntityFluidBattery()
	{
		super(MultiblockFluidBattery.instance, new int[]{3, 4, 5}, 32000, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

	}

	@Override
	protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		super.update();

	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(getPos().getX()-(facing.getAxis()==Axis.Z?2: 1), getPos().getY(), getPos().getZ()-(facing.getAxis()==Axis.X?2: 1), getPos().getX()+(facing.getAxis()==Axis.Z?3: 2), getPos().getY()+3, getPos().getZ()+(facing.getAxis()==Axis.X?3: 2));
	}

	@Override
	public float[] getBlockBounds()
	{
		/*if(pos==1||pos==3||pos==4||pos==6||pos==8||pos==11||pos==12||pos==13||pos==14||pos==24)
			return new float[]{0, 0, 0, 1, .5f, 1};
		if(pos==22)
			return new float[]{0, 0, 0, 1, .75f, 1};
		if(pos==37)
			return new float[]{0, 0, 0, 0, 0, 0};

		EnumFacing fl = facing;
		EnumFacing fw = facing.rotateY();
		if(mirrored)
			fw = fw.getOpposite();
		if(pos > 15&&pos%5 > 0&&pos%5 < 4)
		{
			float minX = 0;
			float maxX = 1;
			float minZ = 0;
			float maxZ = 1;
			if(pos%5==1)
			{
				minX = fw==EnumFacing.EAST?.1875f: 0;
				maxX = fw==EnumFacing.WEST?.8125f: 1;
				minZ = fw==EnumFacing.SOUTH?.1875f: 0;
				maxZ = fw==EnumFacing.NORTH?.8125f: 1;
			}
			else if(pos%5==3)
			{
				minX = fw==EnumFacing.WEST?.1875f: 0;
				maxX = fw==EnumFacing.EAST?.8125f: 1;
				minZ = fw==EnumFacing.NORTH?.1875f: 0;
				maxZ = fw==EnumFacing.SOUTH?.8125f: 1;
			}
			if((pos%15)/5==0)
			{
				if(fl==EnumFacing.EAST)
					minX = .1875f;
				if(fl==EnumFacing.WEST)
					maxX = .8125f;
				if(fl==EnumFacing.SOUTH)
					minZ = .1875f;
				if(fl==EnumFacing.NORTH)
					maxZ = .8125f;
			}

			return new float[]{minX, 0, minZ, maxX, 1, maxZ};
		}
		if(pos==19)
			return new float[]{facing==EnumFacing.WEST?.5f: 0, 0, facing==EnumFacing.NORTH?.5f: 0, facing==EnumFacing.EAST?.5f: 1, 1, facing==EnumFacing.SOUTH?.5f: 1};
*/
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		return null;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		BlockPos pos = getPos().add(0, -1, 0).offset(facing, -2);
		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = Utils.insertStackIntoInventory(inventoryTile, output, facing);
		if(!output.isEmpty())
			Utils.dropStackAtPos(world, pos, output, facing.getOpposite());
	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{
	}

	@Override
	public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 0;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 0;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return null;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public int[] getOutputSlots()
	{
		return null;
	}

	@Override
	public int[] getOutputTanks()
	{
		return null;
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return null;
	}

	@Override
	public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		return new IFluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resources)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return false;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{

		return super.getCapability(capability, facing);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRenderGroup(IBlockState object, String group)
	{
		boolean isIIDataPort = (!ImmersiveIntelligenceHelper.ii&&"BoxInputData".equals(group));
		//boolean isGlass = "glass".equals(group);
		return !(isIIDataPort);
	}
}