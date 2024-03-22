package com.csonyi.cosmerecraft.gui;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AllomancyGui extends Screen {

  private final Map<AllomanticMetal, MetalStateEditor> metalEditors = new HashMap<>();
  private MetalStateManager metalStateManager;

  public AllomancyGui() {
    super(Component.literal("Allomancy"));
  }

  @Override
  protected void init() {
    super.init();
    if (minecraft == null) {
      return;
    }
    metalStateManager = new MetalStateManager(minecraft.player);
    AllomanticMetal.stream()
        .filter(metal -> !metal.isGodMetal())
        .forEach(this::createMetalEditor);
    metalEditors.values().stream()
        .map(MetalStateEditor::widgets)
        .flatMap(Set::stream)
        .forEach(this::addRenderableWidget);
  }

  @Override
  public void tick() {
    super.tick();
    // TODO: update the server with the new state
  }

  @Override
  public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
    super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
  }

  private void createMetalEditor(AllomanticMetal metal) {
    var metalState = metalStateManager.getState(metal);
    var editor = new MetalStateEditor(
        metalState,
        16, 16 + metal.ordinal() * 20, // TODO: set the correct position
        font);
    metalEditors.put(metal, editor);
  }
}
