package com.igteam.immersiveenergy.core.lib;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class IENLib
{
    public static final String MODID = "immersiveenergy";
    public static final String VERSION = "1.0.0";

    public static final Logger IEN_LOGGER = LogUtils.getLogger();


    public static Logger getNewLogger()
    {
        return  LogUtils.getLogger();
    }
}
