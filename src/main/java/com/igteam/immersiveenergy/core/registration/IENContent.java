package com.igteam.immersiveenergy.core.registration;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.Tree.InnerNode;
import com.igteam.immersiveenergy.common.tag.IENTags;
import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

public class IENContent
{
    public static void modContruction(IEventBus event)
    {
        IENLib.IEN_LOGGER.info("Registering Multiblocks to Immersive Engineering");
        IENMultiblockProvider.forceClassLoad();
        IENRegistrationHolder.initialize();
        IENTags.initialize();
        IENRecipeTypes.init();
    }

    public static void initializeManualEntries()
    {
        ManualInstance instance = ManualHelper.getManual();
        InnerNode<ResourceLocation, ManualEntry> parent_category = instance.getRoot().getOrCreateSubnode(new ResourceLocation(IENLib.MODID, "main"), 99);

        ManualEntry.ManualEntryBuilder builder = new ManualEntry.ManualEntryBuilder(ManualHelper.getManual());
        builder.readFromFile(new ResourceLocation(IENLib.MODID, "intro"));
        instance.addEntry(parent_category, builder.create());

        InnerNode<ResourceLocation, ManualEntry> multiblock_category = parent_category.getOrCreateSubnode(new ResourceLocation(IENLib.MODID, "ien_multiblocks"), 0);
    }

    private static void multiblockEntry(ManualInstance instance, InnerNode<ResourceLocation, ManualEntry> category, String id)
    {
        ManualEntry.ManualEntryBuilder multiblock = new ManualEntry.ManualEntryBuilder(ManualHelper.getManual());
        multiblock.readFromFile(new ResourceLocation(IENLib.MODID, id));
        instance.addEntry(category, multiblock.create());
    }

    public static void initialize(ParallelDispatchEvent event)
    {

    }
}
