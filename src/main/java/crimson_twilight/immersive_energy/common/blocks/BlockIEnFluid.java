package crimson_twilight.immersive_energy.common.blocks;

import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.IEnContent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * @author Pabilo8
 * @since 02.12.2020
 */
public class BlockIEnFluid extends BlockFluidClassic {
    private int flammability = 0;
    private int fireSpread = 0;
    private PotionEffect[] potionEffects;

    public BlockIEnFluid(String name, Fluid fluid, Material material) {
        super(fluid, material);
        this.setUnlocalizedName(ImmersiveEnergy.MODID + "." + name);
        this.setCreativeTab(ImmersiveEnergy.creativeTab);
        IEnContent.registeredIEnBlocks.add(this);
    }

    public BlockIEnFluid setFlammability(int flammability, int fireSpread) {
        this.flammability = flammability;
        this.fireSpread = fireSpread;
        return this;
    }

    public BlockIEnFluid setPotionEffects(PotionEffect... potionEffects) {
        this.potionEffects = potionEffects;
        return this;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return this.flammability;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return fireSpread;
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return this.flammability > 0;
    }


    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (potionEffects != null && entity instanceof EntityLivingBase) {
            for (PotionEffect effect : potionEffects)
                if (effect != null)
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(effect));
        }
    }
}
