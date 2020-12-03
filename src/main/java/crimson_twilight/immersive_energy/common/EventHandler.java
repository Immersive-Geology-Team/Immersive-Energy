package crimson_twilight.immersive_energy.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;

import com.mojang.realmsclient.util.Pair;

import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.util.IEnDamageSources;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = ImmersiveEnergy.MODID)
public class EventHandler
{
	public static final Queue<Pair<Integer, BlockPos>> requestedBlockUpdates = new LinkedList<>();
	private static final UUID POWER_ARMOR_SPEED_BOOST_ID = UUID.fromString("eba4c34c-c7d9-11e9-a32f-2a2ae2dbcce4");
	private static final AttributeModifier POWER_ARMOR_SPEED_BOOST = new AttributeModifier(POWER_ARMOR_SPEED_BOOST_ID, "Power Armor Speed Boost", 0.4D, 2);
	public static final Map<EntityPlayer, Pair<Entity, RayTraceResult>> hitList = new WeakHashMap<>();
    private static int oldDisplayWidth = 0;
    private static int oldDisplayHeight = 0;
    public static boolean resetShaders = false;
    public static HashMap<Integer, ShaderGroup> shaderGroups;

	@SubscribeEvent
	public static void onLoad(WorldEvent.Load event)
	{

	}

	//transferPerTick
	@SubscribeEvent
	public static void onSave(WorldEvent.Save event)
	{
		IEnSaveData.setDirty(0);
	}

	@SubscribeEvent
	public static void onUnload(WorldEvent.Unload event)
	{
		IEnSaveData.setDirty(0);
	}

	//
	@SubscribeEvent
	public static void onCapabilitiesAttachEntity(AttachCapabilitiesEvent<Entity> event)
	{

	}

