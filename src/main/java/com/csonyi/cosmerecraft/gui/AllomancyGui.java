package com.csonyi.cosmerecraft.gui;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.networking.MetalStateUpdateHandler;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.Arrays;
import java.util.Set;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AllomancyGui extends Screen {

  private static final ResourceLocation BACKGROUND = ResourceUtils.modLocation("textures/gui/allomancy_gui_bg.png");

  private int scaledWidth;
  private int scaledHeight;
  private EditorQuadrant[][] editorQuadrants;

  private MetalStateManager metalStateManager;

  public AllomancyGui() {
    super(Component.literal("Allomancy"));
  }

  @Override
  protected void init() {
    if (minecraft == null) {
      return;
    }
    scaledWidth = minecraft.getWindow().getGuiScaledWidth();
    scaledHeight = minecraft.getWindow().getGuiScaledHeight();
    MetalStateUpdateHandler.queryMetalStatesFromServer();
    metalStateManager = new MetalStateManager(minecraft.player);

    renderBackground();
    addEditorQuadrants();
  }

  private void renderBackground() {
    var background = ImageWidget.texture(
        scaledWidth - 2 * xMargin(), scaledHeight - 2 * yMargin(),
        ResourceUtils.modLocation("textures/gui/allomancy_gui_bg.png"),
        scaledWidth - 2 * xMargin(), scaledHeight - 2 * yMargin());
    background.setPosition(xMargin(), yMargin());
    addRenderableOnly(background);
  }

  private void addEditorQuadrants() {
    var typeBoxWidth = Mth.floor(scaledWidth / 2.2 - xMargin());
    var typeBoxHeight = Mth.floor(scaledHeight / 2.5 - yMargin());
    editorQuadrants = new EditorQuadrant[][]{
        {
            new EditorQuadrant(
                xMargin() + xPadding(),
                yMargin() + yPadding(),
                typeBoxWidth, typeBoxHeight,
                font, AllomanticMetal.Type.PHYSICAL,
                metalStateManager),
            new EditorQuadrant(
                scaledWidth - xMargin() - xPadding() - typeBoxWidth,
                yMargin() + yPadding(),
                typeBoxWidth, typeBoxHeight,
                font, AllomanticMetal.Type.MENTAL,
                metalStateManager)},
        {
            new EditorQuadrant(
                xMargin() + xPadding(),
                scaledHeight - yMargin() - yPadding() - typeBoxHeight,
                typeBoxWidth, typeBoxHeight,
                font, AllomanticMetal.Type.ENHANCEMENT,
                metalStateManager),
            new EditorQuadrant(
                scaledWidth - xMargin() - xPadding() - typeBoxWidth,
                scaledHeight - yMargin() - yPadding() - typeBoxHeight,
                typeBoxWidth, typeBoxHeight,
                font, AllomanticMetal.Type.TEMPORAL,
                metalStateManager)}};
    Arrays.stream(editorQuadrants)
        .flatMap(Arrays::stream)
        .peek(this::addRenderableOnly)
        .flatMap(EditorQuadrant::streamChildren)
        .forEach(this::addRenderableWidget);
  }

  private void updateServer() {
    MetalStateUpdateHandler.pushBurnStrengthUpdatesToServer(
        Arrays.stream(editorQuadrants)
            .flatMap(Arrays::stream)
            .map(EditorQuadrant::getStates)
            .flatMap(Set::stream));
  }

  @Override
  public void tick() {
    MetalStateUpdateHandler.queryMetalStatesFromServer();
  }

  @Override
  public void onClose() {
    updateServer();
    super.onClose();
  }

  @Override
  protected boolean shouldNarrateNavigation() {
    return false;
  }

  private int xMargin() {
    return scaledWidth / 48;
  }

  private int yMargin() {
    return scaledHeight / 16;
  }

  private int xPadding() {
    return scaledWidth / 24;
  }

  private int yPadding() {
    return scaledHeight / 12;
  }
}
