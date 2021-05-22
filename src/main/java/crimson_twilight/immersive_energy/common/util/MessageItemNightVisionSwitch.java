package crimson_twilight.immersive_energy.common.util;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import crimson_twilight.immersive_energy.common.IEnContent;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageItemNightVisionSwitch implements IMessage
{
    boolean active;

    public MessageItemNightVisionSwitch(boolean active)
    {
        this.active = active;
    }

    public MessageItemNightVisionSwitch()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.active = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.active);
    }

    public static class Handler implements IMessageHandler<MessageItemNightVisionSwitch, IMessage>
    {
        @Override
        public IMessage onMessage(MessageItemNightVisionSwitch message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() ->
            {
                ItemStack equipped = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if(equipped.getItem().equals(IEnContent.itemPowerArmorHelmet))
                {
                    ItemNBTHelper.setBoolean(equipped, "night_vision_active", message.active);
                    System.out.println("Sent message: " + message.active);
                }
            });
            return null;
        }
    }
}