package crimson_twilight.immersive_energy.api;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class IEnMaths extends MathHelper
{
	
	
	public static float modyfyTempBasedOnTime(float tempIn, World worldIn)
	{
		return tempIn + 4 * worldIn.getCelestialAngleRadians(worldIn.getWorldTime() - 4);
	}
	
	
	//Pure Math
	
	public static int min(int var1, int var2)
	{
		return var1 >= var2 ? var2 : var1;
	}
	
	public static float min(float var1, float var2)
	{
		return var1 >= var2 ? var2 : var1;
	}
	
	public static double min(double var1, double var2)
	{
		return var1 >= var2 ? var2 : var1;
	}
	
	public static long min(long var1, long var2)
	{
		return var1 >= var2 ? var2 : var1;
	}
	
	public static short min(short var1, short var2)
	{
		return var1 >= var2 ? var2 : var1;
	}
	
	public static byte min(byte var1, byte var2)
	{
		return var1 >= var2 ? var2 : var1;
	}
	
	
	
	public static int max(int var1, int var2)
	{
		return var1 >= var2 ? var1 : var2;
	}
	
	public static float max(float var1, float var2)
	{
		return var1 >= var2 ? var1 : var2;
	}
	
	public static double max(double var1, double var2)
	{
		return var1 >= var2 ? var1 : var2;
	}
	
	public static long max(long var1, long var2)
	{
		return var1 >= var2 ? var1 : var2;
	}
	
	public static short max(short var1, short var2)
	{
		return var1 >= var2 ? var1 : var2;
	}
	
	public static byte max(byte var1, byte var2)
	{
		return var1 >= var2 ? var1 : var2;
	}
	
	
	
	public static int round(float var)
	{
		return var < 0 ? ( ( var != (int)var ) ? (var - 0.5) <= (int)var - 1 ? (int)var - 1 : (int)var : (int)var ) : (var != (int)var) ? ((int)(var + 0.5) == (int)var + 1 ? (int)var + 1 : (int)var) : (int)var;
	}
	
	public static int round(double var)
	{
		return var < 0 ? ( ( var != (int)var ) ? (var - 0.5) <= (int)var - 1 ? (int)var - 1 : (int)var : (int)var ) : (var != (int)var) ? ((int)(var + 0.5) == (int)var + 1 ? (int)var + 1 : (int)var) : (int)var;
	}
}
