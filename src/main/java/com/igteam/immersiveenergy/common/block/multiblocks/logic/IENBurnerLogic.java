package com.igteam.immersiveenergy.common.block.multiblocks.logic;

import blusunrize.immersiveengineering.api.energy.NullEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.*;
import blusunrize.immersiveengineering.api.utils.CapabilityReference;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.interfaces.MBOverlayText;
import blusunrize.immersiveengineering.common.util.CachedRecipe;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.inventory.SlotwiseItemHandler;
import com.google.common.collect.ImmutableList;
import com.igteam.immersiveenergy.ImmersiveEnergy;
import com.igteam.immersiveenergy.common.block.multiblocks.recipe.BurnerFuel;
import com.igteam.immersiveenergy.common.block.multiblocks.shapes.FullblockShape;
import com.igteam.immersiveenergy.core.lib.IENLib;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class IENBurnerLogic implements IMultiblockLogic<IENBurnerLogic.State>, IServerTickableComponent<IENBurnerLogic.State>, IClientTickableComponent<IENBurnerLogic.State>, MBOverlayText<IENBurnerLogic.State>
{
    public static final BlockPos MASTER_OFFSET = new BlockPos(0,0,0);
    public static final BlockPos FURNACE_POS = new BlockPos(1,1,1);
    private static final List<BlockPos> ENERGY_OUTPUTS = List.of(new BlockPos(0,2,0), new BlockPos(0,2,2));
    public static final int INPUT_SLOT = 0;
    public static final int NUM_SLOTS = 1;

    @Override
    public void tickClient(IMultiblockContext<State> ctx)
    {

    }

    @Override
    public void tickServer(IMultiblockContext<State> ctx)
    {
        final State state = ctx.getState();
        boolean active = state.active;
        //IENLib.IEN_LOGGER.info(state.inventory.getStackInSlot(0).getDisplayName().getString());
        boolean hasFuel = state.hasFuel;
        int output = state.output;
        if (state.burnTime > 0)
        {
            List<IEnergyStorage> presentOutputs = state.energyOutputs.stream()
                .map(CapabilityReference::getNullable)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            if (!presentOutputs.isEmpty() && EnergyHelper.distributeFlux(presentOutputs, output, true) < output)
            {
                EnergyHelper.distributeFlux(presentOutputs, output, false);
            }
            state.burnTime--;
        }
        if (hasFuel && state.burnTime <= 0)
        {
            BurnerFuel recipe = BurnerFuel.getRecipeFor(ctx.getLevel().getRawLevel(),state.inventory.getStackInSlot(INPUT_SLOT));
            if (recipe!=null)
            {
                state.burnTime = recipe.burnTime;
                state.output = recipe.output;
                /*ItemStack oldFuel = state.invCap.getValue().getStackInSlot(INPUT_SLOT);
                oldFuel.shrink(1);*/
                state.invCap.getValue().extractItem(INPUT_SLOT, 1, false);
                if (!active) active=true;
            }
            else if (active) active=false;
        }
        state.active=active;
        ctx.markMasterDirty();
        ctx.requestMasterBESync();
    }

    @Override
    public State createInitialState(IInitialMultiblockContext<State> context)
    {
        return new IENBurnerLogic.State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType shapeType) {
        return FullblockShape.GETTER;
    }

    @Override
    public <T>
    LazyOptional<T> getCapability(IMultiblockContext<State> ctx, CapabilityPosition position, Capability<T> cap)
    {
        if (cap==ForgeCapabilities.ITEM_HANDLER)
            return ctx.getState().invCap.cast(ctx);
        if (cap==ForgeCapabilities.ENERGY)
        {
            IENLib.IEN_LOGGER.info(position.toString());
            if (position.side()==null||(position.side()==RelativeBlockFace.UP&&ENERGY_OUTPUTS.contains(position.posInMultiblock())))
            {

                return ctx.getState().energyView.cast(ctx);
            }
        }
        return LazyOptional.empty();
    }


    @Override
    public InteractionResult click(IMultiblockContext<State> ctx, BlockPos pos, Player player, InteractionHand hand, BlockHitResult absoluteHit, boolean isClient)
    {
        if (pos.equals(FURNACE_POS) && !isClient)
        {
            Level level = ctx.getLevel().getRawLevel();
            ItemStack stack = player.getItemInHand(hand);
            if (BurnerFuel.getRecipeFor(level, stack)!=null)
            {
                final State state = ctx.getState();
                state.hasFuel = true;
                ItemStack fuel = state.inventory.getStackInSlot(INPUT_SLOT);
                if (fuel.isEmpty())
                {
                    state.invCap.getValue().insertItem(INPUT_SLOT, stack.copy(), false);
                    stack.setCount(0);
                    ctx.markDirtyAndSync();
                    return InteractionResult.SUCCESS;
                }
                if (stack.is(fuel.getItem()))
                {
                    if (stack.getCount() + fuel.getCount() > stack.getMaxStackSize())
                    {
                        stack.shrink(stack.getMaxStackSize() - fuel.getCount());
                        fuel.setCount(stack.getMaxStackSize());
                        ctx.markDirtyAndSync();
                        return InteractionResult.SUCCESS;
                    }
                    fuel.grow(stack.getCount());
                    stack.setCount(0);
                    ctx.markDirtyAndSync();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public List<Component> getOverlayText(State state, Player player, boolean b)
    {
        ItemStack stack = state.inventory.getStackInSlot(0);
        return List.of(Component.literal("hasFuel: "  + state.hasFuel), stack.getDisplayName(),
                Component.literal("Count: "+stack.getCount()), Component.literal("Burn Time: "+state.burnTime),
                Component.literal("Output: "+state.output+ " RF"));
    }

    @Override
    public void dropExtraItems(State state, Consumer<ItemStack> drop)
    {
        MBInventoryUtils.dropItems(state.inventory, drop);
    }



    public static class State implements IMultiblockState
    {
        private boolean active = false;
        private boolean hasFuel = false;
        private int burnTime = 0;
        private int output = 0;
        private final SlotwiseItemHandler inventory;
        private final StoredCapability<IItemHandler> invCap;
        private final List<CapabilityReference<IEnergyStorage>> energyOutputs;
        private final StoredCapability<IEnergyStorage> energyView;

        public State(IInitialMultiblockContext<State> ctx)
        {
            final Supplier<@Nullable Level> levelGetter = ctx.levelSupplier();
            ImmutableList.Builder<CapabilityReference<IEnergyStorage>> outputs = ImmutableList.builder();
            for(BlockPos pos : ENERGY_OUTPUTS)
            {
                outputs.add(ctx.getCapabilityAt(ForgeCapabilities.ENERGY, pos, RelativeBlockFace.DOWN));
            }
            this.energyOutputs = outputs.build();
            this.inventory = new SlotwiseItemHandler(List.of(
                    SlotwiseItemHandler.IOConstraint.input(i -> BurnerFuel.getRecipeFor(levelGetter.get(), i)!=null)
                ),
                ctx.getMarkDirtyRunnable()
            );
            this.energyView = new StoredCapability<>(NullEnergyStorage.INSTANCE);
            this.invCap = new StoredCapability<>(this.inventory);
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt)
        {
            nbt.putBoolean("active", active);
            nbt.putBoolean("fuel", hasFuel);
            nbt.putInt("burnTime", burnTime);
            nbt.putInt("output", output);
            nbt.put("inventory", inventory.serializeNBT());
        }

        @Override
        public void readSaveNBT(CompoundTag nbt)
        {
            active = nbt.getBoolean("active");
            hasFuel = nbt.getBoolean("fuel");
            burnTime = nbt.getInt("burnTime");
            output = nbt.getInt("output");
            inventory.deserializeNBT(nbt.getCompound("inventory"));
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt)
        {
            writeSaveNBT(nbt);
            nbt.putBoolean("active", active);
        }

        @Override
        public void readSyncNBT(CompoundTag nbt)
        {
            readSaveNBT(nbt);
            final boolean oldActive = active;
            active = nbt.getBoolean("active");
            if(active&&!oldActive)
            {
                //animation_fanFadeIn = 80;
            }
            else if(!active&&oldActive)
            {
                //animation_fanFadeOut = 80;
            }
        }

        public boolean isActive()
        {
            return active;
        }

        public SlotwiseItemHandler getInventory()
        {
            return inventory;
        }
    }
}
