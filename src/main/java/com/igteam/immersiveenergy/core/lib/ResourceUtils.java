package com.igteam.immersiveenergy.core.lib;

import blusunrize.immersiveengineering.api.Lib;
import net.minecraft.resources.ResourceLocation;



public class ResourceUtils
{
    public static ResourceLocation ien(String path)
    {
        return new ResourceLocation(IENLib.MODID, path);
    }

    public static ResourceLocation ie(String path){
        return new ResourceLocation(Lib.MODID, path);
    }
}
