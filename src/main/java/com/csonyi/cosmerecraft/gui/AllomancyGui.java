package com.csonyi.cosmerecraft.gui;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AllomancyGui extends Screen {

  private final Map<AllomanticMetal, MetalStateEditor> metalEditors = new HashMap<>();
  private final MetalStateManager metalStateManager;

  protected AllomancyGui(Component pTitle) {
    super(pTitle);
    metalStateManager = new MetalStateManager(minecraft.player);
  }

  @Override
  protected void init() {
    super.init();
    if (minecraft == null) {
      return;
    }
    AllomanticMetal.stream().forEach(this::createMetalEditor);


  }

  private void createMetalEditor(AllomanticMetal metal) {
    var metalState = metalStateManager.getState(metal);
    var editor = new MetalStateEditor(
        metalState,
        0, 0,
        font);
    metalEditors.put(metal, editor);
  }
}
