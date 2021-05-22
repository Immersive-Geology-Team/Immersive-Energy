package crimson_twilight.immersive_energy.common.gui;

import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerInternalStorageItem extends ContainerItem {
    public IItemHandler inv;

    public ContainerInternalStorageItem(InventoryPlayer iinventory, World world, EntityEquipmentSlot entityEquipmentSlot, ItemStack heldItem) {
        super(iinventory, world, entityEquipmentSlot, heldItem);
        this.inv = heldItem.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (this.inv instanceof IEItemStackHandler) {
            ((IEItemStackHandler)this.inv).setInventoryForUpdate(iinventory);
        }

        this.updateSlots();
    }

    protected void updateSlots() {
        if (this.inv != null) {
            super.updateSlots();
        }
    }

    @Override
    int addSlots() {return 0;}

    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        if (this.inv instanceof IEItemStackHandler) {
            ((IEItemStackHandler)this.inv).setInventoryForUpdate((IInventory)null);
        }

    }

    protected void updatePlayerItem() {
    }
}
