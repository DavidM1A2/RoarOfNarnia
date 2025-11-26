package com.dslovikosky.narnia.client.renderer.entity;

import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.JadisEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class JadisRenderer extends MobRenderer<JadisEntity, JadisRenderState, JadisModel> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.modLocation("jadis"), "main");
    private static final ResourceLocation TEXTURE = Constants.modLocation("textures/entity/jadis.png");

    public JadisRenderer(EntityRendererProvider.Context context) {
        super(context, new JadisModel(context.bakeLayer(LAYER_LOCATION), RenderType::entityCutout), 0.5f);
    }

    @Override
    public JadisRenderState createRenderState() {
        return new JadisRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(final JadisRenderState renderState) {
        return TEXTURE;
    }
}
