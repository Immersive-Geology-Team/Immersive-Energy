package crimson_twilight.immersive_energy.common.blocks.multiblock;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockGeoHeater implements IMultiblock
{
    static final IngredientStack[] materials = new IngredientStack[]{

    };
    public static MultiblockGeoHeater instance = new MultiblockGeoHeater();
    static ItemStack[][][] structure = new ItemStack[][][];

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
        return
    }
}
