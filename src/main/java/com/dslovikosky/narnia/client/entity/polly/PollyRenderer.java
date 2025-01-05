package com.dslovikosky.narnia.client.entity.polly;

import com.dslovikosky.narnia.client.entity.human_child.HumanChildRenderer;
import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.human_child.HumanChildEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PollyRenderer extends HumanChildRenderer<HumanChildEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/polly.png");

    public PollyRenderer(final EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(final HumanChildEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
