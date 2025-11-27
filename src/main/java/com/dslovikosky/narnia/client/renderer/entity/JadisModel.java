package com.dslovikosky.narnia.client.renderer.entity;

import com.dslovikosky.narnia.common.constants.Constants;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
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

public class JadisModel extends EntityModel<JadisRenderState> {
    public static final AnimationHolder SIT = getAnimation(Constants.modLocation("jadis/sit"));
    private static final AnimationHolder WALK = getAnimation(Constants.modLocation("jadis/walk"));
    private final KeyframeAnimation sit;
    private final KeyframeAnimation walk;

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public JadisModel(final ModelPart root, final Function<ResourceLocation, RenderType> renderType) {
        super(root, renderType);
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.left_arm = this.body.getChild("left_arm");
        this.right_arm = this.body.getChild("right_arm");
        this.left_leg = this.body.getChild("left_leg");
        this.right_leg = this.body.getChild("right_leg");
        sit = SIT.get().bake(root);
        walk = WALK.get().bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -7.0F, -2.0F, 8.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -23.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -26.0F, 0.0F));

        PartDefinition crown = head.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(32, 0).addBox(4.0F, 0.6176F, -4.7353F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(40, 12).addBox(-5.0F, 0.6176F, -4.7353F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(40, 24).addBox(-4.0F, 0.6176F, 4.2647F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 27).addBox(-4.0F, 0.6176F, -4.7353F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 30).addBox(-2.5F, -0.3824F, -4.7353F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 12).addBox(-1.5F, -1.3824F, -4.7353F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 14).addBox(-1.0F, -2.3824F, -4.7353F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 35).addBox(-5.0F, -0.3824F, -4.7353F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 49).addBox(-5.0F, -1.3824F, -0.2353F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 32).addBox(-5.0F, -0.3824F, -0.7353F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 38).addBox(-5.0F, -0.3824F, 4.2647F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 47).addBox(-0.5F, -1.3824F, 4.2647F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 34).addBox(-1.0F, -0.3824F, 4.2647F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 41).addBox(4.0F, -0.3824F, 4.2647F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 32).addBox(4.0F, -0.3824F, -0.7353F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 51).addBox(4.0F, -1.3824F, -0.2353F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 44).addBox(4.0F, -0.3824F, -4.7353F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.6176F, -0.2647F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 36).addBox(0.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -30.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 36).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -30.0F, 0.0F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -16.0F, 0.0F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(final JadisRenderState renderState) {
        if (renderState.ageInTicks % 100 >= 50) {
            if (((long) renderState.ageInTicks) % 100 == 50) {
                super.setupAnim(renderState);
                sit.apply(0, 1f);
            }
            return;
        }
        super.setupAnim(renderState);
        walk.apply(System.currentTimeMillis() % 10000, 1f);
    }
}
