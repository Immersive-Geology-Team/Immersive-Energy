package com.igteam.immersiveenergy.common.data.generators;

import net.minecraft.core.HolderLookup.Provider;
import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class IENFluidTags extends FluidTagsProvider
{
    public IENFluidTags(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, IENLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider)
    {
    }
}
