package crimson_twilight.immersive_energy.client.render;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.IEnContent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOverlay extends Gui
{
	protected static final ResourceLocation ENVIRONMENTSL_HELMET_OVERLAY_TEX_PATH = new ResourceLocation(ImmersiveEnergy.MODID, "textures/misc/environmentalvisor.png");
    protected final Minecraft mc;
    
    public GuiOverlay(Minecraft mcIn) 
    {
    	this.mc = mcIn;
	}

	public void renderGameOverlay(float partialTicks) 
    {
    	ScaledResolution res = new ScaledResolution(mc);
    	if(mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(IEnContent.itemPowerArmorHelmet) && mc.gameSettings.thirdPersonView == 0)
    	{
    		if(ItemNBTHelper.getBoolean(mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), "night_vision_active")) 
    		{
    			renderEnvironmentalHelmetOverlay(res);
    		}
    	}
    }
    
	protected void renderEnvironmentalHelmetOverlay(ScaledResolution scaledRes)
    {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(ENVIRONMENTSL_HELMET_OVERLAY_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
