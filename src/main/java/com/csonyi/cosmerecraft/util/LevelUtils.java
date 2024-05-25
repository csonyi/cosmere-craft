package com.csonyi.cosmerecraft.util;

import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

public class LevelUtils {

  public static boolean isEntityInDimension(Entity entity, ResourceKey<Level> dimensionKey) {
    return entity.level().dimension().compareTo(dimensionKey) == 0;
  }

  public static Supplier<Stream<ChunkAccess>> surroundingChunksStreamGetter(ChunkAccess chunk) {
    if (chunk.getLevel() == null) {
      return Stream::empty;
    }
    return () -> ChunkPos.rangeClosed(chunk.getPos(), 1)
        .map(ChunkPos::getWorldPosition)
        .map(chunk.getLevel()::getChunkAt);
  }
}
