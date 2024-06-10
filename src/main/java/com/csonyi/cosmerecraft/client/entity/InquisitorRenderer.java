package com.csonyi.cosmerecraft.client.entity;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.entity.Inquisitor;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class InquisitorRenderer extends HumanoidMobRenderer<Inquisitor, InquisitorModel<Inquisitor>> {

  private static final ResourceLocation TEXTURE = new ResourceLocation(CosmereCraft.MOD_ID, "textures/entity/inquisitor.png");

  public InquisitorRenderer(EntityRendererProvider.Context renderContext) {
    super(
        renderContext,
        new InquisitorModel<>(renderContext.bakeLayer(InquisitorModel.LAYER_LOCATION)),
        1.0F);
  }

  @Override
  public ResourceLocation getTextureLocation(Inquisitor entity) {
    return TEXTURE;
  }
}
