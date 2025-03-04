package com.igteam.immersiveenergy.common.data.generators;

import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

public class IENItemModelProvider extends ItemModelProvider
{
    private final Logger logger = IENLib.getNewLogger();
    public IENItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), IENLib.MODID, existingFileHelper);
    }

    private void generateBlockItem(String item_name, String parent_loc)
    {
        String itemLocation = new ResourceLocation(IENLib.MODID, "item/"+ item_name).getPath();
        ResourceLocation parentLocation = new ResourceLocation(IENLib.MODID, "block/"+parent_loc);

        withExistingParent(itemLocation, parentLocation);
    }

    @Override
    protected void registerModels() {

    }
}
