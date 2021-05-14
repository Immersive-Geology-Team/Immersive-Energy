package crimson_twilight.immersive_energy.client.model;

import crimson_twilight.immersive_energy.ImmersiveEnergy;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
public class PowerArmorModel extends ModelBiped {

    @SideOnly(Side.CLIENT)
    public static PowerArmorModel powerArmorModel = new PowerArmorModel(0, 0);

    //List of parts for group flipping / translation / rotation
    private ItemStack renderStack = ItemStack.EMPTY;
    private EntityEquipmentSlot renderSlot = EntityEquipmentSlot.HEAD;

    public PowerArmorModel(int textureWidthIn, int textureHeightIn) {
        //yOffset
        super(1, 0, textureWidthIn, textureHeightIn);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        GlStateManager.pushMatrix();
        actualRender(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();

    }

    public void renderAsPart() {
        if (renderSlot == EntityEquipmentSlot.HEAD) {

        } else if (renderSlot == EntityEquipmentSlot.CHEST) {
            GlStateManager.translate(0, 0.5f, 0);
            GlStateManager.scale(0.85f, 0.85f, 0.85f);

        } else if (renderSlot == EntityEquipmentSlot.LEGS) {
            GlStateManager.translate(0.25f - 0.0625, 0.75, 0);
            //leftLegModel
            GlStateManager.translate(-0.25f, 0, 0);
            //rightLegModel
        } else if (renderSlot == EntityEquipmentSlot.FEET) {
            GlStateManager.translate(0.25f - 0.0625, 0.75, 0);
            //leftFootModel
            GlStateManager.translate(-0.25f, 0, 0);
            GlStateManager.rotate(-35, 0, 1, 0);
            //rightFootModel
        }
    }

    protected void actualRender(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        GlStateManager.pushMatrix();

        if (entity.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
        if (renderSlot == EntityEquipmentSlot.HEAD) {
            //renderChild(this.bipedHead, headModel, scale);
        } else if (renderSlot == EntityEquipmentSlot.CHEST) {
            //renderChild(this.bipedBody, bodyModel, scale);
            //renderChild(this.bipedLeftArm, EvenMoreImmersiveModelRegistry.instance.armLeftModel, scale);
            //renderChild(this.bipedRightArm, rightArmModel, scale);
            //renderAddons(renderStack, renderSlot, scale, ageInTicks);
        } else if (renderSlot == EntityEquipmentSlot.LEGS) {
            //renderChild(this.bipedLeftLeg, leftLegModel, scale);
            //renderChild(this.bipedRightLeg, rightLegModel, scale);
        } else if (renderSlot == EntityEquipmentSlot.FEET) {
            //renderChild(this.bipedLeftLeg, leftFootModel, scale);
            //renderChild(this.bipedRightLeg, rightFootModel, scale);
        }

        GlStateManager.popMatrix();
    }

    public void renderAddons(ItemStack renderStack, EntityEquipmentSlot renderSlot, float scale, float ageInTicks) {

    }

    public void renderChild(ModelRenderer biped, IBakedModel model, float scale) {
        if (!biped.isHidden && biped.showModel) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(biped.rotationPointX * scale, biped.rotationPointY * scale, biped.rotationPointZ * scale);

            if (biped.rotateAngleY != 0.0F) {
                GlStateManager.rotate(biped.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            }

            if (biped.rotateAngleX != 0.0F) {
                GlStateManager.rotate(biped.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
            }

            if (biped.rotateAngleZ != 0.0F) {
                GlStateManager.rotate(biped.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
            }

            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.rotate(180, 0, 1, 0);
            GlStateManager.scale(1.06, 1.06, 1.06);

            List<BakedQuad> quads = model.getQuads(null, null, 1);
            //ImmersiveEnergy.logger.info(quads);
            if (!quads.isEmpty()) {

            }

            GlStateManager.popMatrix();
        }
    }

    public PowerArmorModel prepareForRender(EntityEquipmentSlot part, ItemStack stack) {
        this.renderStack = stack;
        this.renderSlot = part;
        return this;
    }
}
