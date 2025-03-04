package com.igteam.immersiveenergy.common.block.helper;

import net.minecraft.world.level.block.Block;

public interface IENBlockType {
    Block getBlock();

    int getColor(int index);
}
