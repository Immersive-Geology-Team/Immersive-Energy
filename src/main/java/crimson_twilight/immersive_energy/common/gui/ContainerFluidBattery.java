package crimson_twilight.immersive_energy.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityGasBurner;
import crimson_twilight.immersive_energy.common.blocks.multiblock.TileEntityFluidBattery;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFluidBattery extends ContainerIEBase
{

	public ContainerFluidBattery(InventoryPlayer inventoryPlayer, TileEntityFluidBattery tile)
	{
		super(inventoryPlayer, tile);
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 38, 26,2));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 1, 38, 57,1));

		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 122, 26,2));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 122, 57,1));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;
		
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 87 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, k, 8 + k * 18, 145));
        }
	}

}
