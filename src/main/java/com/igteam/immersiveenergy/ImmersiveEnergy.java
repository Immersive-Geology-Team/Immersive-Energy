package com.igteam.immersiveenergy;

import com.igteam.immersiveenergy.client.IENClientRenderHandler;
import com.igteam.immersiveenergy.core.lib.IENLib;
import com.igteam.immersiveenergy.core.registration.IENContent;
import com.igteam.immersiveenergy.core.registration.IENRecipeSerializers;
import com.igteam.immersiveenergy.core.registration.IENRegistrationHolder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IENLib.MODID)
public class ImmersiveEnergy
{
    public ImmersiveEnergy()
    {
        IEventBus modEventBus =  FMLJavaModLoadingContext.get().getModEventBus();
        IENLib.IEN_LOGGER.info("IEN Starting");
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        IENRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        IENRegistrationHolder.addRegistersToEventBus(modEventBus);
        IENContent.modContruction(modEventBus);
    }

    private void clientSetup(FMLClientSetupEvent event)
    {
        IENClientRenderHandler.register();
        IENClientRenderHandler.init(event);
        IENContent.initializeManualEntries();
    }

    public void setup(final FMLCommonSetupEvent event)
    {

    }
}