	@SubscribeEvent
	public static void onCapabilitiesAttachItem(AttachCapabilitiesEvent<ItemStack> event)
	{

	}
	
	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		ItemStack head, body, legs, boots;
		boots = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		body = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		int suitHeatDamage = 0;
		if(boots.getItem().equals(IEnContent.itemPowerArmorBoots) && IEnContent.itemPowerArmorBoots.getHeat(boots) > IEnContent.itemPowerArmorBoots.getMaxHeat(boots))
		{
			suitHeatDamage += MathHelper.ceil((double)((IEnContent.itemPowerArmorBoots.getHeat(boots) - IEnContent.itemPowerArmorBoots.getMaxHeat(boots))/100d));
		}
		if(legs.getItem().equals(IEnContent.itemPowerArmorLegs) && IEnContent.itemPowerArmorLegs.getHeat(legs) > IEnContent.itemPowerArmorLegs.getMaxHeat(legs))
		{
			suitHeatDamage += MathHelper.ceil(((double)(IEnContent.itemPowerArmorLegs.getHeat(legs) - IEnContent.itemPowerArmorLegs.getMaxHeat(legs))/100d));
		}
		if(body.getItem().equals(IEnContent.itemPowerArmorChestplate) && IEnContent.itemPowerArmorChestplate.getHeat(body) > IEnContent.itemPowerArmorChestplate.getMaxHeat(body))
		{
			suitHeatDamage += MathHelper.ceil(((double)(IEnContent.itemPowerArmorChestplate.getHeat(body) - IEnContent.itemPowerArmorChestplate.getMaxHeat(body))/100d));
		}
		if(head.getItem().equals(IEnContent.itemPowerArmorHelmet) && IEnContent.itemPowerArmorHelmet.getHeat(head) > IEnContent.itemPowerArmorHelmet.getMaxHeat(head))
		{
			suitHeatDamage += MathHelper.ceil(((double)(IEnContent.itemPowerArmorHelmet.getHeat(head) - IEnContent.itemPowerArmorHelmet.getMaxHeat(head))/100d));
		}
		if(suitHeatDamage > 0 && entity.getEntityWorld().getTotalWorldTime()%20==0)
		{
			entity.attackEntityFrom(IEnDamageSources.causeBurningSuitDamage(), suitHeatDamage);
		}
	}

	@SubscribeEvent
	public static void onLivingAttacked(LivingAttackEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		ItemStack head, body, legs, boots;
		boots = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		body = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

		if(boots.getItem().equals(IEnContent.itemPowerArmorBoots)
				&&legs.getItem().equals(IEnContent.itemPowerArmorLegs)
				&&body.getItem().equals(IEnContent.itemPowerArmorChestplate)
				&&head.getItem().equals(IEnContent.itemPowerArmorHelmet))
		{
			if((event.getSource()==DamageSource.IN_FIRE||event.getSource()==DamageSource.ON_FIRE))
			{
				event.setCanceled(true);
				IEnContent.itemPowerArmorBoots.modifyHeat(boots, IEnContent.itemPowerArmorBoots.getHeat(boots) == 1100 ? 0 : 0.1f * event.getAmount() * (1100 - IEnContent.itemPowerArmorBoots.getHeat(boots)));
				IEnContent.itemPowerArmorLegs.modifyHeat(legs, IEnContent.itemPowerArmorBoots.getHeat(legs) == 1100 ? 0 : 0.1f * event.getAmount() * (1100 - IEnContent.itemPowerArmorLegs.getHeat(legs)));
				IEnContent.itemPowerArmorChestplate.modifyHeat(body, IEnContent.itemPowerArmorBoots.getHeat(body) == 1100 ? 0 : 0.1f * event.getAmount() * (1100 - IEnContent.itemPowerArmorChestplate.getHeat(body)));
				IEnContent.itemPowerArmorHelmet.modifyHeat(head, IEnContent.itemPowerArmorBoots.getHeat(head) == 1100 ? 0 : 0.1f * event.getAmount() * (1100 - IEnContent.itemPowerArmorHelmet.getHeat(head)));
			}
			else if(event.getSource()==DamageSource.LAVA)
			{
				event.setCanceled(true);
				IEnContent.itemPowerArmorBoots.modifyHeat(boots, IEnContent.itemPowerArmorBoots.getHeat(boots) == 1200 ? 0 : 0.2f * event.getAmount() * (1200 - IEnContent.itemPowerArmorBoots.getHeat(boots)));
				IEnContent.itemPowerArmorLegs.modifyHeat(legs, IEnContent.itemPowerArmorBoots.getHeat(legs) == 1200 ? 0 : 0.2f * event.getAmount() * (1200 - IEnContent.itemPowerArmorLegs.getHeat(legs)));
				IEnContent.itemPowerArmorChestplate.modifyHeat(body, IEnContent.itemPowerArmorBoots.getHeat(body) == 1200 ? 0 : 0.2f * event.getAmount() * (1200 - IEnContent.itemPowerArmorChestplate.getHeat(body)));
				IEnContent.itemPowerArmorHelmet.modifyHeat(head, IEnContent.itemPowerArmorBoots.getHeat(head) == 1200 ? 0 : 0.2f * event.getAmount() * (1200 - IEnContent.itemPowerArmorHelmet.getHeat(head)));
			}
			if(event.getSource()==DamageSource.CACTUS)
			{
				event.setCanceled(true);
			}

		}

		if(event.getSource()==DamageSource.HOT_FLOOR && boots.getItem().equals(IEnContent.itemPowerArmorBoots))
		{
			IEnContent.itemPowerArmorBoots.modifyHeat(boots, IEnContent.itemPowerArmorBoots.getHeat(boots) == 100 ? 0 : 0.1f * event.getAmount() * (100 - IEnContent.itemPowerArmorBoots.getHeat(boots)));
			event.setCanceled(true);
		}

		if(event.getSource()==DamageSource.FALL&&boots.getItem().equals(IEnContent.itemPowerArmorBoots))
		{
			int fallreduction = 2;

			if(legs.getItem().equals(IEnContent.itemPowerArmorLegs))
			{
				if(body.getItem().equals(IEnContent.itemPowerArmorChestplate))
				{
					if(head.getItem().equals(IEnContent.itemPowerArmorHelmet))
					{
						fallreduction = 9;
					}
					else
						fallreduction = 8;
				}
				else
					fallreduction = 6;
			}
			else
				fallreduction = 2;
			float damage = event.getAmount();
			if(damage-fallreduction <= 0)
			{
				playBlockedSound(entity);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void hurtEvent(LivingHurtEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		if(event.getSource()==DamageSource.FALL&&entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().equals(IEnContent.itemPowerArmorBoots))
		{
			int fallreduction = 2;
			if(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem().equals(IEnContent.itemPowerArmorLegs))
			{
				if(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(IEnContent.itemPowerArmorChestplate))
				{
					if(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(IEnContent.itemPowerArmorHelmet))
					{
						fallreduction = 9;
					}
					else
						fallreduction = 8;
				}
				else
					fallreduction = 6;
			}
			else
				fallreduction = 2;
			float damage = event.getAmount();
			if(damage-fallreduction <= 0)
			{
				playBlockedSound(entity);
				event.setCanceled(true);
			}
			else
			{
				playReducedSound(entity);
				event.setAmount((damage-fallreduction)*.8f);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		if(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().equals(IEnContent.itemPowerArmorBoots))
		{
			
		}
		if(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().equals(IEnContent.itemPowerArmorBoots)
				&&player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem().equals(IEnContent.itemPowerArmorLegs)
				&&player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(IEnContent.itemPowerArmorChestplate)
				&&player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(IEnContent.itemPowerArmorHelmet))
		{
			player.extinguish();
			int energy = EnergyHelper.getEnergyStored(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
			IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			if(iattributeinstance.getModifier(POWER_ARMOR_SPEED_BOOST_ID)!=null)
			{
				iattributeinstance.removeModifier(POWER_ARMOR_SPEED_BOOST_ID);
			}
			if(!player.isRiding()&&(player.moveStrafing!=0||player.moveForward!=0)&&energy > 5)
			{
				iattributeinstance.applyModifier(POWER_ARMOR_SPEED_BOOST);
				EnergyHelper.extractFlux(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), 5, false);
			}
		}
	}

	public static void playBlockedSound(EntityLivingBase entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isRemote)
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_CLOTH_STEP, SoundCategory.PLAYERS, 1, 1);
	}

	public static void playReducedSound(EntityLivingBase entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isRemote)
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.8f, 0.4f);
	}

	@SubscribeEvent
	public static void blastProtection(LivingHurtEvent event)
	{
		if(event.getSource().isExplosion())
		{
			float damage = event.getAmount();
			float mult = 1;
			EntityLivingBase entity = event.getEntityLiving();
			for(int i = 0; i < 4; i++)
			{
				switch(i)
				{
					case 0:
						if(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().equals(IEnContent.itemPowerArmorBoots))
							mult -= 0.05;
						break;
					case 1:
						if(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem().equals(IEnContent.itemPowerArmorLegs))
							mult -= 0.2;
						break;
					case 2:
						if(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(IEnContent.itemPowerArmorChestplate))
							mult -= 0.3;
						break;
					case 3:
						if(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(IEnContent.itemPowerArmorHelmet))
							mult -= 0.15;
						break;
				}

				event.setAmount(damage*mult);
			}
		}
	}

	@SubscribeEvent //So reflection always takes priority
	public static void reflectArrows(ProjectileImpactEvent.Arrow event)
	{
		final EntityArrow projectile = event.getArrow();

		if(!projectile.getEntityWorld().isRemote)
		{
			if(event.getEntity()!=null&&event.getEntity() instanceof EntityLivingBase)
			{
				EntityLivingBase entity = (EntityLivingBase)event.getRayTraceResult().entityHit;
				if(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().equals(IEnContent.itemPowerArmorBoots)
						&&entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem().equals(IEnContent.itemPowerArmorLegs)
						&&entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(IEnContent.itemPowerArmorChestplate)
						&&entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals(IEnContent.itemPowerArmorHelmet))
				{
					Random rand = new Random();
					int item = rand.nextInt(4);
					int damageItem = 100+rand.nextInt(25)-rand.nextInt(25);
					ItemStack armor = null;
					switch(item)
					{
						case 0:
							armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);

							break;
						case 1:
							armor = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
							break;
						case 2:
							armor = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
							break;
						case 3:
							armor = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
							break;
						default:
							armor = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
							break;
					}
					if(projectile.getDamage() <= 15)
					{
						armor.damageItem(damageItem, entity);
						playReflectSound(entity);
						projectile.shoot(projectile.motionX*-1, projectile.motionY, projectile.motionZ*-1, 0.3f, 0.2f);
						projectile.shootingEntity = entity;
						event.setCanceled(true);
					}
					else
					{
						projectile.setDamage(projectile.getDamage()-15);
						armor.damageItem(250, entity);
						playPirceSound(entity);
					}
				}
			}
		}
	}

	public static void playReflectSound(EntityLivingBase entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isRemote)
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.8f, 0.4f);
	}

	public static void playPirceSound(EntityLivingBase entity)
	{
		World world = entity.getEntityWorld();
		if(!world.isRemote)
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.PLAYERS, 0.8f, 0.4f);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGameOverlayRender(RenderGameOverlayEvent.Post e)
	{
//		EntityPlayer player = Minecraft.getMinecraft().player;
//		Minecraft mc = Minecraft.getMinecraft();
//
//		int w = e.getResolution().getScaledWidth();
//		int h = e.getResolution().getScaledHeight();
//		
//		int x = w / 2;
//		int y = h / 2;
//
//		
//		if (e.getType() == ElementType.ALL) {
//
//			double checkSpeed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
//			
//			mc.fontRenderer.drawStringWithShadow("Player Speed: " + String.valueOf(checkSpeed), w / 2 + 30, h / 2 + 10, 0xFFAA00);
//		
//		}
	}

	@SubscribeEvent
	public static void onFurnaceBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		if(Utils.isFluidRelatedItemStack(event.getItemStack()))
		{
			FluidStack fs = FluidUtil.getFluidContained(event.getItemStack());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onLogin(PlayerLoggedInEvent event)
	{

	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onLogout(PlayerLoggedOutEvent event)
	{

	}
	
//	@SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public static void renderShaders(Pre event) 
//	{
//        if(event.getType() == ElementType.ALL) 
//        {
//            Minecraft mc = Minecraft.getMinecraft();
//            if(OpenGlHelper.shadersSupported && shaderGroups.size() > 0) 
//            {
//                updateShaderFrameBuffers(mc);
//                GL11.glMatrixMode(5890);
//                GL11.glLoadIdentity();
//
//                for(Iterator<ShaderGroup> var2 = shaderGroups.values().iterator(); var2.hasNext(); GL11.glPopMatrix()) 
//                {
//                    ShaderGroup sg = (ShaderGroup)var2.next();
//                    GL11.glPushMatrix();
//
//                    try 
//                    {
//                        sg.render(event.getPartialTicks());
//                    } catch (Exception var5) {
//                    }
//                }
//
//                mc.getFramebuffer().bindFramebuffer(true);
//            }
//        }
//
//    }
//
//	@SideOnly(Side.CLIENT)
//    private static void updateShaderFrameBuffers(Minecraft mc) {
//        if(resetShaders || mc.displayWidth != oldDisplayWidth || oldDisplayHeight != mc.displayHeight) {
//            Iterator<ShaderGroup> var1 = shaderGroups.values().iterator();
//
//            while(var1.hasNext()) 
//            {
//                ShaderGroup sg = (ShaderGroup)var1.next();
//                sg.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
//            }
//
//            oldDisplayWidth = mc.displayWidth;
//            oldDisplayHeight = mc.displayHeight;
//            resetShaders = false;
//        }
//
//    }
}
