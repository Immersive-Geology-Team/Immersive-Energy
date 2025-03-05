package com.igteam.immersiveenergy.common.block.multiblocks;

import blusunrize.immersiveengineering.api.multiblocks.ClientMultiblocks;
import com.igteam.immersiveenergy.common.block.multiblocks.helper.IENClientMultiblockProperties;
import com.igteam.immersiveenergy.core.lib.IENLib;
import com.igteam.immersiveenergy.core.registration.IENMultiblockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class IENBurner extends IENTemplateMultiblock {

    public static final IENBurner INSTANCE = new IENBurner();

    public IENBurner() {
        super(new ResourceLocation(IENLib.MODID, "multiblocks/burner"),
                new BlockPos(0,0,0), new BlockPos(1,1,1), new BlockPos(3,3,2),
                IENMultiblockProvider.BURNER);
    }

    @Override
    public float getManualScale() {
        return 16;
    }

    @Override
    public void initializeClient(Consumer<ClientMultiblocks.MultiblockManualData> consumer) {
        consumer.accept(new IENClientMultiblockProperties(this, 0,0,0));
    }
}
