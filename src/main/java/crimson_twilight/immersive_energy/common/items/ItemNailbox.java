package crimson_twilight.immersive_energy.common.items;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.items.ItemInternalStorage;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.Sets;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.api.tool.NailboxHandler;
import crimson_twilight.immersive_energy.common.CommonProxy;
import crimson_twilight.immersive_energy.common.Config;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.IEnGUIList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ItemNailbox extends ItemInternalStorage implements ITool, IGuiItem
{
    public ItemNailbox() {
        super("nailbox", 1);
        this.hasSubtypes = false;
        NailboxHandler.addNailType(new Predicate<ItemStack>() {
            final Set<String> set = Sets.newHashSet(Config.IEnConfig.Tools.nailbox_nails);

            @Override
            public boolean test(ItemStack stack) {
                return set.contains(stack.getItem().getRegistryName().toString());
            }
        });
        fixupItem();
    }

    public void fixupItem()
    {
        //First, get the item out of IE's registries.
        Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
        if(rItem!=this) throw new IllegalStateException("fixupItem was not called at the appropriate time");

        //Now, reconfigure the block to match our mod.
        this.setUnlocalizedName(ImmersiveEnergy.MODID+"."+this.itemName);
        this.setCreativeTab(ImmersiveEnergy.creativeTab);

        //And add it to our registries.
        IEnContent.registeredIEnItems.add(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
    {
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int nails = inv.getStackInSlot(0).getCount() + inv.getStackInSlot(1).getCount();
        list.add(String.format("%s %s", I18n.format(Lib.DESC+"info.nailsStored"), nails));
    }

    @Override
    public boolean isTool(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getSlotCount(ItemStack itemStack) {
        return 6;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote)
            CommonProxy.openGuiForItem(player, hand==EnumHand.MAIN_HAND? EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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
            NonNullList<ItemStack> nails = NonNullList.withSize(getSlotCount(stack), ItemStack.EMPTY);
            for(int i = 0; i < getSlotCount(stack); i++)
                nails.set(i, handler.getStackInSlot(i));
            ret.setTag("nails", Utils.writeInventory(nails));
        }
        return ret;
    }

    @Override
    public int getGuiID(ItemStack itemStack)
    {
        return IEnGUIList.GUI_NAILBOX;
    }
}
