package crimson_twilight.immersive_energy.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityGasBurner;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerGasBurner extends ContainerIEBase
{

	public ContainerGasBurner(InventoryPlayer inventoryPlayer, TileEntityGasBurner tile) 
	{
		super(inventoryPlayer, tile);
		this.addSlotToContainer(new IESlot(this, this.inv, 0, 69, 24)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return TileEntityGasBurner.isCookableFood(stack);
			}
		});
		this.addSlotToContainer(new IESlot.Output(this, this.inv, 1, 91, 24));
        
		this.slotCount = tile.getInventory().size();
		this.tile = tile;
		
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, k, 8 + k * 18, 142));
        }
	}

}
