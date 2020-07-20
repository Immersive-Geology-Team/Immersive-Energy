package crimson_twilight.immersive_energy.api.energy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FuelHandler 
{
	static final HashMap<String, Integer> gasBurnerAmountTick = new HashMap<String, Integer>();
	
	public static void registerGasBurnerFuel(Fluid fuel, int tickPermb)
	{
		if (fuel != null) 
		{
			gasBurnerAmountTick.put(fuel.getName(), tickPermb);
		}
	}
	
	public static boolean isValidFuel(Fluid fuel)
	{
		if (fuel != null) 
		{
			return gasBurnerAmountTick.containsKey(fuel.getName());
		}
		return false;
	}
	
	public static HashMap<String, Integer> getFuelPerTick()
	{
		return gasBurnerAmountTick;
	}
	
	public static int getTickPermb(Fluid fuel)
	{
		if (!isValidFuel(fuel)) return 0;
		return gasBurnerAmountTick.get(fuel.getName());
	}
	
	public static List<FluidStack> getBurnerFuels()
	{
		List<FluidStack> fuels = new ArrayList<>();
		for(String fuel : gasBurnerAmountTick.keySet())
		{
			if (fuel != null) 
			{
				fuels.add(FluidRegistry.getFluidStack(fuel, 1));
			}
		}
		return fuels;
	}
}
