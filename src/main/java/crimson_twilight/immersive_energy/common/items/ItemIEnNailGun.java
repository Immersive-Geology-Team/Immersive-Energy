package crimson_twilight.immersive_energy.common.items;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ITool;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.ListUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import crimson_twilight.immersive_energy.api.tool.IMicroRocket;
import crimson_twilight.immersive_energy.api.tool.INail;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.CommonProxy;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.IEnGUIList;
import crimson_twilight.immersive_energy.common.entities.EntityIEnNail;
import crimson_twilight.immersive_energy.common.helper.IEnSlot;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemIEnNailGun extends ItemUpgradeableTool implements ITool, EnergyHelper.IIEEnergyItem, IEItemInterfaces.IGuiItem
{
    public ItemIEnNailGun() {
        super("nail_gun", 1, "NAILGUN");
        this.hasSubtypes = false;
        fixupItem();
    }

    @Override
    public boolean isTool(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canModify(ItemStack itemStack) {
        return true;
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

    @Override
    public int getGuiID(ItemStack itemStack) {
        return IEnGUIList.GUI_NAILGUN;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
    {
        String stored = this.getEnergyStored(stack)+"/"+this.getMaxEnergyStored(stack);
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        list.add(I18n.format(Lib.DESC+"info.energyStored", stored));
        int ammo = 0;
        for(int i = 0; i < getAmmoSlots(stack); i++) { ammo += inv.getStackInSlot(i).getCount(); }
        list.add(String.format("%s %s", I18n.format(Lib.DESC+"info.nailsStored"), ammo));
        if(this.getUpgrades(stack).hasKey("NAILGUN_MICRO_ROCKETS"))
        {
            int rockets = 0;
            for(int i = 0; i < this.getUpgrades(stack).getInteger("NAILGUN_MICRO_ROCKETS"); i++) { rockets += inv.getStackInSlot(3+i).getCount(); }
            list.add(String.format("%s %s", I18n.format(Lib.DESC+"info.rocketsStored"), rockets));
        }
    }

    @Override
    public Slot[] getWorkbenchSlots(Container container, ItemStack stack)
    {
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        return new Slot[]
                {
                        new IEnSlot.Upgrades(container, inv, 5, 100, 24, "NAILGUN_SIDES", stack, true),
                        new IEnSlot.Upgrades(container, inv, 6, 100, 46, "NAILGUN_AMMO", stack, true)
                };
    }

    @Override
    public int getSlotCount(ItemStack itemStack) {
        return 3+2+2;
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
        if(ret==null) {
            ret = new NBTTagCompound();
        }
        else
        {
            ret = ret.copy();
        }
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(handler!=null)
        {
            NonNullList<ItemStack> nails = NonNullList.withSize(getAmmoSlots(stack), ItemStack.EMPTY);
            for(int i = 0; i < getAmmoSlots(stack); i++)
                nails.set(i, handler.getStackInSlot(i));
            ret.setTag("nails", Utils.writeInventory(nails));
            NonNullList<ItemStack> rockets  = NonNullList.withSize(this.getUpgrades(stack).getInteger("NAILGUN_MICRO_ROCKETS"), ItemStack.EMPTY);
            for(int i = 0; i < this.getUpgrades(stack).getInteger("NAILGUN_MICRO_ROCKETS"); i++)
                rockets.set(i, handler.getStackInSlot(3+i));
            ret.setTag("rockets", Utils.writeInventory(rockets));
        }
        return ret;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        if (!stack.isEmpty())
            return new IEItemStackHandler(stack)
            {
                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
                {
                    return super.hasCapability(capability, facing);
                }

                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
                {
                    return super.getCapability(capability, facing);
                }
            };
        return null;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(ItemNBTHelper.hasKey(stack, "cooldown"))
        {
            int cooldown = ItemNBTHelper.getInt(stack, "cooldown")-1;
            if(cooldown <= 0)
                ItemNBTHelper.remove(stack, "cooldown");
            else
                ItemNBTHelper.setInt(stack, "cooldown", cooldown);
        }
    }

    public int getShootCooldown(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, "cooldown");
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) { return EnumAction.BOW; }

    public boolean isEmpty(ItemStack stack)
    {
        IItemHandler inv = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(inv!=null)
            for(int i = 0; i < inv.getSlots(); i++)
            {
                ItemStack b = inv.getStackInSlot(i);
                if((!b.isEmpty()&&b.getItem() instanceof INail) || (!b.isEmpty()&&b.getItem() instanceof IMicroRocket))
                    return false;
            }
        return true;
    }

    private int getAmmoSlots(ItemStack stack) { return 2+this.getUpgrades(stack).getInteger("NAILGUN_AMMO"); }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if(!player.isSneaking()&&player.getCooledAttackStrength(1)>=1&&performInWorldRecipe(player.getHeldItem(hand), world.getBlockState(pos).getBlock())) return EnumActionResult.SUCCESS;
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack nail_gun = player.getHeldItem(hand);
        if (!world.isRemote)
        {
            if(player.isSneaking())
            {
                CommonProxy.openGuiForItem(player, hand==EnumHand.MAIN_HAND? EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND);
                return new ActionResult(EnumActionResult.SUCCESS, nail_gun);
            }
            if(player.getCooledAttackStrength(1) < 1 || getShootCooldown(nail_gun) > 0)
                return new ActionResult(EnumActionResult.PASS, nail_gun);
            IItemHandlerModifiable inv = (IItemHandlerModifiable)nail_gun.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            assert inv!=null;
            NonNullList<ItemStack> ammo = ListUtils.fromItems(this.getContainedItems(nail_gun));
            boolean fired = false;
            if(!this.isEmpty(nail_gun))
            {
                for (int i = 0; i < inv.getSlots(); i++) {
                    if(ammo.get(i).getItem() instanceof INail&&fired==false&&this.getEnergyStored(nail_gun) >= 50) {
                        this.extractEnergy(nail_gun, 50, false);
                        Vec3d vec = player.getLookVec();
                        Entity entNail = getNail(world, player, vec, ammo.get(i));
                        player.world.spawnEntity(entNail);
                        ammo.get(i).shrink(1);
                        fired=true;
                    }
                    if(ammo.get(i).getItem() instanceof IMicroRocket) {
                        Vec3d vec = player.getLookVec();
                        //Entity entRocket = getRocket(world, player, vec, ammo.get(i));
                        //player.world.spawnEntity(entRocket);
                        ammo.get(i).shrink(1);
                        fired=true;
                    }
                }
            }
            if(fired){
                ItemNBTHelper.setInt(nail_gun, "cooldown", 10);
                return new ActionResult(EnumActionResult.SUCCESS, nail_gun);
            }
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

    private boolean performInWorldRecipe(ItemStack stack, Block block) { return false; }


    @Override
    public int getMaxEnergyStored(ItemStack itemStack) { return 6400; }
}
