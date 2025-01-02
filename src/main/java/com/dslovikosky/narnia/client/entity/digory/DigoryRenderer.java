package com.dslovikosky.narnia.client.entity.digory;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.digory.DigoryEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DigoryRenderer extends LivingEntityRenderer<DigoryEntity, DigoryModel> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/digory.png");

    public DigoryRenderer(final EntityRendererProvider.Context context) {
        super(context, new DigoryModel(context.bakeLayer(DigoryModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(DigoryEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
