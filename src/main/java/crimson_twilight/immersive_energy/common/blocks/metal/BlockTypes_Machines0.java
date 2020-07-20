package crimson_twilight.immersive_energy.common.blocks.metal;

import java.util.Locale;

import crimson_twilight.immersive_energy.common.blocks.BlockIEnBase;
import net.minecraft.util.IStringSerializable;

public enum BlockTypes_Machines0 implements IStringSerializable, BlockIEnBase.IBlockEnum
{
	GAS_BURNER;

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
		return true;
	}
	
	
}
