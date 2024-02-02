package com.csonyi.cosmerecraft.client;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.mojang.blaze3d.platform.InputConstants;
import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyMappings {

  public static final String CATEGORIES_COSMERECRAFT = "key.categories.cosmerecraft";
  public static final Lazy<KeyMapping> SCAN_FOR_ANCHORS =
      Lazy.of(() -> new KeyMapping(
          "key.%s.scan_for_anchors".formatted(CosmereCraft.MOD_ID),
          KeyConflictContext.IN_GAME,
          InputConstants.Type.KEYSYM,
          GLFW.GLFW_KEY_KP_5,
          CATEGORIES_COSMERECRAFT));


  @SubscribeEvent
  public void registerBindings(RegisterKeyMappingsEvent event) {
    event.register(SCAN_FOR_ANCHORS.get());
  }
}
