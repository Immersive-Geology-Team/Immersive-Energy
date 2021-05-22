package crimson_twilight.immersive_energy.common.helper;

import blusunrize.immersiveengineering.common.gui.IESlot;
import crimson_twilight.immersive_energy.api.tool.INail;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public abstract class IEnSlot extends IESlot
{
    public IEnSlot(Container container, IInventory inv, int id, int x, int y) {
        super(container, inv, id, x, y);
    }

    public static class Nail extends SlotItemHandler {
        int limit;

        public Nail(IItemHandler inv, int id, int x, int y, int limit) {
            super(inv, id, x, y);
            this.limit = limit;
        }

        public boolean isItemValid(ItemStack itemStack) {
            return !itemStack.isEmpty() && itemStack.getItem() instanceof INail;
        }

        public int getSlotStackLimit() {
            return this.limit;
        }

        public int getItemStackLimit(@Nonnull ItemStack stack) {
            return this.limit;
        }
    }
}
