package com.csonyi.cosmerecraft.capability.anchorobserver;

import static com.csonyi.cosmerecraft.CosmereCraftBlocks.ANCHOR_TAG;
import static com.csonyi.cosmerecraft.Registry.attachmentTypes;

import com.csonyi.cosmerecraft.Config;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;

public class AnchorObserver {

  public static Supplier<AttachmentType<HashSet<BlockPos>>> KNOWN_ANCHORS = attachmentTypes().register(
      "known_anchors",
      () -> AttachmentType.builder(() -> new HashSet<BlockPos>()).build());

  private final ServerPlayer player;
  private final Level level;

  public AnchorObserver(ServerPlayer player) {
    this.player = player;
    this.level = player.level();
  }

  public void scan() {
    var rangeAABB = player.getBoundingBox()
        .inflate(Config.Server.maxSteelPushDistance);
    var newAnchors = BlockPos.betweenClosedStream(rangeAABB)
        .filter(pos -> level.getBlockState(pos).getTags().anyMatch(ANCHOR_TAG::equals))
        .collect(Collectors.toSet());
    setAnchors(newAnchors);
  }

  public boolean isAnchorInRange() {
    return !getAnchors().isEmpty();
  }

  public Set<BlockPos> getAnchors() {
    return player.getData(KNOWN_ANCHORS);
  }

  private void setAnchors(Set<BlockPos> newAnchors) {
    player.setData(KNOWN_ANCHORS, new HashSet<>(newAnchors));
  }
}
