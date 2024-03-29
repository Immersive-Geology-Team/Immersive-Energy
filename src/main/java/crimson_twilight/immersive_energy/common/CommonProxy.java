package crimson_twilight.immersive_energy.common;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.mojang.authlib.GameProfile;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.blocks.metal.TileEntityGasBurner;
import crimson_twilight.immersive_energy.common.blocks.multiblock.TileEntityFluidBattery;
import crimson_twilight.immersive_energy.common.compat.IEnCompatModule;
import crimson_twilight.immersive_energy.common.gui.ContainerFluidBattery;
import crimson_twilight.immersive_energy.common.gui.ContainerGasBurner;
import crimson_twilight.immersive_energy.common.gui.ContainerNailbox;
import crimson_twilight.immersive_energy.common.items.ItemNailbox;
import crimson_twilight.immersive_energy.common.util.IEnKeybinds;
import crimson_twilight.immersive_energy.common.util.network.IEnPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public void preInit()
	{
		IEnCompatModule.doModulesPreInit();
		IEnPacketHandler.preInit();
	}

	public void preInitEnd()
	{
		
	}

	public void init()
	{
		IEnCompatModule.doModulesInit();
	}

	public void initEnd()
	{

	}

	public void postInit()
	{
		IEnCompatModule.doModulesPostInit();
	}

	public void postInitEnd()
	{
		
	}

	public void serverStarting()
	{
		
	}

	public void onWorldLoad()
	{
		
	}

	public static <T extends TileEntity & IGuiTile> void openGuiForTile(@Nonnull EntityPlayer player, @Nonnull T tile)
	{
		player.openGui(ImmersiveEnergy.instance, tile.getGuiID(), tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
	}

	public static void openGuiForItem(@Nonnull EntityPlayer player, @Nonnull EntityEquipmentSlot slot)
	{
		ItemStack stack = player.getItemStackFromSlot(slot);
		if(stack.isEmpty()||!(stack.getItem() instanceof IGuiItem))
			return;
		IGuiItem gui = (IGuiItem)stack.getItem();
		player.openGui(ImmersiveEnergy.instance, 100*slot.ordinal()+gui.getGuiID(stack), player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		EntityEquipmentSlot slot = EntityEquipmentSlot.values()[ID/100];
		ID %= 100;//Slot determined, get actual ID
		ItemStack item = player.getItemStackFromSlot(slot);
		if(!item.isEmpty()&&item.getItem() instanceof IGuiItem&&((IGuiItem)item.getItem()).getGuiID(item)==ID) {
			if (ID == IEnGUIList.GUI_NAILBOX && item.getItem() instanceof ItemNailbox)
				return new ContainerNailbox(player.inventory, world, slot, item);
		}
		if(tile instanceof IGuiTile)
		{
			Object gui = null;
			if (ID==IEnGUIList.GUI_GAS_BURNER && tile instanceof TileEntityGasBurner)
				gui = new ContainerGasBurner(player.inventory, (TileEntityGasBurner)tile);
			else if (ID==IEnGUIList.GUI_FLUID_BATTERY && tile instanceof TileEntityFluidBattery)
				gui = new ContainerFluidBattery(player.inventory, (TileEntityFluidBattery)tile);

			((IGuiTile)tile).onGuiOpened(player, false);
			return gui;
		}
		return null;
	}

	public void handleTileSound(SoundEvent soundEvent, TileEntity tile, boolean tileActive, float volume, float pitch)
	{
	}

	public void stopTileSound(String soundName, TileEntity tile)
	{
	}

	public void spawnSparkFX(World world, double x, double y, double z, double mx, double my, double mz)
	{
	}

	public void spawnRedstoneFX(World world, double x, double y, double z, double mx, double my, double mz, float size, float r, float g, float b)
	{
	}

	public void spawnFluidSplashFX(World world, FluidStack fs, double x, double y, double z, double mx, double my, double mz)
	{
	}

	public void spawnBubbleFX(World world, FluidStack fs, double x, double y, double z, double mx, double my, double mz)
	{
	}

	public void spawnFractalFX(World world, double x, double y, double z, Vec3d direction, double scale, int prefixColour, float[][] colour)
	{
	}

	public void draw3DBlockCauldron()
	{
	}

	public void drawSpecificFluidPipe(String configuration)
	{
	}

	public boolean armorHasCustomModel(ItemStack stack)
	{
		return false;
	}

	public boolean drawConveyorInGui(String conveyor, EnumFacing facing)
	{
		return false;
	}

	public void drawFluidPumpTop()
	{
	}

	public String[] splitStringOnWidth(String s, int w)
	{
		return new String[]{s};
	}

	public World getClientWorld()
	{
		return null;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}

	public String getNameFromUUID(String uuid)
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance().getMinecraftSessionService().fillProfileProperties(new GameProfile(UUID.fromString(uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5")), null), false).getName();
	}

	public void reInitGui()
	{
	}

	public void removeStateFromSmartModelCache(IExtendedBlockState state)
	{
	}

	public void removeStateFromConnectionModelCache(IExtendedBlockState state)
	{
	}

	public void clearConnectionModelCache()
	{
	}

	public void clearRenderCaches()
	{
	}
}
