package com.csonyi.cosmerecraft.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class LevelUtils {

  public static boolean isEntityInDimension(Entity entity, ResourceKey<Level> dimensionKey) {
    return entity.level().dimension().compareTo(dimensionKey) == 0;
  }
}
