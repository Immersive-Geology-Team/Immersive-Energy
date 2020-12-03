package crimson_twilight.immersive_energy.common.blocks.multiblock;

import crimson_twilight.immersive_energy.common.blocks.BlockIEnBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum BlockTypes_MetalMultiblock0 implements IStringSerializable, BlockIEnBase.IBlockEnum
{
	FLUID_BATTERY(true);

	private boolean needsCustomState;
	BlockTypes_MetalMultiblock0(boolean needsCustomState)
	{
		this.needsCustomState = needsCustomState;
	}

	@Override
	public String getName()
	{
		return this.toString().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public int getMeta()
	{
		return ordinal();
	}

	@Override
	public boolean listForCreative() 
	{
		return false;
	}

	public boolean needsCustomState()
	{
		return this.needsCustomState;
	}

	public String getCustomState()
	{
		return getName().toLowerCase();
	}
	
}
