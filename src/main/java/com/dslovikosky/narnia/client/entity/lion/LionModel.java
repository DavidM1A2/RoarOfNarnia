package com.dslovikosky.narnia.client.entity.lion;
// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.lion.LionEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class LionModel<T extends LionEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lion"), "main");
    private final ModelPart body;
    private final ModelPart mouth;
    private final ModelPart head;
    private final ModelPart fr_leg;
    private final ModelPart bl_leg;
    private final ModelPart tail;
    private final ModelPart br_leg;
    private final ModelPart fl_leg;

    public LionModel(ModelPart root) {
        this.body = root.getChild("body");
        this.mouth = this.body.getChild("mouth");
        this.head = this.body.getChild("head");
        this.fr_leg = this.body.getChild("fr_leg");
        this.bl_leg = this.body.getChild("bl_leg");
        this.tail = this.body.getChild("tail");
        this.br_leg = this.body.getChild("br_leg");
        this.fl_leg = this.body.getChild("fl_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.5F, -3.0F, 3.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition mouth = body.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offset(-1.0F, -5.0F, -6.0F));

        PartDefinition jaw_r1 = mouth.addOrReplaceChild("jaw_r1", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, -0.6F, -1.8F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(16, 14).addBox(-1.5F, -8.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mane_r1 = head.addOrReplaceChild("mane_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -5.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition muzzle_r1 = head.addOrReplaceChild("muzzle_r1", CubeListBuilder.create().texOffs(16, 20).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -7.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition fr_leg = body.addOrReplaceChild("fr_leg", CubeListBuilder.create().texOffs(12, 23).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -5.5F, -1.5F));

        PartDefinition bl_leg = body.addOrReplaceChild("bl_leg", CubeListBuilder.create().texOffs(4, 23).addBox(0.0F, -0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.5F, 6.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-1.0F, -8.0F, 7.0F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(16, 0).addBox(0.5F, 0.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition br_leg = body.addOrReplaceChild("br_leg", CubeListBuilder.create().texOffs(8, 23).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -5.5F, 6.5F));

        PartDefinition fl_leg = body.addOrReplaceChild("fl_leg", CubeListBuilder.create().texOffs(0, 23).addBox(0.0F, -0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.5F, -1.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(LionAnimations.WALK, pLimbSwing, pLimbSwingAmount, 2f, 2.5f);
        this.animate(pEntity.getTalkAnimationState(), LionAnimations.TALK, pAgeInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.translate(0, -3, 0);
        poseStack.scale(3F, 3F, 3F);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return body;
    }
}