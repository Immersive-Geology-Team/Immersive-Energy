package com.igteam.immersiveenergy.common.data.generators;

import blusunrize.immersiveengineering.api.IETags;
import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class IENItemTags extends ItemTagsProvider
{
    public IENItemTags(PackOutput output, CompletableFuture<Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blocks, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blocks, IENLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider)
    {

    }
}
