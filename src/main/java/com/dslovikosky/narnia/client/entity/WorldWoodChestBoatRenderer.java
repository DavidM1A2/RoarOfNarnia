package com.dslovikosky.narnia.client.entity;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WorldWoodChestBoatRenderer extends BoatRenderer {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.modLocation("chest_boat/world_wood"), "main");

    public WorldWoodChestBoatRenderer(final EntityRendererProvider.Context context) {
        super(context, LAYER_LOCATION);
    }
}
