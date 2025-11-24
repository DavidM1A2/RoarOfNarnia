package com.dslovikosky.narnia.client.renderer.entity;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WorldWoodBoatRenderer extends BoatRenderer {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.modLocation("boat/world_wood"), "main");

    public WorldWoodBoatRenderer(final EntityRendererProvider.Context context) {
        super(context, LAYER_LOCATION);
    }
}
