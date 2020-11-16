package crimson_twilight.immersive_energy.client.shader;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import crimson_twilight.immersive_energy.common.EventHandler;
import crimson_twilight.immersive_energy.common.IEnContent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ShaderHandler 
{
    public static final int SHADER_NIGHT_VISION = 0;
	public static ResourceLocation[] shader_resources = new ResourceLocation[]{new ResourceLocation("shaders/night_vision.json")};
	
	public ShaderHandler() {}
	
	protected void checkShaders(PlayerTickEvent event, Minecraft mc) 
	{
		if (event.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(IEnContent.itemPowerArmorHelmet) && mc.gameSettings.thirdPersonView == 0) {
            if (!EventHandler.shaderGroups.containsKey(0) && ItemNBTHelper.getBoolean(event.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), "night_vision_active")) 
            {
                try 
                {
                    this.setShader(new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader_resources[3]), 3);
                } catch (JsonSyntaxException var4) 
                {
                } catch (IOException var5) 
                {
                }
            }
        } else if (EventHandler.shaderGroups.containsKey(0)) 
        {
            this.deactivateShader(0);
        }
	}
	
	void setShader(ShaderGroup target, int shaderId) 
	{
        if (OpenGlHelper.shadersSupported) 
        {
            if (EventHandler.shaderGroups.containsKey(shaderId)) 
            {
                ((ShaderGroup)EventHandler.shaderGroups.get(shaderId)).deleteShaderGroup();
                EventHandler.shaderGroups.remove(shaderId);
            }
            try 
            {
                if (target == null) 
                {
                    this.deactivateShader(shaderId);
                } else 
                {
                    EventHandler.resetShaders = true;
                    EventHandler.shaderGroups.put(shaderId, target);
                }
            } catch (Exception var4) 
            {
                EventHandler.shaderGroups.remove(shaderId);
            }
        }

    }

    public void deactivateShader(int shaderId) 
    {
        if (EventHandler.shaderGroups.containsKey(shaderId)) 
        {
            ((ShaderGroup)EventHandler.shaderGroups.get(shaderId)).deleteShaderGroup();
        }
        EventHandler.shaderGroups.remove(shaderId);
    }
}
