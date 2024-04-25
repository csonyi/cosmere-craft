package com.csonyi.cosmerecraft.util;

import com.csonyi.cosmerecraft.CosmereCraft;
import net.minecraft.resources.ResourceLocation;

public class ResourceUtils {

  private ResourceUtils() {
    // Utility class, no instantiation
  }


  /**
   * Creates a resource location in the default ("minecraft") namespace from the given path.
   *
   * @param path the path of the resource location
   * @return the resource location
   */
  public static ResourceLocation minecraftLocation(String path) {
    return new ResourceLocation(path);
  }

  /**
   * Creates a resource location in the mod's (value of {@link CosmereCraft#MOD_ID}) namespace from the given path.
   *
   * @param path the path of the resource location
   * @return the resource location
   */
  public static ResourceLocation modLocation(String path) {
    return new ResourceLocation(CosmereCraft.MOD_ID, path);
  }
}
