package crimson_twilight.immersive_energy.common.blocks.multiblock;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IEEnums;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import crimson_twilight.immersive_energy.common.Config;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.IEnGUIList;
import crimson_twilight.immersive_energy.common.compat.ImmersiveIntelligenceHelper;
import crimson_twilight.immersive_energy.common.util.IEnCommonUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static crimson_twilight.immersive_energy.common.Config.IEnConfig.Machines.*;

/**
 * @author Pabilo8
 * @author Crimson Twilight
 * @since 02.12.2020
 */

@Optional.Interface(iface = "elucent.albedo.lighting.pl.pabilo8.immersiveintelligence.api.data.IDataDevice", modid = "immersiveintelligence")
public class TileEntityFluidBattery extends TileEntityMultiblockMetal<TileEntityFluidBattery, IMultiblockRecipe> implements IAdvancedSelectionBounds, IAdvancedCollisionBounds, IOBJModelCallback<IBlockState>, IGuiTile, IDataDevice {
    public FluidTank[] tanks = {
            new FluidTank(FluidBattery.fluidCapacity),
            new FluidTank(FluidBattery.fluidCapacity)
    };

    //When true, II takes the control of each port independently, using data
    public boolean dataControlMode = false;
    //true - output, false - input
    public boolean[] controlModes = new boolean[]{false, true, true, false};

    public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

    @SideOnly(Side.CLIENT)
    private AxisAlignedBB renderAABB;

    public TileEntityFluidBattery() {
        super(MultiblockFluidBattery.instance, new int[]{3, 4, 5}, 0, true);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        tanks[0].readFromNBT(nbt.getCompoundTag("tank0"));
        tanks[1].readFromNBT(nbt.getCompoundTag("tank1"));

        if (!descPacket) {
            nbt.setTag("inventory", Utils.writeInventory(inventory));
        }

        dataControlMode = nbt.getBoolean("dataControlMode");
        controlModes = new boolean[]{
                nbt.getBoolean("b1"),
                nbt.getBoolean("b2"),
                nbt.getBoolean("b3"),
                nbt.getBoolean("b4")
        };
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setTag("tank0", tanks[0].writeToNBT(new NBTTagCompound()));
        nbt.setTag("tank1", tanks[1].writeToNBT(new NBTTagCompound()));

        if (!descPacket) {
            inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
        }

        nbt.setBoolean("dataControlMode", dataControlMode);
        nbt.setBoolean("b1", controlModes[0]);
        nbt.setBoolean("b2", controlModes[1]);
        nbt.setBoolean("b3", controlModes[2]);
        nbt.setBoolean("b4", controlModes[3]);
    }

