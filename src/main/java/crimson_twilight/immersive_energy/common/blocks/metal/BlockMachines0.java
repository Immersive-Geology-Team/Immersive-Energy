package crimson_twilight.immersive_energy.common.blocks.metal;

import java.util.Arrays;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.blocks.BlockIEnTileProvider;
import crimson_twilight.immersive_energy.common.blocks.ItemBlockIEnLiquidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockMachines0 extends BlockIEnTileProvider<BlockTypes_Machines0>
{
	public static final AxisAlignedBB GAS_BURNER_AABB = new AxisAlignedBB(0.34375, 0, 0.34375, 0.65625, 0.75, 0.65625);
	
	public BlockMachines0() 
	{
		super("metal_machines0",Material.IRON, PropertyEnum.create("type", BlockTypes_Machines0.class), ItemBlockIEnLiquidStorage.class, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE, IOBJModelCallback.PROPERTY);
		this.setHardness(3.0F);
		this.setResistance(15.0F);
		lightOpacity = 0;
		this.setMetaBlockLayer(BlockTypes_Machines0.GAS_BURNER.getMeta(), BlockRenderLayer.CUTOUT);
		this.setNotNormalBlock(BlockTypes_Machines0.GAS_BURNER.getMeta());
	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		return null;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (getMetaFromState(state) == BlockTypes_Machines0.GAS_BURNER.getMeta())
		{
			return GAS_BURNER_AABB;
		}
		return super.getBoundingBox(state, world, pos);
	}
	/*
	@SuppressWarnings("deprecation")
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask,
			List<AxisAlignedBB> list, Entity ent, boolean p_185477_7_) 
	{
		if (getMetaFromState(state) == BlockTypes_Generators0.SOLAR_PANEL.getMeta())
		{
			list.add(SOLAR_PANEL_BASE_AABB.offset(pos));
		}
		super.addCollisionBoxToList(state, world, pos, mask, list, ent, p_185477_7_);
	} */

	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState ext = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof TileEntityImmersiveConnectable))
				return state;
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable)te).genConnBlockstate());
		}
		return state;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		IBlockState downState = world.getBlockState(pos.down());
		if(stack.getItemDamage()==BlockTypes_Machines0.GAS_BURNER.getMeta())
			return downState.isTopSolid() || downState.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
		return true;
		
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) 
	{
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		if(isSideSolid(state, world, pos, side))
			return BlockFaceShape.SOLID;
		return null;
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityGasBurner)
			return side==EnumFacing.DOWN;
		return true;
	}

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) 
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityGasBurner) 
		{
			BlockPos belowPos=pos.offset(EnumFacing.DOWN);
			Block blockBelow = getBlockFrom(world, belowPos);
			IBlockState downState = world.getBlockState(pos.down());
			if(!(blockBelow.isTopSolid(downState) || downState.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID || downState.getBlock().isOpaqueCube(downState))){
				this.dropBlockAsItem(world, pos, IEnContent.blockMachines0.getStateFromMeta(BlockTypes_Machines0.GAS_BURNER.getMeta()), 0);
				world.setBlockToAir(pos);
				return;
			}
		}
		super.neighborChanged(state, world, pos, block, fromPos);
	}
		

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createBasicTE(World worldIn, BlockTypes_Machines0 type) 
	{
		switch(type) 
		{
			case GAS_BURNER:
				return new TileEntityGasBurner();
		}
		return null;
	}
	
	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		if (getMetaFromState(state) == BlockTypes_Machines0.GAS_BURNER.getMeta())
		{
			return EnumBlockRenderType.MODEL;
		}
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	/*
	@SuppressWarnings("null")
	public List<AxisAlignedBB> getSelectedBounds(IBlockState state, World worldIn, BlockPos pos) {
		List<AxisAlignedBB> bounds=null;
		if (getMetaFromState(state) == BlockTypes_Generators0.SOLAR_PANEL.getMeta())
		{
			bounds.add(SOLAR_PANEL_BASE_AABB.offset(pos));
		}
		return bounds;
	}  */
}
