package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class CosmereCraftKeyMappings {

  public static final String COSMERECRAFT_CATEGORIES = "key.categories.cosmerecraft";
  public static final Lazy<KeyMapping> OPEN_ALLOMANCY_GUI =
      Lazy.of(() -> new KeyMapping(
          getTranslationKey("open_allomancy_gui"),
          KeyConflictContext.IN_GAME,
          InputConstants.Type.KEYSYM,
          GLFW.GLFW_KEY_I,
          COSMERECRAFT_CATEGORIES));

  private static String getTranslationKey(String key) {
    return "key.%s.%s".formatted(CosmereCraft.MOD_ID, key);
  }
}
