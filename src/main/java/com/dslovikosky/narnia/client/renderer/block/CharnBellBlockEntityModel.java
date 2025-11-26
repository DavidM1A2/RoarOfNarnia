package com.dslovikosky.narnia.client.renderer.block;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.dslovikosky.narnia.client.renderer.block.base.BlockBenchModel;
import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.entity.animation.json.AnimationHolder;

import java.util.function.Function;

public class CharnBellBlockEntityModel extends BlockBenchModel<CharnBellBlockEntityRenderState> {
    private static final AnimationHolder RING_NORTH = getAnimation(Constants.modLocation("charn_bell/ring_north"));
    private static final AnimationHolder RING_SOUTH = getAnimation(Constants.modLocation("charn_bell/ring_south"));
    private final KeyframeAnimation ringNorth;
    private final KeyframeAnimation ringSouth;

    public CharnBellBlockEntityModel(final Function<ResourceLocation, RenderType> renderType) {
        super(getLayerDefinition(), renderType);
        ringNorth = RING_NORTH.get().bake(root);
        ringSouth = RING_SOUTH.get().bake(root);
    }

    private static LayerDefinition getLayerDefinition() {
        final MeshDefinition meshDefinition = new MeshDefinition();
        final PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition base = partDefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition hammer = base.addOrReplaceChild("hammer", CubeListBuilder.create().texOffs(18, 25).addBox(-0.5F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-2.5F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9021F, -1.5F, 2.6769F, 0.0F, -0.7418F, 0.0F));

        PartDefinition stand = base.addOrReplaceChild("stand", CubeListBuilder.create().texOffs(18, 27).addBox(4.5F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 22).addBox(5.0F, -9.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 17).addBox(4.0F, -12.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(3.0F, -14.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 30).addBox(2.0F, -15.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 27).addBox(-6.5F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(14, 23).addBox(-6.0F, -9.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 22).addBox(-5.0F, -12.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 29).addBox(-4.0F, -14.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 30).addBox(-3.0F, -15.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 23).addBox(-2.0F, -15.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 30).addBox(-1.0F, -16.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bell = stand.addOrReplaceChild("bell", CubeListBuilder.create().texOffs(8, 31).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-1.0F, 2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 17).addBox(-1.5F, 3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-2.0F, 6.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 32).addBox(-0.5F, 7.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(final CharnBellBlockEntityRenderState renderState) {
        super.setupAnim(renderState);
        final float ticksIntoAnimation = renderState.gameTime - renderState.lastHitTime + renderState.partialTick;
        final long msIntoAnimation = Math.round(ticksIntoAnimation / 20 * 1000);
        (renderState.hitNorth ? ringNorth : ringSouth).apply(msIntoAnimation, 1f);
    }
}