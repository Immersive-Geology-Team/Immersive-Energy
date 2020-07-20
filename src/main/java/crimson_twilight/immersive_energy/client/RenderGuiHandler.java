package crimson_twilight.immersive_energy.client;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageRequestBlockUpdate;
import crimson_twilight.immersive_energy.client.render.GuiOverlay;
import crimson_twilight.immersive_energy.common.items.ItemPowerArmorHelmet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderGuiHandler 
{

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		GuiOverlay guiOverlay = new GuiOverlay(mc);
		if(event.getType() == ElementType.CROSSHAIRS)
		{
			guiOverlay.renderGameOverlay(event.getPartialTicks());
		}
		else if(event.getType() == ElementType.TEXT && ClientUtils.mc().gameSettings.thirdPersonView==0)
		{
			EntityPlayer player = ClientUtils.mc().player;

			//Held items
			/*
			for(EnumHand hand : EnumHand.values())

				if(!player.getHeldItem(hand).isEmpty())
				{

				}
			 */

			//Armor (or -our, depends on how much United(tm) is your Kingdom)
			if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem()instanceof ItemPowerArmorHelmet)
			{
				RayTraceResult mop = ClientUtils.mc().objectMouseOver;
				if(mop!=null&&mop.getBlockPos()!=null)
				{
					TileEntity tileEntity = player.world.getTileEntity(mop.getBlockPos());

					int col = IEConfig.nixietubeFont?Lib.colour_nixieTubeText: 0xffffff;
					String[] text = null;
					if(tileEntity instanceof IFluxReceiver)
					{
						int maxStorage = ((IFluxReceiver)tileEntity).getMaxEnergyStored(mop.sideHit);
						int storage = ((IFluxReceiver)tileEntity).getEnergyStored(mop.sideHit);
						if(maxStorage > 0)
							text = I18n.format(Lib.DESC_INFO+"energyStored", "<br>"+Utils.toScientificNotation(storage, "0##", 100000)+" / "+Utils.toScientificNotation(maxStorage, "0##", 100000)).split("<br>");
					}
					else if(mop.entityHit instanceof IFluxReceiver)
					{
						int maxStorage = ((IFluxReceiver)mop.entityHit).getMaxEnergyStored(null);
						int storage = ((IFluxReceiver)mop.entityHit).getEnergyStored(null);
						if(maxStorage > 0)
							text = I18n.format(Lib.DESC_INFO+"energyStored", "<br>"+Utils.toScientificNotation(storage, "0##", 100000)+" / "+Utils.toScientificNotation(maxStorage, "0##", 100000)).split("<br>");
					}
					if(text!=null)
					{
						if(player.world.isRemote && player.world.getTotalWorldTime()%20==0)
						{
							//must use this, the packet belongs and can be received only by IE's handler
							ImmersiveEngineering.packetHandler.sendToServer(new MessageRequestBlockUpdate(mop.getBlockPos()));
						}
						int i = 0;
						for(String s : text)
							if(s!=null)
							{
								int w = blusunrize.immersiveengineering.client.ClientProxy.nixieFontOptional.getStringWidth(s);
								blusunrize.immersiveengineering.client.ClientProxy.nixieFontOptional.drawString(s, event.getResolution().getScaledWidth()/2-w/2, event.getResolution().getScaledHeight()/2-4-text.length*(blusunrize.immersiveengineering.client.ClientProxy.nixieFontOptional.FONT_HEIGHT+2)+(i++)*(blusunrize.immersiveengineering.client.ClientProxy.nixieFontOptional.FONT_HEIGHT+2), col, true);
							}
					}
				}
			}
		}
	}

}
