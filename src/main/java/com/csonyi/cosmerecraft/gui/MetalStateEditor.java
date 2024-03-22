package com.csonyi.cosmerecraft.gui;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import java.util.Set;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

public class MetalStateEditor {

  private static final int TEXTURE_SIZE = 16;
  private static final int SLIDER_WIDTH = 64;

  private final AllomanticMetal.State metalState;
  public final ImageWidget metalTexture;
  public final ExtendedSlider slider;
  public final StringWidget placeHolder;
  public final StringWidget reserve;

  public MetalStateEditor(AllomanticMetal.State metalState, int x, int y, Font font) {
    this.metalState = metalState;
    this.metalTexture = createMetalTexture(x, y);
    this.slider = createSlider(x, y);
    this.placeHolder = createPlaceHolder(x, y, font);
    this.reserve = createReserve(x, y, font);
  }


  private ImageWidget createMetalTexture(int x, int y) {
    var texture = ImageWidget.texture(
        TEXTURE_SIZE, TEXTURE_SIZE,
        metalState.metal().textureLocation(),
        TEXTURE_SIZE, TEXTURE_SIZE);
    texture.setPosition(x, y);
    return texture;
  }

  private ExtendedSlider createSlider(int x, int y) {
    var metal = metalState.metal();
    return new ExtendedSlider(
        x + TEXTURE_SIZE + 4, y,
        SLIDER_WIDTH, TEXTURE_SIZE,
        Component.translatable(metal.getTranslationKey()), Component.empty(),
        0, metal.maxBurnStrength, metalState.burnStrength(), true);
  }

  private StringWidget createPlaceHolder(int x, int y, Font font) {
    return new StringWidget(
        x + TEXTURE_SIZE + 4, y,
        SLIDER_WIDTH, TEXTURE_SIZE,
        Component.translatable(metalState.metal().getTranslationKey()), font);
  }

  private StringWidget createReserve(int x, int y, Font font) {
    return new StringWidget(
        x + TEXTURE_SIZE + 4 + SLIDER_WIDTH + 4, y,
        SLIDER_WIDTH / 2, TEXTURE_SIZE,
        Component.literal(String.valueOf(metalState.reserve())), font);
  }

  public Set<? extends AbstractWidget> widgets() {
    return metalState.available()
        ? Set.of(metalTexture, slider, reserve)
        : Set.of(metalTexture, placeHolder, reserve);
  }

}
