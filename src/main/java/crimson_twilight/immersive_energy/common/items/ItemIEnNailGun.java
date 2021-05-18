package crimson_twilight.immersive_energy.common.items;

import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.ListUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import crimson_twilight.immersive_energy.INail;
import crimson_twilight.immersive_energy.common.entities.EntityIEnNail;
import crimson_twilight.immersive_energy.common.helper.IEnSlot;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemIEnNailGun extends ItemUpgradeableTool implements ITool
{
    public ItemIEnNailGun() {
        super("nail_gun", 1, "NAILGUN");
        this.hasSubtypes = false;
    }

    @Override
    public boolean isTool(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canModify(ItemStack itemStack) {
        return true;
    }



    @Override
    public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
    {
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        return new Slot[]
                {
                        new IEnSlot.Nail(inv, 0, 100, 24, 64)
                };
    }

    @Override
    public int getSlotCount(ItemStack itemStack) {
        return 1;
    }

    @Override
    public void removeFromWorkbench(EntityPlayer player, ItemStack stack) {
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
        NBTTagCompound ret = super.getNBTShareTag(stack);
        if(ret==null)
            ret = new NBTTagCompound();
        else
            ret = ret.copy();
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(handler!=null)
        {
            NonNullList<ItemStack> nails = NonNullList.withSize(1, ItemStack.EMPTY);
            nails.set(0, handler.getStackInSlot(0));
            ret.setTag("nails", Utils.writeInventory(nails));
        }
        return ret;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.BOW;
    }

    public boolean isEmpty(ItemStack stack, boolean allowCasing)
    {
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(inv!=null)
            if(!inv.getStackInSlot(0).isEmpty()&&inv.getStackInSlot(0).getItem() instanceof INail&&ItemNBTHelper.hasKey(inv.getStackInSlot(0), "nails"))
                return false;
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if(performInWorldRecipe(player.getHeldItem(hand), world.getBlockState(pos).getBlock(), player.isSneaking())) return EnumActionResult.SUCCESS;
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack nail_gun = player.getHeldItem(hand);
        if (!world.isRemote)
        {
            IItemHandlerModifiable inv = (IItemHandlerModifiable)nail_gun.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            assert inv!=null;
            NonNullList<ItemStack> nails = ListUtils.fromItems(this.getContainedItems(nail_gun));
            if(!nails.get(0).isEmpty()&&nails.get(0).getItem() instanceof INail)
            {
                Vec3d vec = player.getLookVec();
                Entity entNail = getNail(world, player, vec, nails.get(0));
                player.world.spawnEntity(entNail);
                nails.get(0).shrink(1);
            }
            return new ActionResult(EnumActionResult.SUCCESS, nail_gun);
        }
        return new ActionResult(EnumActionResult.PASS, nail_gun);
    }

    EntityIEnNail getNail(World world, EntityPlayer player, Vec3d vecDir, ItemStack stack)
    {
        INail nail = (INail)stack.getItem();
        EntityIEnNail entNail = new EntityIEnNail(world, player, vecDir.x, vecDir.y, vecDir.z, stack, nail.getDamage());
        entNail.motionX = vecDir.x*2;
        entNail.motionY = vecDir.y*2;
        entNail.motionZ = vecDir.z*2;
        return entNail;
    }

    private boolean performInWorldRecipe(ItemStack stack, Block block, boolean isSneaking)
    {
        if(!isSneaking) return false;

        return false;
    }


}
