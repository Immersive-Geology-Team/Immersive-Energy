package com.igteam.immersiveenergy.common.data.generators;

import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class IENBlockTags extends BlockTagsProvider
{
    public IENBlockTags(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, IENLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider)
    {
        IENLib.IEN_LOGGER.info("HRST Block Tags");
    }
}
