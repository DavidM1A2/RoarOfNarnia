package com.dslovikosky.narnia.client.entity.lion;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.lion.LionEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LionRenderer extends LivingEntityRenderer<LionEntity, LionModel<LionEntity>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/lion.png");

    public LionRenderer(final EntityRendererProvider.Context context) {
        super(context, new LionModel<LionEntity>(context.bakeLayer(LionModel.LAYER_LOCATION)), 1.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(final LionEntity pEntity) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected boolean shouldShowName(LionEntity entity) {
        return false;
    }
}