    @Override
    public void receiveMessageFromClient(NBTTagCompound message) {
        super.receiveMessageFromClient(message);

        if (message.hasKey("dataControlMode"))
            dataControlMode = message.getBoolean("dataControlMode");
        if (message.hasKey("b1"))
            controlModes[0] = message.getBoolean("b1");
        if (message.hasKey("b2"))
            controlModes[1] = message.getBoolean("b2");
        if (message.hasKey("b3"))
            controlModes[2] = message.getBoolean("b3");
        if (message.hasKey("b4"))
            controlModes[3] = message.getBoolean("b4");

        ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, message), IEnCommonUtils.targetPointFromTile(this, 32));
    }

    @Override
    public void receiveMessageFromServer(NBTTagCompound message) {
        super.receiveMessageFromServer(message);

        if (message.hasKey("dataControlMode"))
            dataControlMode = message.getBoolean("dataControlMode");
        if (message.hasKey("b1"))
            controlModes[0] = message.getBoolean("b1");
        if (message.hasKey("b2"))
            controlModes[1] = message.getBoolean("b2");
        if (message.hasKey("b3"))
            controlModes[2] = message.getBoolean("b3");
        if (message.hasKey("b4"))
            controlModes[3] = message.getBoolean("b4");
    }

    @Override
    protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag) {
        return null;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update() {
        super.update();

        if (isDummy())
            return;

        IEnCommonUtils.handleBucketTankInteraction(tanks, inventory, 0, 1, 0, "discharge_fluid");
        IEnCommonUtils.handleBucketTankInteraction(tanks, inventory, 2, 3, 1, "charge_fluid");

        if (!world.isRemote) {
            FluidStack out1 = Utils.copyFluidStackWithAmount(this.tanks[0].getFluid(), Math.min(this.tanks[0].getFluidAmount(), 80), false);
            FluidStack out2 = Utils.copyFluidStackWithAmount(this.tanks[1].getFluid(), Math.min(this.tanks[1].getFluidAmount(), 80), false);
            IFluidHandler output1 = FluidUtil.getFluidHandler(world, getBlockPosForPos(16).offset(facing), facing.getOpposite());
            IFluidHandler output2 = FluidUtil.getFluidHandler(world, getBlockPosForPos(18).offset(facing), facing.getOpposite());

            boolean update = transferFluid(out1, output1);
            if (transferFluid(out2, output2))
                update = true;

            for (int i = 0; i < controlModes.length; i++)
                this.transferEnergy(i);

            if (update) {

            }
        }
    }

    private void transferEnergy(int i) {
        IEEnums.SideConfig energyBlockConfig = getEnergyBlockConfig(getEnergyPos()[i]);

        if (energyBlockConfig != IEEnums.SideConfig.OUTPUT)
            return;
        BlockPos outPos = getBlockPosForPos(getEnergyPos()[i]).up();
        TileEntity tileEntity = Utils.getExistingTileEntity(world, outPos);

        int out = getMaxOutput();
        int remaining = EnergyHelper.insertFlux(tileEntity, EnumFacing.DOWN, out, false);
        tanks[1].drain(remaining/FluidBattery.IFAmount,true);
        tanks[0].fill(new FluidStack(IEnContent.fluidDischarge,remaining/FluidBattery.IFAmount),true);
    }

    private int getMaxOutput() {
        int maxTank = tanks[0].getCapacity()-tanks[0].getFluidAmount();
        return Math.min(Math.min(tanks[1].getFluidAmount(),maxTank) * FluidBattery.IFAmount, FluidBattery.maxOutput);
    }

    private boolean transferFluid(FluidStack out1, IFluidHandler output1) {
        if (output1 != null) {
            int accepted = output1.fill(out1, false);
            if (accepted > 0) {
                int drained = output1.fill(Utils.copyFluidStackWithAmount(out1, Math.min(out1.amount, accepted), false), true);
                this.tanks[0].drain(drained, true);
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().getX() - (facing.getAxis() == Axis.Z ? 2 : 1), getPos().getY(), getPos().getZ() - (facing.getAxis() == Axis.X ? 2 : 1), getPos().getX() + (facing.getAxis() == Axis.Z ? 3 : 2), getPos().getY() + 3, getPos().getZ() + (facing.getAxis() == Axis.X ? 3 : 2));
    }

    @Override
    public float[] getBlockBounds() {
		/*if(pos==1||pos==3||pos==4||pos==6||pos==8||pos==11||pos==12||pos==13||pos==14||pos==24)
			return new float[]{0, 0, 0, 1, .5f, 1};
		if(pos==22)
			return new float[]{0, 0, 0, 1, .75f, 1};
		if(pos==37)
			return new float[]{0, 0, 0, 0, 0, 0};

		EnumFacing fl = facing;
		EnumFacing fw = facing.rotateY();
		if(mirrored)
			fw = fw.getOpposite();
		if(pos > 15&&pos%5 > 0&&pos%5 < 4)
		{
			float minX = 0;
			float maxX = 1;
			float minZ = 0;
			float maxZ = 1;
			if(pos%5==1)
			{
				minX = fw==EnumFacing.EAST?.1875f: 0;
				maxX = fw==EnumFacing.WEST?.8125f: 1;
				minZ = fw==EnumFacing.SOUTH?.1875f: 0;
				maxZ = fw==EnumFacing.NORTH?.8125f: 1;
			}
			else if(pos%5==3)
			{
				minX = fw==EnumFacing.WEST?.1875f: 0;
				maxX = fw==EnumFacing.EAST?.8125f: 1;
				minZ = fw==EnumFacing.NORTH?.1875f: 0;
				maxZ = fw==EnumFacing.SOUTH?.8125f: 1;
			}
			if((pos%15)/5==0)
			{
				if(fl==EnumFacing.EAST)
					minX = .1875f;
				if(fl==EnumFacing.WEST)
					maxX = .8125f;
				if(fl==EnumFacing.SOUTH)
					minZ = .1875f;
				if(fl==EnumFacing.NORTH)
					maxZ = .8125f;
			}

			return new float[]{minX, 0, minZ, maxX, 1, maxZ};
		}
		if(pos==19)
			return new float[]{facing==EnumFacing.WEST?.5f: 0, 0, facing==EnumFacing.NORTH?.5f: 0, facing==EnumFacing.EAST?.5f: 1, 1, facing==EnumFacing.SOUTH?.5f: 1};
*/
        return new float[]{0, 0, 0, 1, 1, 1};
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds() {
        return null;
    }

    @Override
    public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list) {
        return false;
    }

    @Override
    public List<AxisAlignedBB> getAdvancedColisionBounds() {
        return getAdvancedSelectionBounds();
    }

    @Override
    public int[] getEnergyPos() {
        return new int[]{45, 49, 56, 58};
    }

    @Override
    public int[] getRedstonePos() {
        return new int[]{25};
    }

    @Override
    public boolean isInWorldProcessingMachine() {
        return true;
    }

    @Override
    public void doProcessOutput(ItemStack output) {
        BlockPos pos = getPos().add(0, -1, 0).offset(facing, -2);
        TileEntity inventoryTile = this.world.getTileEntity(pos);
        if (inventoryTile != null)
            output = Utils.insertStackIntoInventory(inventoryTile, output, facing);
        if (!output.isEmpty())
            Utils.dropStackAtPos(world, pos, output, facing.getOpposite());
    }

    @Override
    public void doProcessFluidOutput(FluidStack output) {
    }

    @Override
    public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process) {

    }

    @Override
    public int getMaxProcessPerTick() {
        return 0;
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 0;
    }

    @Override
    public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process) {
        return 0;
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public boolean isStackValid(int slot, ItemStack stack) {
        return !stack.isEmpty() && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 16;
    }

    @Override
    public int[] getOutputSlots() {
        return null;
    }

    @Override
    public int[] getOutputTanks() {
        return new int[]{1};
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process) {
        return false;
    }

    @Override
    public IFluidTank[] getInternalTanks() {
        return tanks;
    }

    @Override
    public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting) {
        return null;
    }

    @Override
    protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side) {
        TileEntityFluidBattery master = this.master();
        if (master != null) {
            if (pos == 1 || pos == 16)
                return new FluidTank[]{master.tanks[0]};
            else if (pos == 3 || pos == 18)
                return new FluidTank[]{master.tanks[1]};
        }
        return new FluidTank[0];
    }

    @Override
    protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resources) {
        TileEntityFluidBattery master = master();
        if (master == null)
            return false;

        if (iTank == 0)
            return side == master.facing.getOpposite();
        else if (iTank == 1)
            return side == master.facing.getOpposite();

        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int iTank, EnumFacing side) {
        TileEntityFluidBattery master = master();
        if (master == null)
            return false;

        if (iTank == 0)
            return side == master.facing;
        else if (iTank == 1)
            return side == master.facing;

        return false;
    }

    @Override
    public void doGraphicalUpdates(int slot) {
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        return super.getCapability(capability, facing);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderGroup(IBlockState object, String group) {
        boolean isIIDataPort = (!ImmersiveIntelligenceHelper.ii && "BoxInputData".equals(group));
        //boolean isGlass = "glass".equals(group);
        return !(isIIDataPort);
    }

    @Override
    public boolean canOpenGui() {
        return true;
    }

    @Override
    public int getGuiID() {
        return IEnGUIList.GUI_FLUID_BATTERY;
    }

    @Override
    public TileEntity getGuiMaster() {
        return master();
    }

    //Immersive Intelligence Compat Works™
    @Override
    public void onReceive(DataPacket dataPacket, @Nullable EnumFacing enumFacing) {
        TileEntityFluidBattery master = master();
        if (pos == 29 && master != null && enumFacing == master.facing.rotateY()) {
            //ImmersiveEnergy.logger.info("Otak!");

            IDataType c = dataPacket.getPacketVariable('c');
            if (dataControlMode&&dataPacket.getPacketVariable('1') instanceof DataPacketTypeBoolean)
                controlModes[0] = ((DataPacketTypeBoolean) dataPacket.getPacketVariable('1')).value;
            if (dataControlMode&&dataPacket.getPacketVariable('2') instanceof DataPacketTypeBoolean)
                controlModes[1] = ((DataPacketTypeBoolean) dataPacket.getPacketVariable('2')).value;
            if (dataControlMode&&dataPacket.getPacketVariable('3') instanceof DataPacketTypeBoolean)
                controlModes[2] = ((DataPacketTypeBoolean) dataPacket.getPacketVariable('3')).value;
            if (dataControlMode&&dataPacket.getPacketVariable('4') instanceof DataPacketTypeBoolean)
                controlModes[3] = ((DataPacketTypeBoolean) dataPacket.getPacketVariable('4')).value;
            if (c instanceof DataPacketTypeString) {
                IDataConnector conn = pl.pabilo8.immersiveintelligence.api.Utils.findConnectorFacing(getBlockPosForPos(29), world, master.facing.rotateY());
                DataPacket p = new DataPacket();

                switch (((DataPacketTypeString) c).value) {
                    case "get_energy":
                    case "get_in":
                        p.setVariable('c', new DataPacketTypeString("fluid_in"));
                        p.setVariable('g', new DataPacketTypeInteger(tanks[0].getFluidAmount()));
                        if (conn != null)
                            conn.sendPacket(p);
                        break;
                    case "get_out":
                        p.setVariable('c', new DataPacketTypeString("fluid_out"));
                        p.setVariable('g', new DataPacketTypeInteger(tanks[1].getFluidAmount()));
                        if (conn != null)
                            conn.sendPacket(p);
                        break;
                    case "set_mode": {
                        IDataType m = p.getPacketVariable('m');
                        if (m instanceof DataPacketTypeBoolean)
                            dataControlMode = ((DataPacketTypeBoolean) m).value;
                    }
                    break;
                    case "set_redstone": {
                        dataControlMode = false;
                    }
                    break;
                    case "set_data": {
                        dataControlMode = true;
                    }
                    break;
                    case "enable_data":
                        dataControlMode = true;
                        break;
                    case "disable_data":
                        dataControlMode = false;
                        break;
                    case "toggle_data":
                        dataControlMode = !dataControlMode;
                        break;
                }
            }
        }
    }

    @Override
    public void onSend() {

    }

    @Nonnull
    @Override
    public IEEnums.SideConfig getEnergySideConfig(EnumFacing facing) {
        TileEntityFluidBattery master = master();
        if (facing == EnumFacing.UP && master != null && master.formed) {
            return master.getEnergyBlockConfig(this.pos);
        }
        return IEEnums.SideConfig.NONE;
    }

    private IEEnums.SideConfig getEnergyBlockConfig(int pos) {
        switch (pos) {
            case 45:
                return this.controlModes[0] ? IEEnums.SideConfig.OUTPUT : IEEnums.SideConfig.INPUT;
            case 49:
                return this.controlModes[1] ? IEEnums.SideConfig.OUTPUT : IEEnums.SideConfig.INPUT;
            case 56:
                return this.controlModes[2] ? IEEnums.SideConfig.OUTPUT : IEEnums.SideConfig.INPUT;
            case 58:
                return this.controlModes[3] ? IEEnums.SideConfig.OUTPUT : IEEnums.SideConfig.INPUT;
            default:
                return IEEnums.SideConfig.NONE;
        }
    }

    EnergyHelper.IEForgeEnergyWrapper wrapper = new EnergyHelper.IEForgeEnergyWrapper(this, null);

    @Override
    public EnergyHelper.IEForgeEnergyWrapper getCapabilityWrapper(EnumFacing facing) {
        if (this.formed && this.isEnergyPos())
            return wrapper;
        return null;
    }

    @Override
    public void postEnergyTransferUpdate(int energy, boolean simulate) {
        if (!simulate)
            this.updateMasterBlock(null, energy != 0);
    }
}