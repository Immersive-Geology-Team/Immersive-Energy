package crimson_twilight.immersive_energy.common.blocks;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum BlockTypes_MetalsIEn implements IStringSerializable, BlockIEnBase.IBlockEnum
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
