package com.csonyi.cosmerecraft.capability.anchors;

import com.csonyi.cosmerecraft.ServerConfig;
import com.csonyi.cosmerecraft.util.LevelUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class AnchorObserver {

  private final Player player;
  private final Set<ChunkAnchors> surroundingChunks;

  public AnchorObserver(Player player) {
    this.player = player;
    var chunk = player.level().getChunkAt(player.blockPosition());
    this.surroundingChunks =
        LevelUtils.surroundingChunksStreamGetter(chunk).get()
            .map(ChunkAnchors::ofExisting)
            .collect(Collectors.toSet());
  }

  public boolean hasAnchorInRange() {
    if (!ServerConfig.allomancyNeedsMetalNearby) {
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

  private boolean isAnchorInRange(BlockPos anchorPos) {
    return player.getBoundingBox()
        .inflate(ServerConfig.maxSteelPushDistance)
        .contains(anchorPos.getCenter());
  }

}
