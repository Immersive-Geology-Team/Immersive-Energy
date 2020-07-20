package crimson_twilight.immersive_energy.client.model;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry.ItemModelReplacement;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.client.models.ModelCoresample;
import blusunrize.immersiveengineering.client.models.smart.FeedthroughModel;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Pabilo8 on 14-09-2019.
 * The bestest of best!
 * Blu, please make variables public (and available to addon makers)
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class EvenMoreImmersiveModelRegistry
{
	public static EvenMoreImmersiveModelRegistry instance = new EvenMoreImmersiveModelRegistry();
	private static final ImmutableMap<String, String> flipData = ImmutableMap.of("flip-v", String.valueOf(true));
	private HashMap<ModelResourceLocation, ItemModelReplacement> itemModelReplacements = new HashMap<ModelResourceLocation, ItemModelReplacement>();

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event)
	{
		for(Map.Entry<ModelResourceLocation, ItemModelReplacement> entry : itemModelReplacements.entrySet())
		{
			IBakedModel object = event.getModelRegistry().getObject(entry.getKey());
			if(object!=null)
			{
				try
				{
					event.getModelRegistry().putObject(entry.getKey(), entry.getValue().createBakedModel(object));
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		ModelResourceLocation mLoc = new ModelResourceLocation(new ResourceLocation(ImmersiveEnergy.MODID, IEContent.itemCoresample.itemName), "inventory");
		event.getModelRegistry().putObject(mLoc, new ModelCoresample());
		IConveyorBelt belt = ConveyorHandler.getConveyor(new ResourceLocation(ImmersiveEnergy.MODID, "conveyor"), null);
		ModelConveyor modelConveyor = new ModelConveyor(belt);
		mLoc = new ModelResourceLocation(new ResourceLocation(ImmersiveEnergy.MODID, "conveyor"), "normal");
		event.getModelRegistry().putObject(mLoc, modelConveyor);
		mLoc = new ModelResourceLocation(new ResourceLocation(ImmersiveEnergy.MODID, "conveyor"), "inventory");
		event.getModelRegistry().putObject(mLoc, modelConveyor);
		mLoc = new ModelResourceLocation(new ResourceLocation(ImmersiveEnergy.MODID, "connector"), "inventory,type=feedthrough");
		event.getModelRegistry().putObject(mLoc, new FeedthroughModel());
	}

	public void registerCustomItemModel(ItemStack stack, ItemModelReplacement replacement)
	{
		if(stack.getItem() instanceof ItemIEBase)
		{
			ResourceLocation loc;
			if(((ItemIEBase)stack.getItem()).getSubNames()!=null&&((ItemIEBase)stack.getItem()).getSubNames().length > 0)
				loc = new ResourceLocation(ImmersiveEnergy.MODID, ((ItemIEBase)stack.getItem()).itemName+"/"+((ItemIEBase)stack.getItem()).getSubNames()[stack.getMetadata()]);
			else
				loc = new ResourceLocation(ImmersiveEnergy.MODID, ((ItemIEBase)stack.getItem()).itemName);
			itemModelReplacements.put(new ModelResourceLocation(loc, "inventory"), replacement);
		}
	}

}
