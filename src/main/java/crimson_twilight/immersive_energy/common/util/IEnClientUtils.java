package crimson_twilight.immersive_energy.common.util;

import java.util.List;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;

public class IEnClientUtils 
{
	
	public static void drawTooltip(List<String> tooltip, int posX, int posY, int width, int height, int mouseX, int mouseY, int sizeX, int sizeY)
	{
		if(mouseX >= posX&&mouseX < posX+width&&mouseY >= posY&&mouseY < posY+height)
			ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, ClientUtils.font(), sizeX, sizeY);
	}
	
	public static void drawTooltip(List<String> tooltip, int posX, int posY, int width, int height, int mouseX, int mouseY, int sizeX, int sizeY, FontRenderer fontrenderer)
	{
		if(mouseX >= posX&&mouseX < posX+width&&mouseY >= posY&&mouseY < posY+height)
			ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontrenderer, sizeX, sizeY);
	}

}
