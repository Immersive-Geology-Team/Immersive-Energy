package crimson_twilight.immersive_energy.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class IEnSaveData extends WorldSavedData
{
	private static IEnSaveData INSTANCE;
	public static final String dataName = "ImmersiveEnergy-SaveData";

	public IEnSaveData(String s)
	{
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		// TODO Auto-generated method stub
		return null;
	}


	public static void setDirty(int dimension)
	{
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER&&INSTANCE!=null)
			INSTANCE.markDirty();
	}

	public static void setInstance(int dimension, IEnSaveData in)
	{
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			INSTANCE = in;
	}

}
