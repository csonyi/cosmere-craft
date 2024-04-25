package com.csonyi.cosmerecraft.gui;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

public class EditorQuadrant extends AbstractWidget {

  private static final ResourceLocation BACKGROUND = ResourceUtils.modLocation("textures/gui/allomancy_gui_box.png");
  private static final int ICON_SIZE = 16;

  private final Font font;
  private final AllomanticMetal.Type type;
  private final AllomanticMetal.State[][] metalStates;
  private final ImageWidget[][] metalTextures;
  private final StringWidget[][] metalNames;
  private final ExtendedSlider[][] sliders;
  private final StringWidget[][] reserves;

  public EditorQuadrant(
      int x, int y, int width, int height,
      Font font, AllomanticMetal.Type type,
      MetalStateManager metalStateManager) {
    super(x, y, width, height, type.getNameAsComponent());
    this.font = font;
    this.type = type;
    var metalStates = metalStateManager.getStates(type);
    this.metalStates = new AllomanticMetal.State[][]{
        {metalStates.get(0), metalStates.get(1)},
        {metalStates.get(2), metalStates.get(3)}
    };
    this.metalTextures = new ImageWidget[][]{
        {createMetalTexture(0, 0), createMetalTexture(0, 1)},
        {createMetalTexture(1, 0), createMetalTexture(1, 1)}
    };
    this.metalNames = new StringWidget[][]{
        {createMetalName(0, 0, font), createMetalName(0, 1, font)},
        {createMetalName(1, 0, font), createMetalName(1, 1, font)}
    };
    this.sliders = new ExtendedSlider[][]{
        {createSlider(0, 0), createSlider(0, 1)},
        {createSlider(1, 0), createSlider(1, 1)}
    };
    this.reserves = new StringWidget[][]{
        {createReserve(0, 0, font), createReserve(0, 1, font)},
        {createReserve(1, 0, font), createReserve(1, 1, font)}
    };
  }

  @Override
  protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    guiGraphics.blit(
        BACKGROUND,
        getX(), getY(),
        width, height,
        0, 0,
        width, height,
        width, height);
    guiGraphics.drawString(
        font, getMessage(),
        getX() + Mth.floor(width / 2f) - (font.width(getMessage()) / 2),
        getY() + Mth.floor(height * 0.055),
        0xffffff);
  }

  private ImageWidget createMetalTexture(int x, int y) {
    var metalState = metalStates[x][y];
    var texture = ImageWidget.texture(
        ICON_SIZE, ICON_SIZE,
        metalState.metal().textureLocation("white"),
        ICON_SIZE, ICON_SIZE);
    texture.setPosition(
        getX() + xMargin() + (x * (width / 2)),
        getY() + yMargin() + (y * Mth.floor(height / 2.4)));
    return texture;
  }

  private StringWidget createMetalName(int x, int y, Font font) {
    var metalState = metalStates[x][y].metal();
    return new StringWidget(
        metalTextures[x][y].getRight() + xPadding(),
        getY() + yMargin() + (y * Mth.floor(height / 2.4)),
        editorWidth(), ICON_SIZE,
        metalState.getNameAsComponent(), font);
  }

  private ExtendedSlider createSlider(int x, int y) {
    var metalState = metalStates[x][y];
    if (!metalState.available()) {
      return null;
    }
    var metal = metalState.metal();
    return new ExtendedSlider(
        getX() + xMargin() + (x * (width / 2)),
        metalTextures[x][y].getBottom() + yPadding(),
        editorWidth(), ICON_SIZE,
        Component.empty(), Component.empty(),
        0, metal.maxBurnStrength, metalState.burnStrength(), true);
  }

  private StringWidget createReserve(int x, int y, Font font) {
    var metalState = metalStates[x][y];
    if (!metalState.available()) {
      return null;
    }
    var component = metalState.getReserve()
        .map(TickUtils::toTimeFormat)
        .orElse(Component.literal("-"));
    return new StringWidget(
        sliders[x][y].getRight() + xPadding(), metalTextures[x][y].getBottom() + yPadding(),
        font.width(component), ICON_SIZE,
        component, font);
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    // TODO: a11y
  }

  @Override
  public void playDownSound(SoundManager pHandler) {
  }

  public Stream<AbstractWidget> streamChildren() {
    return Stream.of(metalTextures, metalNames, sliders, reserves)
        .flatMap(Arrays::stream)
        .flatMap(Arrays::stream)
        .filter(Objects::nonNull);
  }

  public Set<AllomanticMetal.State> getStates() {
    return Set.of(
        AllomanticMetal.State.burnStrengthUpdate(
            metalStates[0][0].metal(),
            Optional.ofNullable(sliders[0][0])
                .map(ExtendedSlider::getValueInt)
                .orElse(0)),
        AllomanticMetal.State.burnStrengthUpdate(
            metalStates[0][1].metal(),
            Optional.ofNullable(sliders[0][1])
                .map(ExtendedSlider::getValueInt)
                .orElse(0)),
        AllomanticMetal.State.burnStrengthUpdate(
            metalStates[1][0].metal(),
            Optional.ofNullable(sliders[1][0])
                .map(ExtendedSlider::getValueInt)
                .orElse(0)),
        AllomanticMetal.State.burnStrengthUpdate(
            metalStates[1][1].metal(),
            Optional.ofNullable(sliders[1][1])
                .map(ExtendedSlider::getValueInt)
                .orElse(0)));
  }

  private int xMargin() {
    return width / 24;
  }

  private int yMargin() {
    return height / 5;
  }

  private int xPadding() {
    return width / 64;
  }

  private int yPadding() {
    return height / 25;
  }

  private int editorWidth() {
    return Mth.floor(width / 3.8);
  }

  private int reserveWidth() {
    return width / 8;
  }
}
