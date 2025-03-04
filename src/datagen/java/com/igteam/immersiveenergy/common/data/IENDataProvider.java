package com.igteam.immersiveenergy.common.data;

import com.igteam.immersiveenergy.common.data.generators.*;
import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = IENLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IENDataProvider {
    public static Logger log = LogManager.getLogger(IENLib.MODID + "/DataGenerator");

    @SubscribeEvent
    public static void generate(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput out = generator.getPackOutput();
        final var lookup = event.getLookupProvider();

        log.info("-===== Starting Data Generation for Immersive Energy =====-");

        if(event.includeServer()){
            IENBlockStateProvider blockStateProvider = new IENBlockStateProvider(generator, helper);
            generator.addProvider(true, blockStateProvider);
            generator.addProvider(true, new IENItemModelProvider(generator, helper));
            generator.addProvider(true, new IENComplexItemModelProvider(out, helper));
            BlockTagsProvider blockTags = new IENBlockTags(out, lookup, helper);
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new IENFluidTags(out, lookup, helper));
            generator.addProvider(true, new IENItemTags(out, lookup, blockTags.contentsGetter(), helper));
            generator.addProvider(true, new IENDynamicModelProvider(blockStateProvider, out, helper));
            generator.addProvider(true, new IENRecipes(out));
            generator.addProvider(true, new LootTableProvider(out, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(IENBlockLootProvider::new, LootContextParamSets.BLOCK))));
        }
    }

}