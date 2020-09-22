package crimson_twilight.immersive_energy.common.blocks.multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import crimson_twilight.immersive_energy.common.blocks.BlockIEnMultiblock;
import crimson_twilight.immersive_energy.common.blocks.ItemBlockIEnBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import java.util.Arrays;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class BlockMetalMultiblock0 extends BlockIEnMultiblock<BlockTypes_MetalMultiblock0>
{
	public BlockMetalMultiblock0()
	{
		super("metal_multiblock0", Material.IRON, PropertyEnum.create("type", BlockTypes_MetalMultiblock0.class), ItemBlockIEnBase.class, IEProperties.FACING_HORIZONTAL,
				IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.MULTIBLOCKSLAVE, Properties.AnimationProperty, IOBJModelCallback.PROPERTY, IEProperties.DYNAMICRENDER);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		this.setAllNotNormalBlock();
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		if(BlockTypes_MetalMultiblock0.values()[meta].needsCustomState())
			return BlockTypes_MetalMultiblock0.values()[meta].getCustomState();
		return null;
	}

	@Override
	public TileEntity createBasicTE(World world, BlockTypes_MetalMultiblock0 type)
	{
		switch(type)
		{
			case FLUID_BATTERY:
			{
				return new TileEntityFluidBattery();
			}
		}
		return null;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}

	//Can be overriden when implementing multiblock connectors
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return super.getExtendedState(state, world, pos);
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		super.breakBlock(world, pos, state);
	}

}
