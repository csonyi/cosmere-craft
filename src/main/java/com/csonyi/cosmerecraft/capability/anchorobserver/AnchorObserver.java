package com.csonyi.cosmerecraft.capability.anchorobserver;

import com.csonyi.cosmerecraft.Config;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;

public class AnchorObserver {

  public static Supplier<AttachmentType<HashSet<BlockPos>>> KNOWN_ANCHORS =
      () -> AttachmentType.builder(() -> new HashSet<BlockPos>()).build();

  private final Player player;

  public AnchorObserver(Player player) {
    this.player = player;
  }

  private boolean isAnchorInRange(BlockPos anchor) {
    return anchor.distSqr(player.blockPosition()) <= Config.Server.maxSteelPushDistance;
  }

  public Set<BlockPos> getAnchorsInRange() {
    return getKnownAnchors().stream()
        .filter(this::isAnchorInRange)
        .collect(Collectors.toSet());
  }

  public boolean hasAnchorInRange() {
    return getKnownAnchors().stream()
        .anyMatch(this::isAnchorInRange);
  }

  public void learnAnchor(BlockPos anchor) {
    var newAnchors = getKnownAnchors();
    newAnchors.add(anchor);
    setKnownAnchors(newAnchors);
  }

  public void forgetAnchor(BlockPos anchor) {
    var newAnchors = getKnownAnchors();
    newAnchors.remove(anchor);
    setKnownAnchors(newAnchors);
  }

  private Set<BlockPos> getKnownAnchors() {
    return player.getData(KNOWN_ANCHORS);
  }

  private void setKnownAnchors(Set<BlockPos> newAnchors) {
    player.setData(KNOWN_ANCHORS, new HashSet<>(newAnchors));
  }
}
