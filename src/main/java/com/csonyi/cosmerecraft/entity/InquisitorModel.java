package com.csonyi.cosmerecraft.entity;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.monster.Monster;

public class InquisitorModel<T extends Monster> extends PlayerModel<T> {

  // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(ResourceUtils.modResourceLocation("inquisitor"), "main");

  public InquisitorModel(ModelPart root) {
    super(root, false);
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
    partdefinition.addOrReplaceChild("ear", CubeListBuilder.create(), PartPose.ZERO);

    PartDefinition head = partdefinition.addOrReplaceChild("head",
        CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1047F, 0.0873F, 0.0F));

    PartDefinition head_left_spike = head.addOrReplaceChild("head_left_spike",
        CubeListBuilder.create().texOffs(0, 64).addBox(1.5F, -4.5F, -4.5F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -5.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition head_right_spike = head.addOrReplaceChild("head_right_spike",
        CubeListBuilder.create().texOffs(0, 64).addBox(-2.5F, -4.5F, -4.5F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(-3.0F, -5.0F, -5.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition body = partdefinition.addOrReplaceChild("body",
        CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition body_top_right_spike = body.addOrReplaceChild("body_top_right_spike",
        CubeListBuilder.create().texOffs(3, 67).addBox(3.5F, -7.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(3.0F, -8.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 1.0F));

    PartDefinition body_top_left_spike = body.addOrReplaceChild("body_top_left_spike",
        CubeListBuilder.create().texOffs(3, 67).addBox(1.5F, -4.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 6.0F, 1.0F));

    PartDefinition body_top_center_spike = body.addOrReplaceChild("body_top_center_spike",
        CubeListBuilder.create().texOffs(3, 67).addBox(1.5F, -4.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 1.0F));

    PartDefinition body_bottom_center_spike = body.addOrReplaceChild("body_bottom_center_spike",
        CubeListBuilder.create().texOffs(3, 67).addBox(1.5F, -4.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 14.0F, 1.0F));

    PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm",
        CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

    PartDefinition right_arm_bottom_spike = right_arm.addOrReplaceChild("right_arm_bottom_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 6.0F, 0.0F));

    PartDefinition right_arm_top_spike = right_arm.addOrReplaceChild("right_arm_top_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 2.0F, 0.0F));

    PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm",
        CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.2094F, 0.0F, 0.0F));

    PartDefinition left_arm_top_spike = left_arm.addOrReplaceChild("left_arm_top_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 2.0F, 0.0F));

    PartDefinition left_arm_bottom_spike = left_arm.addOrReplaceChild("left_arm_bottom_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.0F, 0.0F));

    PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg",
        CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-1.9F, 12.0F, 0.0F, 0.192F, 0.0F, 0.0349F));

    PartDefinition right_leg_top_spike = right_leg.addOrReplaceChild("right_leg_top_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1F, 4.0F, 0.0F));

    PartDefinition right_leg_bottom_spike = right_leg.addOrReplaceChild("right_leg_bottom_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1F, 7.0F, 0.0F));

    PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg",
        CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(1.9F, 12.0F, 0.0F, -0.1745F, 0.0F, -0.0349F));

    PartDefinition left_leg_top_spike = left_leg.addOrReplaceChild("left_leg_top_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 4.0F, 0.0F));

    PartDefinition left_leg_bottom_spike = left_leg.addOrReplaceChild("left_leg_bottom_spike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 7.0F, 0.0F));

    PartDefinition right_sleeve = partdefinition.addOrReplaceChild("right_sleeve",
        CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offset(-5.0F, 2.0F, 0.0F));

    PartDefinition jacket = partdefinition.addOrReplaceChild("jacket",
        CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition left_sleeve = partdefinition.addOrReplaceChild("left_sleeve",
        CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offset(5.0F, 2.0F, 0.0F));

    PartDefinition right_pants = partdefinition.addOrReplaceChild("right_pants",
        CubeListBuilder.create().texOffs(0, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offset(-2.0F, 12.0F, 0.0F));

    PartDefinition left_pants = partdefinition.addOrReplaceChild("left_pants",
        CubeListBuilder.create().texOffs(0, 48).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offset(2.0F, 12.0F, 0.0F));

    PartDefinition cloak = partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.0F));

    PartDefinition cloak_middle_r1 = cloak.addOrReplaceChild("cloak_middle_r1",
        CubeListBuilder.create().texOffs(22, 68).addBox(-7.0F, -1.0F, -3.0F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, 2.8136F, 4.8484F, 0.2182F, 0.0F, 0.0F));

    PartDefinition cloak_r1 = cloak.addOrReplaceChild("cloak_r1",
        CubeListBuilder.create().texOffs(20, 68).addBox(-8.0F, 0.5F, 0.0F, 16.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.48F, 0.0F, 0.0F));

    PartDefinition cloak_bottom = cloak.addOrReplaceChild("cloak_bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -2.0F));

    PartDefinition cloak_bottom_right_r1 = cloak_bottom.addOrReplaceChild("cloak_bottom_right_r1",
        CubeListBuilder.create().texOffs(36, 68).addBox(-7.0F, -0.0742F, 0.3223F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(36, 68).addBox(3.0F, -0.0742F, 0.3223F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(36, 68).addBox(-2.0F, -0.0742F, 0.3223F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, -9.6556F, 6.0017F, 0.0436F, 0.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 80);
  }

  @Override
  public void renderToBuffer(
      PoseStack poseStack, VertexConsumer vertexConsumer,
      int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    renderCloak(poseStack, vertexConsumer, packedLight, packedOverlay);
  }
}
