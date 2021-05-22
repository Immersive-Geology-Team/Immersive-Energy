package crimson_twilight.immersive_energy.common.gui;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class ContainerItem extends Container {
    protected final InventoryPlayer inventoryPlayer;
    protected final World world;
    protected int blockedSlot;
    protected final EntityEquipmentSlot equipmentSlot;
    protected final ItemStack heldItem;
    protected final EntityPlayer player;
    public int internalSlots;

    public ContainerItem(InventoryPlayer inventoryPlayer, World world, EntityEquipmentSlot entityEquipmentSlot, ItemStack heldItem) {
        this.inventoryPlayer = inventoryPlayer;
        this.world = world;
        this.player = inventoryPlayer.player;
        this.equipmentSlot = entityEquipmentSlot;
        this.heldItem = heldItem.copy();
        this.updateSlots();
    }

    protected void updateSlots() {
        this.internalSlots = this.addSlots();
        this.blockedSlot = this.inventoryPlayer.currentItem + 27 + this.internalSlots;
    }

    abstract int addSlots();

    public EntityEquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
        ItemStack oldStackInSlot = ItemStack.EMPTY;
        Slot slotObject = this.inventorySlots.get(slot);
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            oldStackInSlot = stackInSlot.copy();
            if (slot < this.internalSlots) {
                if (!this.mergeItemStack(stackInSlot, this.internalSlots, this.internalSlots + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.allowShiftclicking() && !stackInSlot.isEmpty()) {
                boolean b = true;
                int i = 0;

                while(true) {
                    if (i >= this.internalSlots) {
                        if (b) {
                            return ItemStack.EMPTY;
                        }
                        break;
                    }

                    Slot s = (Slot)this.inventorySlots.get(i);
                    if (s != null && s.isItemValid(stackInSlot) && (s.getStack().isEmpty() || ItemStack.areItemsEqual(stackInSlot, s.getStack()) && Utils.compareItemNBT(stackInSlot, s.getStack()))) {
                        int space = Math.min(s.getItemStackLimit(stackInSlot), stackInSlot.getMaxStackSize());
                        if (!s.getStack().isEmpty()) {
                            space -= s.getStack().getCount();
                        }

                        if (space > 0) {
                            ItemStack insert = stackInSlot;
                            if (space < stackInSlot.getCount()) {
                                insert = stackInSlot.splitStack(space);
                            }

                            if (this.mergeItemStack(insert, i, i + 1, true)) {
                                b = false;
                            }
                        }
                    }

                    ++i;
                }
            }

            if (stackInSlot.getCount() == 0) {
                slotObject.putStack(ItemStack.EMPTY);
            } else {
                slotObject.onSlotChanged();
            }

            slotObject.inventory.markDirty();
            if (stackInSlot.getCount() == oldStackInSlot.getCount()) {
                return ItemStack.EMPTY;
            }

            slotObject.onTake(this.player, oldStackInSlot);
            this.updatePlayerItem();
            this.detectAndSendChanges();
        }

        return oldStackInSlot;
    }

    protected boolean allowShiftclicking() {
        return true;
    }

    public boolean canInteractWith(@Nonnull EntityPlayer entityplayer) {
        return ItemStack.areItemsEqual(this.player.getItemStackFromSlot(this.equipmentSlot), this.heldItem);
    }

    @Nonnull
    public ItemStack slotClick(int par1, int par2, ClickType par3, EntityPlayer par4EntityPlayer) {
        if (par1 != this.blockedSlot && (par3 != ClickType.SWAP || par2 != par4EntityPlayer.inventory.currentItem)) {
            ItemStack ret = super.slotClick(par1, par2, par3, par4EntityPlayer);
            this.updatePlayerItem();
            return ret;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        if (!this.world.isRemote) {
            this.updatePlayerItem();
        }

    }

    protected void updatePlayerItem() {
    }
}
