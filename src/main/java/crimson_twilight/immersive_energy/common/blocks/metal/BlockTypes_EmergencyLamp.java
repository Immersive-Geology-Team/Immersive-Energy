package crimson_twilight.immersive_energy.common.blocks.metal;

import crimson_twilight.immersive_energy.common.blocks.BlockIEnBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum BlockTypes_EmergencyLamp implements IStringSerializable, BlockIEnBase.IBlockEnum
{
	EMERGENCY_LAMP;

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
