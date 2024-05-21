package com.csonyi.cosmerecraft.capability.anchors;

import com.csonyi.cosmerecraft.Config;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class AnchorObserver {

  private final Player player;
  private final Level level;
  private final ChunkAnchors currentChunk;
  private final Set<ChunkAnchors> surroundingChunks;

  public AnchorObserver(Player player) {
    this.player = player;
    this.level = player.level();
    this.currentChunk = ChunkAnchors.ofExisting(level.getChunkAt(player.blockPosition()));
    this.surroundingChunks = findSurroundingChunks()
        .map(ChunkAnchors::ofExisting)
        .collect(Collectors.toSet());
  }

  public boolean hasAnchorInRange() {
    if (!Config.Server.allomancyNeedsMetalNearby) {
      return true;
    }
    return !getAnchorsInRange().isEmpty();
  }

  public Set<BlockPos> getAnchorsInRange() {
    return streamSurroundingAnchors()
        .filter(this::isAnchorInRange)
        .collect(Collectors.toSet());
  }

  private Stream<BlockPos> streamSurroundingAnchors() {
    return surroundingChunks.stream()
        .map(ChunkAnchors::getAnchors)
        .flatMap(List::stream);
  }

  private Stream<LevelChunk> findSurroundingChunks() {
    return ChunkPos.rangeClosed(currentChunk.getPos(), 1)
        .map(ChunkPos::getWorldPosition)
        .map(level::getChunkAt);
  }

  private boolean isAnchorInRange(BlockPos anchorPos) {
    return player.getBoundingBox()
        .inflate(Config.Server.maxSteelPushDistance)
        .contains(anchorPos.getCenter());
  }

}
