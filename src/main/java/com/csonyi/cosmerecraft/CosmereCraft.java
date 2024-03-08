package com.csonyi.cosmerecraft;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CosmereCraft.MOD_ID)
public class CosmereCraft {

  // private static final Logger LOGGER = LogUtils.getLogger();
  public static final String MOD_ID = "cosmerecraft";

  public CosmereCraft(IEventBus modEventBus) {
    Registry.register(modEventBus);
  }

  public static ResourceLocation createResourceLocation(String path) {
    return new ResourceLocation(MOD_ID, path);
  }

}
