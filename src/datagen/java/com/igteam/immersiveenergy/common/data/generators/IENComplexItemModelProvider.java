package com.igteam.immersiveenergy.common.data.generators;

import com.igteam.immersiveenergy.common.data.TRSRModelBuilder;
import com.igteam.immersiveenergy.core.lib.IENLib;
import com.igteam.immersiveenergy.core.registration.IENMultiblockProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.loaders.ObjModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class IENComplexItemModelProvider extends ModelProvider<TRSRModelBuilder>
{
    public IENComplexItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, IENLib.MODID, ITEM_FOLDER, TRSRModelBuilder::new, existingFileHelper);
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName();
    }

    static final ResourceLocation ITEM_GENERATED = new ResourceLocation("minecraft", "item/generated");

    @Override
    protected void registerModels()
    {
        generateMultiblockModel("burner", IENMultiblockProvider.BURNER.block());
    }

    private void doTransform(ModelBuilder<?>.TransformsBuilder transform, ItemDisplayContext type, @Nullable Vector3f translation, @Nullable Vector3f rotationAngle, float scale){
        ModelBuilder<?>.TransformsBuilder.TransformVecBuilder trans = transform.transform(type);
        if(translation != null)
            trans.translation(translation.x(), translation.y(), translation.z());
        if(rotationAngle != null)
            trans.rotation(rotationAngle.x(), rotationAngle.y(), rotationAngle.z());
        trans.scale(scale);
        trans.end();
    }

    private TRSRModelBuilder obj(Supplier<? extends ItemLike> item, String model){
        return obj(item.get(), model);
    }

    private TRSRModelBuilder obj(ItemLike item, String model){
        return getBuilder(name(item))
                .customLoader(ObjModelBuilder::begin)
                .modelLocation(modLoc("models/" + model)).flipV(true).end();
    }

    private String name(ItemLike item){
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }

    private void generateMultiblockModel(String id, Supplier<? extends ItemLike> block)
    {
        TRSRModelBuilder model = obj(block, "block/multiblock/obj/"+ id + "/" + id + ".obj");

        ModelBuilder<?>.TransformsBuilder trans = model.transforms();
        doTransform(trans, ItemDisplayContext.FIRST_PERSON_LEFT_HAND, new Vector3f(-1.75F, 2.5F, 1.25F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, new Vector3f(-1.75F, 2.5F, 1.75F), new Vector3f(0, 225, 0), 0.03125F);
        doTransform(trans, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, new Vector3f(-0.75F, 0, -1.25F), new Vector3f(0, 90, 0), 0.03125F);
        doTransform(trans, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, new Vector3f(1.0F, 0, -1.75F), new Vector3f(0, 270, 0), 0.03125F);
        doTransform(trans, ItemDisplayContext.HEAD, new Vector3f(0, 8, -8), null, 0.2F);
        doTransform(trans, ItemDisplayContext.GUI, new Vector3f(6, -6, 0), new Vector3f(30, 225, 0), 0.1875F);
        doTransform(trans, ItemDisplayContext.GROUND, new Vector3f(-1.5F, 3, -1.5F), null, 0.0625F);
        doTransform(trans, ItemDisplayContext.FIXED, new Vector3f(-1, -8, -2), null, 0.0625F);
    }
}
