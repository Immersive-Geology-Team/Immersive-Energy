package crimson_twilight.immersive_energy.common.blocks;

import java.util.Locale;

import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.IStringSerializable;

public enum BlockTypes_OresIEn implements IStringSerializable, BlockIEnBase.IBlockEnum
{
	THORIUM,
	TUNGSTEN;

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