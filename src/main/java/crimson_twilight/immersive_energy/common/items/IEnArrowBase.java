package crimson_twilight.immersive_energy.common.items;

import java.util.List;

import javax.annotation.Nullable;

import crimson_twilight.immersive_energy.ImmersiveEnergy;
import crimson_twilight.immersive_energy.common.IEnContent;
import crimson_twilight.immersive_energy.common.entities.EntityIEnArrow;
import crimson_twilight.immersive_energy.common.util.IArrowLogic;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
Thanks to Darkhax's Simply Arrows for the basis of this.
*/
public class IEnArrowBase extends ItemArrow
{
	private boolean infinity = true;
	private int damage = 2;
	private int knockback = 0;
	private boolean flaming = false;
	private boolean ignore_invulnerability = false;
	private ItemStack dropItem = ItemStack.EMPTY;
	private IArrowLogic logic;
    private String logic_desc = "";
    private String logic_extra = "";
    
	public IEnArrowBase(String name, String... logic_desc) 
	{
		this.setUnlocalizedName(ImmersiveEnergy.MODID+"."+name);
		this.setCreativeTab(ImmersiveEnergy.creativeTab);
		this.setMaxStackSize(64);
		this.logic_desc = logic_desc[0];
		this.logic_extra = logic_desc[1];
		this.dropItem = new ItemStack(this);
		IEnContent.registeredIEnItems.add(this);
	}
    
	public IEnArrowBase(String name, int stackSize, String... logic_desc) 
	{
		this.setUnlocalizedName(ImmersiveEnergy.MODID+"."+name);
		this.setCreativeTab(ImmersiveEnergy.creativeTab);
		this.setMaxStackSize(stackSize);
		this.logic_desc = logic_desc[0];
		this.logic_extra = logic_desc[1];
		this.dropItem = new ItemStack(this);
		IEnContent.registeredIEnItems.add(this);
	}
    
	public IEnArrowBase(String name, int stackSize) 
	{
		this.setUnlocalizedName(ImmersiveEnergy.MODID+"."+name);
		this.setCreativeTab(ImmersiveEnergy.creativeTab);
		this.setMaxStackSize(stackSize);
		this.dropItem = new ItemStack(this);
		IEnContent.registeredIEnItems.add(this);
	}
    
	public IEnArrowBase(String name) 
	{
		this.setUnlocalizedName(ImmersiveEnergy.MODID+"."+name);
		this.setCreativeTab(ImmersiveEnergy.creativeTab);
		this.setMaxStackSize(64);
		this.dropItem = new ItemStack(this);
		IEnContent.registeredIEnItems.add(this);
	}

    @Override
    public EntityArrow createArrow(World world, ItemStack stack, EntityLivingBase shooter) 
    {
        final EntityIEnArrow arrow = new EntityIEnArrow(world, this.dropItem.copy(), shooter).setIgnoreInvulnerability(this.ignore_invulnerability);
        arrow.setLogic(this.logic);
        arrow.setDamage(this.damage);
        arrow.setKnockbackStrength(this.knockback);
        if(this.flaming) 
        {
            arrow.setFire(100);
        }
        return arrow;
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) 
    {
        final int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
        return enchant < 1 ? false : this.infinity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
    {
        if (this.damage > 0) 
        {
        	tooltip.add(TextFormatting.BLUE + Integer.toString(this.getArrowDamage()) + " " + I18n.format("attribute.name.immersive_energy.damage"));
        }
        if(this.logic != null)
        {
        	if(this.logic_desc != "")
        	{
        		tooltip.add(TextFormatting.BLUE + String.valueOf(this.logic_extra) + " " + I18n.format("attribute.name.immersive_energy."+this.logic_desc));
        	}
        }
        if (this.knockback > 0) 
        {
            tooltip.add(TextFormatting.BLUE + String.valueOf(this.getKnockback()) + " " + I18n.format("attribute.name.immersive_energy.knockback"));
        }
    }

    public boolean isInfinity() 
    {
        return this.infinity;
    }

    public IEnArrowBase setInfinity(boolean infinity) 
    {
        this.infinity = infinity;
        return this;
    }

    public int getArrowDamage() 
    {
        return this.damage;
    }

    public IEnArrowBase setDamage(int damage) 
    {
        this.damage = damage;
        return this;
    }

    public int getKnockback() 
    {
        return this.knockback;
    }

    public IEnArrowBase setKnockback(int knockback) 
    {
        this.knockback = knockback;
        return this;
    }

    public boolean isFlaming() 
    {
        return this.flaming;
    }

    public IEnArrowBase setFlaming(boolean flaming) 
    {
        this.flaming = flaming;
        return this;
    }

    public ItemStack getDropItem() 
    {
        return this.dropItem;
    }

    public IEnArrowBase setDropItem(ItemStack dropItem) 
    {
        this.dropItem = dropItem;
        return this;
    }

    public IArrowLogic getLogic() 
    {
        return this.logic;
    }

    public IEnArrowBase setLogic(IArrowLogic logic) 
    {
        this.logic = logic;
        return this;
    }

    public boolean getIgnoreInvulnerability() 
    {
        return this.ignore_invulnerability;
    }

    public IEnArrowBase setIgnoreInvulnerability(boolean ignore) 
    {
        this.ignore_invulnerability = ignore;
        return this; 
    }
	
}
