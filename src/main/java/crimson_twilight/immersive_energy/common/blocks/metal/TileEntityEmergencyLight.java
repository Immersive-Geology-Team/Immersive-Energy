package crimson_twilight.immersive_energy.common.blocks.metal;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.AbstractConnection;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorMV;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IEForgeEnergyWrapper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEInternalFluxConnector;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEInternalFluxHandler;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import crimson_twilight.immersive_energy.common.Config.IEnConfig.Machines;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TileEntityEmergencyLight extends TileEntityImmersiveConnectable implements ITickable, IIEInternalFluxHandler, ITileDrop, IIEInternalFluxConnector, IOBJModelCallback<IBlockState> {

    public boolean active;
    public int currentTickAccepted = 0;
    private FluxStorage energyStorage = new FluxStorage(getMaxStorage(), getMaxInput(), 0);
    boolean firstTick = true;

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        active = nbt.getBoolean("active");
        energyStorage.readFromNBT(nbt);

        if (descPacket)
            this.markContainingBlockForUpdate(null);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        nbt.setBoolean("active", active);
        energyStorage.writeToNBT(nbt);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing);
    }

    @Override
    public void update() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return super.getMaxRenderDistanceSquared() * IEConfig.increasedTileRenderdistance;
    }

    @Override
    public void readOnPlacement(EntityLivingBase placer, ItemStack stack) {
        if (stack.hasTagCompound()) {
            if (ItemNBTHelper.hasKey(stack, "energyStorage"))
                energyStorage.setEnergy(ItemNBTHelper.getInt(stack, "energyStorage"));
        }
    }


    @Override
    public ItemStack getTileDrop(EntityPlayer player, IBlockState state) {
        ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
        NBTTagCompound tag = new NBTTagCompound();
        if (!tag.hasNoTags())
            stack.setTagCompound(tag);
        if (energyStorage.getEnergyStored() > 0)
            ItemNBTHelper.setInt(stack, "energyStorage", energyStorage.getEnergyStored());
        return stack;
    }

    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        return new Vec3d(0.5f, 0.156f, 0.5f);
    }

    @Override
    public Vec3d getConnectionOffset(Connection con) {

        return new Vec3d(0.5f, 0.156f, 0.5f);
    }

    @Override
    public boolean isEnergyOutput() {
        return false;
    }

    @Override
    public int outputEnergy(int amount, boolean simulate, int energyType) {
        return 0;
    }

    @Override
    protected boolean canTakeLV() {
        return true;
    }

    @Override
    protected boolean canTakeMV() {
        return true;
    }

    IEForgeEnergyWrapper energyWrapper;

    @Override
    public IEForgeEnergyWrapper getCapabilityWrapper(EnumFacing facing) {
        return energyWrapper;
    }

    @Override
    public FluxStorage getFluxStorage() {
        return energyStorage;
    }

    @Override
    public SideConfig getEnergySideConfig(EnumFacing facing) {
        return SideConfig.OUTPUT;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return false;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energyStorage.getEnergyStored();
    }

    private int getMaxStorage() {
        return Machines.storage_solar;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return getMaxStorage();
    }

    @Override
    public int extractEnergy(EnumFacing from, int energy, boolean simulate) {
        return 0;
    }

    public int getMaxInput() {
        return TileEntityConnectorMV.connectorInputValues[1];
    }

    public int getMaxOutput() {
        return TileEntityConnectorMV.connectorInputValues[1];
    }

    public int transferEnergy(int energy, boolean simulate, final int energyType) {
        int received = 0;
        if (!world.isRemote) {
            Set<AbstractConnection> outputs = ImmersiveNetHandler.INSTANCE.getIndirectEnergyConnections(Utils.toCC(this), world);
            int powerLeft = Math.min(Math.min(getMaxOutput(), getMaxInput()), energy);
            final int powerForSort = powerLeft;

            if (outputs.size() < 1)
                return 0;

            int sum = 0;
            HashMap<AbstractConnection, Integer> powerSorting = new HashMap<AbstractConnection, Integer>();
            for (AbstractConnection con : outputs) {
                IImmersiveConnectable end = ApiUtils.toIIC(con.end, world);
                if (con.cableType != null && end != null) {
                    int atmOut = Math.min(powerForSort, con.cableType.getTransferRate());
                    int tempR = end.outputEnergy(atmOut, true, energyType);
                    if (tempR > 0) {
                        powerSorting.put(con, tempR);
                        sum += tempR;
                    }
                }
            }

            if (sum > 0)
                for (AbstractConnection con : powerSorting.keySet()) {
                    IImmersiveConnectable end = ApiUtils.toIIC(con.end, world);
                    if (con.cableType != null && end != null) {
                        float prio = powerSorting.get(con) / (float) sum;
                        int output = (int) (powerForSort * prio);

                        int tempR = end.outputEnergy(Math.min(output, con.cableType.getTransferRate()), true, energyType);
                        int r = tempR;
                        int maxInput = getMaxInput();
                        tempR -= (int) Math.max(0, Math.floor(tempR * con.getPreciseLossRate(tempR, maxInput)));
                        end.outputEnergy(tempR, simulate, energyType);
                        HashSet<IImmersiveConnectable> passedConnectors = new HashSet<IImmersiveConnectable>();
                        float intermediaryLoss = 0;
                        for (Connection sub : con.subConnections) {
                            float length = sub.length / (float) sub.cableType.getMaxLength();
                            float baseLoss = (float) sub.cableType.getLossRatio();
                            float mod = (((maxInput - tempR) / (float) maxInput) / .25f) * .1f;
                            intermediaryLoss = MathHelper.clamp(intermediaryLoss + length * (baseLoss + baseLoss * mod), 0, 1);

                            int transferredPerCon = ImmersiveNetHandler.INSTANCE.getTransferedRates(world.provider.getDimension()).containsKey(sub) ? ImmersiveNetHandler.INSTANCE.getTransferedRates(world.provider.getDimension()).get(sub) : 0;
                            transferredPerCon += r;
                            if (!simulate) {
                                ImmersiveNetHandler.INSTANCE.getTransferedRates(world.provider.getDimension()).put(sub, transferredPerCon);
                                IImmersiveConnectable subStart = ApiUtils.toIIC(sub.start, world);
                                IImmersiveConnectable subEnd = ApiUtils.toIIC(sub.end, world);
                                if (subStart != null && passedConnectors.add(subStart))
                                    subStart.onEnergyPassthrough((int) (r - r * intermediaryLoss));
                                if (subEnd != null && passedConnectors.add(subEnd))
                                    subEnd.onEnergyPassthrough((int) (r - r * intermediaryLoss));
                            }
                        }
                        received += r;
                        powerLeft -= r;
                        if (powerLeft <= 0)
                            break;
                    }
                }
        }
        return received;
    }

}