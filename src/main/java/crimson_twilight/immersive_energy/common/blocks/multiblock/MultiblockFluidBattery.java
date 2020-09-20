/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package crimson_twilight.immersive_energy.common.blocks.multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import blusunrize.immersiveengineering.common.util.Utils;
import crimson_twilight.immersive_energy.common.IEnContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockFluidBattery implements IMultiblock
{
	static final IngredientStack[] materials = new IngredientStack[]{
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 12, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 7, BlockTypes_MetalDecoration0.GENERATOR.getMeta())),
			new IngredientStack(new ItemStack(IEContent.blockMetalDevice1, 4, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta())),
			new IngredientStack("blockSteel", 2),
			new IngredientStack("slabSteel", 1),
			new IngredientStack("blockSheetmetalSteel", 16),
			new IngredientStack("slabSheetmetalSteel", 10)
	};
	public static MultiblockFluidBattery instance = new MultiblockFluidBattery();
	static ItemStack[][][] structure = new ItemStack[3][4][5];

	static ItemStack renderStack = ItemStack.EMPTY;

	static
	{
		for(int h = 0; h < 3; h++)
			for(int l = 0; l < 4; l++)
				for(int w = 0; w < 5; w++)
					switch(h)
					{
						case 0:
						{
							if(w==0||w==4)
								structure[h][l][w]=new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
							else if (w==1||w==3)
								structure[h][l][w]=(l==0||l==3)?
										new ItemStack(IEContent.blockMetalDevice1, 1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta()):
										new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
							if(w==2)
							{
								if(l==0)
									continue;
								else if(l==3)
									structure[h][l][w]=new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.GENERATOR.getMeta());
								else
									structure[h][l][w]=new IngredientStack("blockSteel").getExampleStack();
							}
						}
						break;
						case 1:
						{
							if(w==2)
							{
								if(l>1)
									structure[h][l][w]=new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.GENERATOR.getMeta());
							}
							else
								structure[h][l][w]=new IngredientStack("blockSheetmetalSteel").getExampleStack();
						}
						break;
						case 2:
						{
							if((w==0&&l==1)||(w==4&&l==1)||(w==1&&l==3)||(w==3&&l==3))
								structure[h][l][w]=new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.GENERATOR.getMeta());
							else if(w==2&&l>1)
								structure[h][l][w]=new IngredientStack("slabSteel").getExampleStack();
							else if(w!=2)
								structure[h][l][w]=new IngredientStack("slabSheetmetalSteel").getExampleStack();
						}
						break;
					}
	}

	@Override
	public ItemStack[][][] getStructureManual()
	{
		return structure;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean overwriteBlockRender(ItemStack stack, int iterator)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderFormedStructure()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderFormedStructure()
	{
		if(renderStack.isEmpty())
			renderStack = new ItemStack(IEnContent.blockMetalMultiblocks, 1, BlockTypes_MetalMultiblock0.FLUID_BATTERY.getMeta());
		GlStateManager.translate(1, 1, 3);
		GlStateManager.rotate(-45, 0, 1, 0);
		GlStateManager.rotate(-20, 1, 0, 0);
		GlStateManager.scale(4, 4, 4);

		GlStateManager.disableCull();
		ClientUtils.mc().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.GUI);
		GlStateManager.enableCull();
	}

	@Override
	public float getManualScale()
	{
		return 12;
	}

	@Override
	public String getUniqueName()
	{
		return "IEn:FluidBattery";
	}

	@Override
	public boolean isBlockTrigger(IBlockState state)
	{
		return state.getBlock()==IEContent.blockMetalDecoration0&&(state.getBlock().getMetaFromState(state)==BlockTypes_MetalDecoration0.GENERATOR.getMeta());
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
	{
		if(side.getAxis()==Axis.Y)
			return false;
		BlockPos startPos = pos;
		side = side.getOpposite();

		boolean mirrored = false;
		boolean b = structureCheck(world, startPos, side, false);
		if(!b)
			return false;

		ItemStack hammer = player.getHeldItemMainhand().getItem().getToolClasses(player.getHeldItemMainhand()).contains(Lib.TOOL_HAMMER)?player.getHeldItemMainhand(): player.getHeldItemOffhand();
		if(MultiblockHandler.fireMultiblockFormationEventPost(player, this, pos, hammer).isCanceled())
			return false;

		IBlockState state = IEnContent.blockMetalMultiblocks.getStateFromMeta(BlockTypes_MetalMultiblock0.FLUID_BATTERY.getMeta());
		state = state.withProperty(IEProperties.FACING_HORIZONTAL, side);
		for(int l = -2; l < 2; l++)
			for(int w = -2; w <= 2; w++)
				for(int h = -1; h <= 1; h++)
				{
					if((h==-1&&w==0&&l==-2)||(h>-1&&w==0&&l<0))
						continue;
					int ww = mirrored?-w: w;
					BlockPos pos2 = startPos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

					world.setBlockState(pos2, state);
					TileEntity curr = world.getTileEntity(pos2);
					if(curr instanceof TileEntityFluidBattery)
					{
						TileEntityFluidBattery tile = (TileEntityFluidBattery)curr;
						tile.formed = true;
						tile.pos = (h+1)*20+(l+2)*5+(w+2);
						tile.offset = new int[]{(side==EnumFacing.WEST?-l+1: side==EnumFacing.EAST?l-1: side==EnumFacing.NORTH?ww: -ww), h, (side==EnumFacing.NORTH?-l+1: side==EnumFacing.SOUTH?l-1: side==EnumFacing.EAST?ww: -ww)};
						tile.mirrored = false;
						tile.markDirty();
						world.addBlockEvent(pos2, IEnContent.blockMetalMultiblocks, 255, 0);
					}
				}
		return b;
	}

	boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
	{
		for(int l = -2; l < 2; l++)
			for(int w = -2; w <= 2; w++)
				for(int h = -1; h <= 1; h++)
				{
					if((h==-1&&w==0&&l==-2)||(h>-1&&w==0&&l<0))
						continue;
					int ww = mirror?-w: w;
					BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);

					switch(h)
					{
						case -1:
						{
							if(w==-2||w==2)
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
									return false;
							}
							else if (w==-1||w==1)
							{
								if(l==-2||l==1)
								{
									if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDevice1, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta()))
										return false;
								}
								else
								{
									if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta()))
										return false;
								}
							}
							if(w==0)
							{

								if(l==1)
								{
									if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.GENERATOR.getMeta()))
										return false;
								}
								else
								{
									if(!Utils.isOreBlockAt(world, pos, "blockSteel"))
										return false;
								}
							}
						}
						break;
						case 0:
						{
							if(w==0)
							{
								if(l>-1)
								{
									if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.GENERATOR.getMeta()))
										return false;
								}
							}
							else
							{
								if(!Utils.isOreBlockAt(world, pos, "blockSheetmetalSteel"))
									return false;
							}
						}
						break;
						case 1:
						{
							if((w==-2&&l==-1)||(w==2&&l==-1)||(w==-1&&l==1)||(w==1&&l==1))
							{
								if(!Utils.isBlockAt(world, pos, IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.GENERATOR.getMeta()))
									return false;
							}
							else if(w==0&&l>-1)
							{
								if(!Utils.isOreBlockAt(world, pos, "slabSteel"))
									return false;
							}
							else if(w!=0)
							{
								if(!Utils.isOreBlockAt(world, pos, "slabSheetmetalSteel"))
									return false;
							}
						}
						break;
					}
				}
		return true;
	}

	@Override
	public IngredientStack[] getTotalMaterials()
	{
		return materials;
	}
}