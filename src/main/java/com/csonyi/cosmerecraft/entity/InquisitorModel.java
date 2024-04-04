package com.csonyi.cosmerecraft.entity;

// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.csonyi.cosmerecraft.CosmereCraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class InquisitorModel<T extends Entity> extends EntityModel<T> {

  // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
      new ResourceLocation(CosmereCraft.MOD_ID, "inquisitor"), "main");
  private final ModelPart Head;
  private final ModelPart Body;
  private final ModelPart RightArm;
  private final ModelPart LeftArm;
  private final ModelPart RightLeg;
  private final ModelPart LeftLeg;

  public InquisitorModel(ModelPart root) {
    this.Head = root.getChild("Head");
    this.Body = root.getChild("Body");
    this.RightArm = root.getChild("RightArm");
    this.LeftArm = root.getChild("LeftArm");
    this.RightLeg = root.getChild("RightLeg");
    this.LeftLeg = root.getChild("LeftLeg");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition Head = partdefinition.addOrReplaceChild("Head",
        CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
            .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
        PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1047F, 0.0873F, 0.0F));

    PartDefinition HeadLeftSpike = Head.addOrReplaceChild("HeadLeftSpike",
        CubeListBuilder.create().texOffs(0, 64).addBox(1.5F, -4.5F, -4.5F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -5.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition HeadRightSpike = Head.addOrReplaceChild("HeadRightSpike",
        CubeListBuilder.create().texOffs(0, 64).addBox(-2.5F, -4.5F, -4.5F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(-3.0F, -5.0F, -5.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition Body = partdefinition.addOrReplaceChild("Body",
        CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition Cape = Body.addOrReplaceChild("Cape", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

    PartDefinition CapeMiddle_r1 = Cape.addOrReplaceChild("CapeMiddle_r1",
        CubeListBuilder.create().texOffs(22, 68).addBox(-7.0F, -1.0F, -3.0F, 14.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, -21.1864F, 6.8484F, 0.2182F, 0.0F, 0.0F));

    PartDefinition CapeTop_r1 = Cape.addOrReplaceChild("CapeTop_r1",
        CubeListBuilder.create().texOffs(20, 68).addBox(-8.0F, 0.5F, 0.0F, 16.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, -24.5F, 2.0F, 0.48F, 0.0F, 0.0F));

    PartDefinition CapeBottom = Cape.addOrReplaceChild("CapeBottom", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

    PartDefinition CapeBottomRight_r1 = CapeBottom.addOrReplaceChild("CapeBottomRight_r1",
        CubeListBuilder.create().texOffs(36, 68).addBox(-7.0F, -0.0742F, 0.3223F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(36, 68).addBox(3.0F, -0.0742F, 0.3223F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(36, 68).addBox(-2.0F, -0.0742F, 0.3223F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.0F, -9.6556F, 6.0017F, 0.0436F, 0.0F, 0.0F));

    PartDefinition BodyTopRightSpike = Body.addOrReplaceChild("BodyTopRightSpike",
        CubeListBuilder.create().texOffs(3, 67).addBox(3.5F, -7.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(3.0F, -8.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 1.0F));

    PartDefinition BodyTopLeftSpike = Body.addOrReplaceChild("BodyTopLeftSpike",
        CubeListBuilder.create().texOffs(3, 67).addBox(1.5F, -4.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 6.0F, 1.0F));

    PartDefinition BodyTopCenterSpike = Body.addOrReplaceChild("BodyTopCenterSpike",
        CubeListBuilder.create().texOffs(3, 67).addBox(1.5F, -4.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 1.0F));

    PartDefinition BodyBottomCenterSpike = Body.addOrReplaceChild("BodyBottomCenterSpike",
        CubeListBuilder.create().texOffs(3, 67).addBox(1.5F, -4.5F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(1, 65).addBox(1.0F, -5.0F, -4.0F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 14.0F, 1.0F));

    PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm",
        CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

    PartDefinition RightArmBottomSpike = RightArm.addOrReplaceChild("RightArmBottomSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 6.0F, 0.0F));

    PartDefinition RightArmTopSpike = RightArm.addOrReplaceChild("RightArmTopSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 2.0F, 0.0F));

    PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm",
        CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.2094F, 0.0F, 0.0F));

    PartDefinition LeftArmTopSpike = LeftArm.addOrReplaceChild("LeftArmTopSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 2.0F, 0.0F));

    PartDefinition LeftArmBottomSpike = LeftArm.addOrReplaceChild("LeftArmBottomSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.0F, 0.0F));

    PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg",
        CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offsetAndRotation(-1.9F, 12.0F, 0.0F, 0.192F, 0.0F, 0.0349F));

    PartDefinition RightLegTopSpike = RightLeg.addOrReplaceChild("RightLegTopSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1F, 4.0F, 0.0F));

    PartDefinition RightLegBottomSpike = RightLeg.addOrReplaceChild("RightLegBottomSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(3.0F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(3.0F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1F, 7.0F, 0.0F));

    PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg",
        CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
        PartPose.offsetAndRotation(1.9F, 12.0F, 0.0F, -0.1745F, 0.0F, -0.0349F));

    PartDefinition LeftLegTopSpike = LeftLeg.addOrReplaceChild("LeftLegTopSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 4.0F, 0.0F));

    PartDefinition LeftLegBottomSpike = LeftLeg.addOrReplaceChild("LeftLegBottomSpike",
        CubeListBuilder.create().texOffs(1, 65).addBox(0.5F, -1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
            .texOffs(1, 63).addBox(2.5F, -2.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 7.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 80);
  }

  @Override
  public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green,
      float blue, float alpha) {
    Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
  }
}
