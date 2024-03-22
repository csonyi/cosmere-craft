package com.csonyi.cosmerecraft.capability.anchorobserver;

import static com.csonyi.cosmerecraft.registry.CosmereCraftAttachments.KNOWN_ANCHORS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.ANCHOR_TAG;

import com.csonyi.cosmerecraft.Config;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import org.slf4j.Logger;

public class AnchorObserver {

  private static final Logger LOGGER = LogUtils.getLogger();

  private final ServerPlayer player;
  private final Lazy<Level> level;

  public AnchorObserver(ServerPlayer player) {
    this.player = player;
    this.level = Lazy.of(player::level);
  }

  public Level level() {
    return level.get();
  }

  public void scan() {
    try (var level = level()) {
      var rangeAABB = player.getBoundingBox()
          .inflate(Config.Server.maxSteelPushDistance);
      var newAnchors = BlockPos.betweenClosedStream(rangeAABB)
          .filter(pos -> isAnchor(pos, level))
          .collect(Collectors.toSet());
      setAnchors(newAnchors);
    } catch (IOException e) {
      LOGGER.error("IOException while retrieving level in scan: {}", e.getMessage());
    }
  }

  public boolean hasAnchorInRange() {
    if (!Config.Server.allomancyNeedsMetalNearby) {
      return true;
    }
    return !getAnchors().isEmpty();
  }

  private Set<BlockPos> getAnchors() {
    return player.getData(KNOWN_ANCHORS);
  }

  private void setAnchors(Set<BlockPos> newAnchors) {
    player.setData(KNOWN_ANCHORS, new HashSet<>(newAnchors));
  }

  private boolean isAnchor(BlockPos pos, Level level) {
    return level.getBlockState(pos).getTags().anyMatch(ANCHOR_TAG::equals);
  }
}
