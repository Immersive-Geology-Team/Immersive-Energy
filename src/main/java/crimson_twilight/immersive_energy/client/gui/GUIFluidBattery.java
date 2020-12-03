package crimson_twilight.immersive_energy.client.gui;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.api.energy.FuelHandler;
import crimson_twilight.immersive_energy.common.Config;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityGasBurner;
import crimson_twilight.immersive_energy.common.blocks.multiblock.TileEntityFluidBattery;
import crimson_twilight.immersive_energy.common.gui.ContainerFluidBattery;
import crimson_twilight.immersive_energy.common.gui.ContainerGasBurner;
import crimson_twilight.immersive_energy.common.util.IEnClientUtils;
import crimson_twilight.immersive_energy.common.util.IEnCommonUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static crimson_twilight.immersive_energy.common.Config.IEnConfig.Machines.*;

public class GUIFluidBattery extends GuiIEContainerBase
{
	private static final float MAX_SWITCH_TICKS = 20f;
	TileEntityFluidBattery tile;
	boolean switches[];
	float[] timers;
	boolean data;
	public GUIFluidBattery(InventoryPlayer inventoryPlayer, TileEntityFluidBattery tile)
	{
		super(new ContainerFluidBattery(inventoryPlayer, tile));
		this.tile = tile;
		this.xSize = 176;
		this.ySize = 168;
		switches=tile.controlModes;
		data=tile.dataControlMode;
		timers=new float[]{
				data?0:MAX_SWITCH_TICKS,
				switches[0]?0:MAX_SWITCH_TICKS,
				switches[1]?0:MAX_SWITCH_TICKS,
				switches[2]?0:MAX_SWITCH_TICKS,
				switches[3]?0:MAX_SWITCH_TICKS
		};
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		//this.fontRenderer.drawString(net.minecraft.client.resources.I18n.format("tile."+ImmersiveEnergy.MODID+".metal_device0.gas_burner.name"), 8, 6, 0x0a0a0a);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		ArrayList<String> tooltip = new ArrayList<String>();

		ClientUtils.bindTexture(ImmersiveEnergy.MODID+":textures/gui/fluid_battery.png");
		renderSwitch(guiLeft+96,guiTop+36,0,18,partialTicks, data,0xff0000,0xffffff);

		renderSwitch(guiLeft+67,guiTop+48,1,18,partialTicks, switches[0],0x4c7bb1,0xffb515);
		renderSwitch(guiLeft+96,guiTop+48,2,18,partialTicks, switches[1],0x4c7bb1,0xffb515);
		renderSwitch(guiLeft+67,guiTop+59,3,18,partialTicks, switches[2],0x4c7bb1,0xffb515);
		renderSwitch(guiLeft+96,guiTop+59,4,18,partialTicks, switches[3],0x4c7bb1,0xffb515);

		tickSwitch(0,data);
		for (int i = 0; i < switches.length; i++) {
			tickSwitch(i+1,switches[i]);
		}

		int energy=tile.tanks[1].getFluidAmount()*FluidBattery.IFAmount;
		int maxEnergy=tile.tanks[1].getCapacity()*FluidBattery.IFAmount;

		if(IEnCommonUtils.isPointInRectangle(guiLeft+65, guiTop+72, guiLeft+64+47, guiTop+79,mouseX,mouseY))
			tooltip.add(energy+"/"+maxEnergy+" IF");

		ClientUtils.handleGuiTank(tile.tanks[0], guiLeft+15, guiTop+23, 24, 38, 0, 0, 0, 0, mouseX, mouseY, ImmersiveEnergy.MODID+":textures/gui/fluid_battery.png", tooltip);
		ClientUtils.handleGuiTank(tile.tanks[1], guiLeft+137, guiTop+23, 24, 38, 0, 0, 0, 0, mouseX, mouseY, ImmersiveEnergy.MODID+":textures/gui/fluid_battery.png", tooltip);

		if(tooltip.size()>0)
		ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, ClientUtils.font());
	}

	private void tickSwitch(int id, boolean value) {
		timers[id]=MathHelper.clamp(timers[id]+(value?-1:1),0,MAX_SWITCH_TICKS);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(ImmersiveEnergy.MODID+":textures/gui/fluid_battery.png");
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		ClientUtils.handleGuiTank(tile.tanks[0], guiLeft+15, guiTop+23, 24, 38, 0, 0, 0, 0, mouseX, mouseY, ImmersiveEnergy.MODID+":textures/gui/fluid_battery.png", null);
		ClientUtils.handleGuiTank(tile.tanks[1], guiLeft+137, guiTop+23, 24, 38, 0, 0, 0, 0, mouseX, mouseY, ImmersiveEnergy.MODID+":textures/gui/fluid_battery.png", null);
		ClientUtils.bindTexture(ImmersiveEnergy.MODID+":textures/gui/fluid_battery.png");

		this.drawTexturedModalRect(guiLeft+37,guiTop+25,176,80,18,18);
		this.drawTexturedModalRect(guiLeft+37,guiTop+56,176,80,18,18);

		this.drawTexturedModalRect(guiLeft+121,guiTop+25,176,80,18,18);
		this.drawTexturedModalRect(guiLeft+121,guiTop+56,176,80,18,18);

		int energy=tile.tanks[1].getFluidAmount()*FluidBattery.IFAmount;
		int maxEnergy=tile.tanks[1].getCapacity()*FluidBattery.IFAmount;

		int stored = (int)(47*energy/(float)maxEnergy);
		IEnClientUtils.drawGradientRectHorizontal(guiLeft+65, guiTop+72, guiLeft+64+stored, guiTop+79,  0xff600b00,0xffb51500);
	}

	public void renderSwitch(int x, int y, int id, int width, float partialTicks, boolean mode, int color1, int color2)
	{
		//ImmersiveEnergy.logger.info(id +" / " +mode);
		float progress = 1f-(MathHelper.clamp((timers[id]+(mode?partialTicks:-partialTicks)),0,MAX_SWITCH_TICKS)/MAX_SWITCH_TICKS);
		float[] c = IEnClientUtils.medColour(IEnClientUtils.rgbIntToRGB(color1),IEnClientUtils.rgbIntToRGB(color2),progress);
		int offset = (int)(progress*(width-7));
		GlStateManager.color(c[0],c[1],c[2]);
		this.drawTexturedModalRect(x+offset,y,176,98,8,9);
		GlStateManager.color(1f,1f,1f);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseButton==0)
		{
			if(IEnCommonUtils.isPointInRectangle(guiLeft+96,guiTop+36,guiLeft+96+18,guiTop+36+9,mouseX,mouseY))
			{
				data=!data;
				sendUpdatePacket();
			}
			else if(IEnCommonUtils.isPointInRectangle(guiLeft+67,guiTop+48,guiLeft+67+18,guiTop+48+9,mouseX,mouseY))
			{
				switches[0]=!switches[0];
				sendUpdatePacket();
			}
			else if(IEnCommonUtils.isPointInRectangle(guiLeft+96,guiTop+48,guiLeft+96+18,guiTop+48+9,mouseX,mouseY))
			{
				switches[1]=!switches[1];
				sendUpdatePacket();
			}
			else if(IEnCommonUtils.isPointInRectangle(guiLeft+67,guiTop+59,guiLeft+67+18,guiTop+59+9,mouseX,mouseY))
			{
				switches[2]=!switches[2];
				sendUpdatePacket();
			}
			else if(IEnCommonUtils.isPointInRectangle(guiLeft+96,guiTop+59,guiLeft+96+18,guiTop+59+9,mouseX,mouseY))
			{
				switches[3]=!switches[3];
				sendUpdatePacket();
			}

		}
	}

	private void sendUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("dataControlMode",data);

        nbt.setBoolean("b1",switches[0]);
        nbt.setBoolean("b2",switches[1]);
        nbt.setBoolean("b3",switches[2]);
        nbt.setBoolean("b4",switches[3]);
		ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(this.tile, nbt));
	}
}
