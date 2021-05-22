package crimson_twilight.immersive_energy.client.gui;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import crimson_twilight.immersive_energy.common.gui.ContainerNailbox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;

public class GUINailbox extends GuiIEContainerBase
{
    public GUINailbox(InventoryPlayer inventoryPlayer, World world, EntityEquipmentSlot slot, ItemStack nailbox)
    {
        super(new ContainerNailbox(inventoryPlayer, world, slot, nailbox));
        this.ySize = 238;
    }

    @Override
    public void drawScreen(int mx, int my, float partial) {
        super.drawScreen(mx, my, partial);
        ArrayList<String> tooltip = new ArrayList<String>();
        int slot = -1;
        for (int i = 0; i < ((ContainerNailbox) this.inventorySlots).internalSlots; i++) {
            Slot s = this.inventorySlots.inventorySlots.get(i);
            if (!s.getHasStack() && mx > guiLeft + s.xPos && mx < guiLeft + s.xPos + 16 && my > guiTop + s.yPos && my < guiTop + s.yPos + 16)
                slot = i;
        }
        String ss = null;
        if(slot < 6) ss = "nail";
        if (ss != null)
            tooltip.add(TextFormatting.GRAY + I18n.format(Lib.DESC_INFO + "nailbox." + ss));
        if (!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft + xSize, -1);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("immersive_energy:textures/gui/nailbox.png");
        this.drawTexturedModalRect(guiLeft, guiTop-17, 0, 0, 176, ySize+17);
    }
}
