package crimson_twilight.immersive_energy.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.ICallbackContainer;
import crimson_twilight.immersive_energy.api.tool.NailboxHandler;
import crimson_twilight.immersive_energy.common.helper.IEnSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerNailbox extends ContainerInternalStorageItem implements ICallbackContainer
{
    public ContainerNailbox(InventoryPlayer inventoryPlayer, World world, EntityEquipmentSlot slot, ItemStack nailbox)
    {
        super(inventoryPlayer, world, slot, nailbox);
    }

    @Override
    int addSlots()
    {
        int i = 0;
        this.addSlotToContainer(new IEnSlot.ContainerCallback(this, this.inv, i++, 62, 99));
        this.addSlotToContainer(new IEnSlot.ContainerCallback(this, this.inv, i++, 80, 99));
        this.addSlotToContainer(new IEnSlot.ContainerCallback(this, this.inv, i++, 98, 99));
        this.addSlotToContainer(new IEnSlot.ContainerCallback(this, this.inv, i++, 62, 117));
        this.addSlotToContainer(new IEnSlot.ContainerCallback(this, this.inv, i++, 80, 117));
        this.addSlotToContainer(new IEnSlot.ContainerCallback(this, this.inv, i++, 98, 117));

        bindPlayerInventory(inventoryPlayer);
        return i;
    }

    @Override
    public boolean canInsert(ItemStack stack, int i, Slot slot) {
        return NailboxHandler.isNail(stack);
    }

    @Override
    public boolean canTake(ItemStack stack, int slotNumber, Slot slotObject) {
        return true;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 157 + i * 18));

        for (int i = 0; i < 9; i++)
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 215));
    }
}
