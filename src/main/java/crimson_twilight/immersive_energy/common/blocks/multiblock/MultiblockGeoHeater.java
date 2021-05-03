package crimson_twilight.immersive_energy.common.blocks.multiblock;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockGeoHeater implements IMultiblock
{
    static final IngredientStack[] materials = new IngredientStack[]{

    };
    public static MultiblockGeoHeater instance = new MultiblockGeoHeater();
    static ItemStack[][][] structure = new ItemStack[1][1][1]; //TODO add actual values

    static ItemStack renderStack = ItemStack.EMPTY;

    static
    {

    }

    @Override
    public ItemStack[][][] getStructureManual()
    {
        return structure;
    }

    @Override
    public IngredientStack[] getTotalMaterials() {
        return new IngredientStack[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean overwriteBlockRender(ItemStack stack, int iterator)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderFormedStructure()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderFormedStructure()
    {

    }

    @Override
    public float getManualScale()
    {
        return 12;
    }

    @Override
    public String getUniqueName()
    {
        return "IEn:GeoHeater";
    }

    @Override
    public boolean isBlockTrigger(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean createStructure(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
        return false;
    }
}
