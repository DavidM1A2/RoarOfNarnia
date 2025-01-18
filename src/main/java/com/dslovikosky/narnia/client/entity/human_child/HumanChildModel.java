package com.dslovikosky.narnia.client.entity.human_child;
// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.dslovikosky.narnia.common.constants.Constants;
import com.dslovikosky.narnia.common.entity.human_child.HumanChildEntity;
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

public class HumanChildModel<T extends HumanChildEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "human_child"), "main");
    private final ModelPart body;
    private final ModelPart leg2;
    private final ModelPart leg1;
    private final ModelPart arm1;
    private final ModelPart arm2;
    private final ModelPart head;

    public HumanChildModel(final ModelPart root) {
        this.body = root.getChild("body");
        this.leg2 = this.body.getChild("leg2");
        this.leg1 = this.body.getChild("leg1");
        this.arm1 = this.body.getChild("arm1");
        this.arm2 = this.body.getChild("arm2");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -2.0F, 0.0F));

        PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(6, 4).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -2.0F, 0.0F));

        PartDefinition arm1 = body.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(4, 7).addBox(0.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -4.0F, 0.0F));

        PartDefinition arm2 = body.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -4.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.head.yRot = pNetHeadYaw * (float) (Math.PI / 180f);
        this.head.xRot = pHeadPitch * (float) (Math.PI / 180f);

        this.animateWalk(HumanChildAnimations.WALK, pLimbSwing, pLimbSwingAmount, 2f, 2.5f);
        this.animate(pEntity.getIdleAnimationState(), HumanChildAnimations.IDLE, pAgeInTicks, 1f);
        this.animate(pEntity.getTalkAnimationState(), HumanChildAnimations.TALK, pAgeInTicks, 1f);
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