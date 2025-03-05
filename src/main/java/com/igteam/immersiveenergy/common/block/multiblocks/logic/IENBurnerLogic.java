package com.igteam.immersiveenergy.common.block.multiblocks.logic;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.igteam.immersiveenergy.common.block.multiblocks.IENBurner;
import com.igteam.immersiveenergy.common.block.multiblocks.shapes.FullblockShape;
import com.igteam.immersiveenergy.common.block.multiblocks.shapes.GenericShape;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class IENBurnerLogic implements IMultiblockLogic<IENBurnerLogic.State>, IServerTickableComponent<IENBurnerLogic.State>, IClientTickableComponent<IENBurnerLogic.State> {

    @Override
    public void tickClient(IMultiblockContext<State> iMultiblockContext) {

    }

    @Override
    public void tickServer(IMultiblockContext<State> iMultiblockContext) {

    }

    @Override
    public State createInitialState(IInitialMultiblockContext<State> context) {
        return new IENBurnerLogic.State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType shapeType) {
        return FullblockShape.GETTER;
    }

    public static class State implements IMultiblockState
    {

        public State(IInitialMultiblockContext<State> ctx){

        }

        @Override
        public void writeSaveNBT(CompoundTag compoundTag) {

        }

        @Override
        public void readSaveNBT(CompoundTag compoundTag) {

        }
    }
}
