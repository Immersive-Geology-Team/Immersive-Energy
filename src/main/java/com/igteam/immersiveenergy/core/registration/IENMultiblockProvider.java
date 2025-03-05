package com.igteam.immersiveenergy.core.registration;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import com.igteam.immersiveenergy.common.block.multiblocks.logic.IENBurnerLogic;

public class IENMultiblockProvider
{
    public static final MultiblockRegistration<IENBurnerLogic.State> BURNER = IENRegistrationHolder.registerMetalMultiblock("burner", new IENBurnerLogic(), () -> IENRegistrationHolder.getMBTemplate.apply("burner"));

    public static void forceClassLoad(){};
}
