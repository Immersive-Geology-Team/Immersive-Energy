package crimson_twilight.immersive_energy.client.render;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityEmergencyLight;
import crimson_twilight.immersive_energy.common.compat.IEnCompatModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 21.09.2020
 */
public class EmergencyLightRenderer extends TileEntitySpecialRenderer<TileEntityEmergencyLight>
{
	@Override
	public void render(TileEntityEmergencyLight te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		//ClientUtils.bindTexture(texture);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();

		BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		BlockPos blockPos = te.getPos();
		IBlockState state = getWorld().getBlockState(blockPos).withProperty(IEProperties.DYNAMICRENDER,true);
		IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(state);
		float f = ((getWorld().getTotalWorldTime()+partialTicks)%120)/120f;
		float f1 = 0.5F;

		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.translate(0.5,0.25,0.5);

			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.enableBlend();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.depthMask(false);
			GlStateManager.pushMatrix();

			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.rotate(360f*f, 0.0F, 0, 1.0F);
			for(int i = 0; (float)i < 2; ++i)
			{
				GlStateManager.rotate(180f, 0.0F, 0, 1.0F);
				float f2 = 4.0F+f1;
				float f3 = 1.0F+f1*0.25F;
				bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
				bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(255, 0, 0, (int)(255.0F*(1.0F-f1))).endVertex();
				bufferbuilder.pos(-0.866D*(double)f3, f2, -0.5F*f3).color(255, 100, 100, 0).endVertex();
				bufferbuilder.pos(0.866D*(double)f3, f2, -0.5F*f3).color(255, 100, 100, 0).endVertex();
				bufferbuilder.pos(0.0D, f2, 1.0F*f3).color(255, 100, 100, 0).endVertex();
				bufferbuilder.pos(-0.866D*(double)f3, f2, -0.5F*f3).color(255, 100, 100, 0).endVertex();
				tessellator.draw();
			}

			GlStateManager.popMatrix();
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
			GlStateManager.shadeModel(7424);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			RenderHelper.enableStandardItemLighting();
		}

		ClientUtils.bindAtlas();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(.5, -1.25, .5);
		GlStateManager.rotate(-360f*f, 0.0F, 1.0F, 0);


		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		if(Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(7425);
		else
			GlStateManager.shadeModel(7424);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		buffer.setTranslation(-.5-blockPos.getX(), -.5-blockPos.getY(), -.5-blockPos.getZ());
		buffer.color(255, 255, 255, 255);
		blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, buffer, true);
		buffer.setTranslation(0.0D, 0.0D, 0.0D);
		Tessellator.getInstance().draw();

		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();


		GlStateManager.popMatrix();
	}
}
