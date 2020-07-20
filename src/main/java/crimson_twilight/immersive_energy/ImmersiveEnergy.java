package crimson_twilight.immersive_energy;

import org.apache.logging.log4j.Logger;

import blusunrize.immersiveengineering.common.IEContent;
import crimson_twilight.immersive_energy.client.RenderGuiHandler;
import crimson_twilight.immersive_energy.common.CommonProxy;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.entities.EntityIEnArrow;
import crimson_twilight.immersive_energy.common.items.IEnArrowBase;
import crimson_twilight.immersive_energy.common.world.IEnWorldGen;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ImmersiveEnergy.MODID, name = ImmersiveEnergy.MODNAME, version = ImmersiveEnergy.MODVERSION, dependencies = "required-after:immersiveengineering;after:sereneseasons", useMetadata = true)
public class ImmersiveEnergy 
{

    public static final String MODID = "immersive_energy";
    public static final String MODNAME = "Immersive Energy";
    public static final String MODVERSION= "0.5.3";

	@Mod.Instance(MODID)
	public static ImmersiveEnergy instance = new ImmersiveEnergy();

    @SidedProxy(clientSide = "crimson_twilight.immersive_energy.client.ClientProxy", serverSide = "crimson_twilight.immersive_energy.common.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;

	public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) 
    {
        logger = e.getModLog();
        IEnContent.preInit();
        proxy.preInit();
        
        
        
        IEnContent.preInitEnd();
        proxy.preInitEnd();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) 
    {
    	IEnContent.init();
    	proxy.init();
		IEnWorldGen ienWorldGen = new IEnWorldGen();
		GameRegistry.registerWorldGenerator(ienWorldGen, 0);
		MinecraftForge.EVENT_BUS.register(ienWorldGen);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		



		IEnContent.initEnd();
    	proxy.initEnd();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) 
    {
    	MinecraftForge.EVENT_BUS.register(new RenderGuiHandler());
        IEnContent.postInit();
        proxy.postInit();
        
        
        
        
        for(Item item : IEnContent.registeredIEnItems)
		{
			if(item instanceof IEnArrowBase)
			{
				BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, new BehaviorProjectileDispense() 
				{
					@Override
					protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) 
					{
						IEnArrowBase arrowItem = ((IEnArrowBase) item);
						final EntityIEnArrow arrow = new EntityIEnArrow(worldIn, arrowItem.getDropItem().copy(), position.getX(), position.getY(), position.getZ()).setIgnoreInvulnerability(arrowItem.getIgnoreInvulnerability());
				        arrow.setLogic(arrowItem.getLogic());
				        arrow.setDamage(arrowItem.getArrowDamage());
				        arrow.setKnockbackStrength(arrowItem.getKnockback());
				        arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
				        if(arrowItem.isFlaming()) 
				        {
				            arrow.setFire(100);
				        }
				        return arrow;
					}
				});
			}
		}
        IEnContent.postInitEnd();
        proxy.postInitEnd();
    }


	public static CreativeTabs creativeTab = new CreativeTabs(MODID)
	{

		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(IEContent.blockMetalDecoration0, 1, 1);
		}
	};

}
