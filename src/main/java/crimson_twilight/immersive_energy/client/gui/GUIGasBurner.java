package crimson_twilight.immersive_energy.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.api.energy.FuelHandler;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityGasBurner;
import crimson_twilight.immersive_energy.common.gui.ContainerGasBurner;
import crimson_twilight.immersive_energy.common.util.IEnClientUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class GUIGasBurner extends GuiIEContainerBase
{
	TileEntityGasBurner tile;
	public GUIGasBurner(InventoryPlayer inventoryPlayer, TileEntityGasBurner tile) 
	{
		super(new ContainerGasBurner(inventoryPlayer, tile));
		this.tile = tile;
		this.xSize = 214;
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
		ClientUtils.handleGuiTank(tile.tank, guiLeft+184, guiTop+90, 16, 47, 214, 0, 20, 51, mouseX, mouseY, ImmersiveEnergy.MODID+":textures/gui/gas_burner.png", tooltip);
		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
		List<String> heatHover = new ArrayList<String>();
		heatHover.add(0, "Heat: " + Double.toString(tile.heat) + "/" + Double.toString(tile.heatMax));
		heatHover.add(1, "Minimum Heat: " + Double.toString(tile.heatMin));
		IEnClientUtils.drawTooltip(heatHover, guiLeft+86, guiTop+23, 4, 18, mouseX, mouseY, guiLeft+xSize, -1);
		List<String> burnHover = new ArrayList<String>();
		if(tile.tank.getFluidAmount() > 0)
			burnHover.add(0, "Flame: " + Integer.toString(tile.burnTime) + "/" + Integer.toString(FuelHandler.getTickPermb(tile.tank.getFluid().getFluid())));
		else
			burnHover.add(0, "Flame: " + Integer.toString(tile.burnTime));
		IEnClientUtils.drawTooltip(burnHover, guiLeft+83, guiTop+45, 11, 14, mouseX, mouseY, guiLeft+xSize, -1);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(ImmersiveEnergy.MODID+":textures/gui/gas_burner.png");
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		int l = (int)(15*(tile.heat/tile.heatMax));
		this.drawTexturedModalRect(guiLeft+87, guiTop+24 +15-l, 250, 31-l, 2, 1+l);
		if(tile.burnTime > 0)
		{
			this.drawTexturedModalRect(guiLeft+83, guiTop+45, 234, 0, 12, 15);
		}
		if(tile.cookTime > 0)
		{
			int h = (int)(16*((double)tile.cookTime/(double)tile.cookNeeded));
			this.drawTexturedModalRect(guiLeft+69, guiTop+24 +16-h, 234, 32-h, 16, h);
		}
		
		ClientUtils.handleGuiTank(tile.tank, guiLeft+184, guiTop+90, 16, 47, 214, 0, 20, 51, mouseX, mouseY, ImmersiveEnergy.MODID+":textures/gui/gas_burner.png", null);
	}

}
