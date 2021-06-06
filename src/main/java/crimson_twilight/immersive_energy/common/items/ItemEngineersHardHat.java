package crimson_twilight.immersive_energy.common.items;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.util.IEnKeybinds;
import crimson_twilight.immersive_energy.common.util.MessageItemNightVisionSwitch;
import crimson_twilight.immersive_energy.common.util.network.IEnPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemEngineersHardHat extends ItemUpgradeableArmor
{
    private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-498F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E86-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C188-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-68FD380BB150")};
    public ItemEngineersHardHat() {  super(IEnContent.engineerArmor, EntityEquipmentSlot.HEAD, "ENGINEER_HAT"); }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
    {
        float integrity = 100-(float)getDurabilityForDisplay(stack)*100f;
        list.add(String.format("%s %.2f %%", I18n.format(Lib.DESC_INFO+"integrity"), integrity));
        super.addInformation(stack,world,list,flag);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if(IEnKeybinds.helmet_night_vision.isPressed() && getUpgrades(stack).getBoolean("nightvision") && stack.getItem().equals(IEnContent.itemEngineersHardHat))
            IEnPacketHandler.INSTANCE.sendToServer(new MessageItemNightVisionSwitch(!(ItemNBTHelper.getBoolean(stack, "night_vision_active"))));
    }

    @Override
    public int getSlotCount() {
        return 3;
    }

    @Override
    public float getMaxHeat(ItemStack stack) {
        return 0;
    }

    @Override
    public float getHeatCap(ItemStack stack) {
        return 300;
    }

    @Override
    public void modifyHeat(ItemStack stack, float amount) { return; }
}
