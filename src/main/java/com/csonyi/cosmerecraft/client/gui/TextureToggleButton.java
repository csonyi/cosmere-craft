package com.csonyi.cosmerecraft.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TextureToggleButton extends ToggleButton {

  protected final ResourceLocation background;
  protected final ResourceLocation onTexture;
  protected final ResourceLocation offTexture;
  protected final int textureWidth;
  protected final int textureHeight;

  public TextureToggleButton(
      int x, int y,
      int width, int height,
      OnToggle onPress,
      ResourceLocation background,
      ResourceLocation onTexture,
      ResourceLocation offTexture) {
    super(x, y, width, height, onPress, Component.empty());
    this.background = background;
    this.onTexture = onTexture;
    this.offTexture = offTexture;
    this.textureWidth = textureWidth();
    this.textureHeight = getTextureHeight();
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(guiGraphics);
    if (state) {
      renderOnTexture(guiGraphics);
    } else {
      renderOffTexture(guiGraphics);
    }

  }

  private void renderBackground(GuiGraphics guiGraphics) {
    guiGraphics.blit(
        background,
        getX(), getY(),
        width, height,
        0.0f, 0.0f,
        width, height,
        width, height);
  }

  private void renderOnTexture(GuiGraphics guiGraphics) {
    guiGraphics.blit(
        onTexture,
        getX() + (width - textureWidth) / 2,
        getY() + (height - textureHeight) / 2,
        textureWidth, textureHeight,
        0.0f, 0.0f,
        textureWidth, textureHeight,
        textureWidth, textureHeight);
  }

  private void renderOffTexture(GuiGraphics guiGraphics) {
    guiGraphics.blit(
        offTexture,
        getX() + (width - textureWidth) / 2,
        getY() + (height - textureHeight) / 2,
        textureWidth, textureHeight,
        0.0f, 0.0f,
        textureWidth, textureHeight,
        textureWidth, textureHeight);
  }

  private int getTextureHeight() {
    return Mth.floor(height * 0.7);
  }

  private int textureWidth() {
    return Mth.floor(width * 0.7);
  }
}
