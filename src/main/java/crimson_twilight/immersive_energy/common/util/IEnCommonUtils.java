package crimson_twilight.immersive_energy.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author Pabilo8
 * @since 03.12.2020
 */
public class IEnCommonUtils {
    //Rightfully Stolen from Pabilo8 by Pabilo8
    public static <T extends IFluidTank & IFluidHandler> boolean handleBucketTankInteraction(T[] tanks, NonNullList<ItemStack> inventory, int bucketInputSlot, int bucketOutputSlot, int tank, String filter) {
        if (inventory.get(bucketInputSlot).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            IFluidHandlerItem cap = inventory.get(bucketInputSlot).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            assert cap != null;
            if (cap.getTankProperties()[0].getContents() != null && cap.getTankProperties()[0].getContents().getFluid().getName().equals(filter)) {
                int amount_prev = tanks[tank].getFluidAmount();

                ItemStack emptyContainer = blusunrize.immersiveengineering.common.util.Utils.drainFluidContainer(tanks[tank], inventory.get(bucketInputSlot), inventory.get(bucketOutputSlot), null);
                if (amount_prev != tanks[tank].getFluidAmount()) {
                    if (!inventory.get(bucketOutputSlot).isEmpty() && OreDictionary.itemMatches(inventory.get(bucketOutputSlot), emptyContainer, true))
                        inventory.get(bucketOutputSlot).grow(emptyContainer.getCount());
                    else if (inventory.get(bucketOutputSlot).isEmpty())
                        inventory.set(bucketOutputSlot, emptyContainer.copy());
                    inventory.get(bucketInputSlot).shrink(1);
                    if (inventory.get(bucketInputSlot).getCount() <= 0)
                        inventory.set(bucketInputSlot, ItemStack.EMPTY);

                    return true;
                }
            }
        }
        return false;
    }

    //Possibly Stolen from Pabilo8 by Pabilo8
    public static boolean isPointInRectangle(double x, double y, double xx, double yy, double px, double py) {
        return px >= x && px < xx && py >= y && py < yy;
    }

    //Absolutely Stolen from Pabilo8 by Pabilo8
    public static NetworkRegistry.TargetPoint targetPointFromPos(BlockPos pos, World world, int range) {
        return new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
    }

    //Unspeakably Stolen from Pabilo8 by Pabilo8
    public static NetworkRegistry.TargetPoint targetPointFromEntity(Entity entity, int range) {
        return new NetworkRegistry.TargetPoint(entity.getEntityWorld().provider.getDimension(), entity.getPositionVector().x, entity.getPositionVector().y, entity.getPositionVector().z, range);
    }

    //Silently Stolen from Pabilo8 by Pabilo8
    public static NetworkRegistry.TargetPoint targetPointFromTile(TileEntity tile, int range) {
        return targetPointFromPos(tile.getPos(), tile.getWorld(), range);
    }
}
