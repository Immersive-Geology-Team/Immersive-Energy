package crimson_twilight.immersive_energy.common.util;

import java.util.List;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

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

	//Proudly stolen from Pabilo8 by Pabilo8
	public static float[] rgbIntToRGB(int rgb)
	{
		float r = (rgb/256/256%256)/255f;
		float g = (rgb/256%256)/255f;
		float b = (rgb%256)/255f;
		return new float[]{r, g, b};
	}

	public static float[] medColour(float[] colour1, float[] colour2, float progress) {
		float rev=1f-progress;
		return new float[]{
				(colour1[0]*rev+colour2[0]*progress),
				(colour1[1]*rev+colour2[1]*progress),
				(colour1[2]*rev+colour2[2]*progress)
		};
	}

	public static void drawGradientRectHorizontal(int x0, int y0, int x1, int y1, int colour0, int colour1)
	{
		float[] f0 = rgbIntToRGB(colour0);
		float[] f1 = rgbIntToRGB(colour1);

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		worldrenderer.pos(x1, y0, 0).color(f1[0], f1[1], f1[2], 1f).endVertex();
		worldrenderer.pos(x0, y0, 0).color(f0[0], f0[1], f0[2], 1f).endVertex();
		worldrenderer.pos(x0, y1, 0).color(f0[0], f0[1], f0[2], 1f).endVertex();
		worldrenderer.pos(x1, y1, 0).color(f1[0], f1[1], f1[2], 1f).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
}
