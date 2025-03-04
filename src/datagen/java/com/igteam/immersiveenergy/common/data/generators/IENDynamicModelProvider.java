package com.igteam.immersiveenergy.common.data.generators;


import com.igteam.immersiveenergy.common.data.generators.IENBlockStateProvider;
import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.loaders.ObjModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.Map.Entry;

public class IENDynamicModelProvider extends ModelProvider<IENDynamicModelProvider.SimpleModelBuilder>
{
    private final IENBlockStateProvider multiblocks;

    public IENDynamicModelProvider(IENBlockStateProvider multiblocks, PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, IENLib.MODID, "dynamic", rl -> new SimpleModelBuilder(rl, existingFileHelper), existingFileHelper);
        this.multiblocks = multiblocks;
    }

    @Override
    protected void registerModels()
    {
        for(Entry<Block, ModelFile> multiblock : multiblocks.unsplitModels.entrySet())
            withExistingParent(BuiltInRegistries.BLOCK.getKey(multiblock.getKey()).getPath(), multiblock.getValue().getLocation());
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(IENLib.MODID, path);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "IEN Dynamic models";
    }

    public static class SimpleModelBuilder extends ModelBuilder<SimpleModelBuilder>
    {

        public SimpleModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper)
        {
            super(outputLocation, existingFileHelper);
        }
    }
}
