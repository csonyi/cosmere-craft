package com.csonyi.cosmerecraft.capability.anchorobserver;

import com.csonyi.cosmerecraft.Config;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;

public class AnchorObserver implements IAnchorObserver {

  protected Set<BlockPos> knownAnchors;

  private static boolean isAnchorInRange(BlockPos anchor, BlockPos playerPos) {
    return anchor.distSqr(playerPos) <= Config.Server.maxSteelPushDistance;
  }

  @Override
  public Set<BlockPos> getAnchorsInRange(BlockPos playerPos) {
    return Optional.ofNullable(knownAnchors).stream()
        .flatMap(Set::stream)
        .filter(anchor -> isAnchorInRange(anchor, playerPos))
        .collect(Collectors.toSet());
  }

  @Override
  public boolean hasAnchorInRange(BlockPos playerPos) {
    return Optional.ofNullable(knownAnchors).stream()
        .flatMap(Set::stream)
        .anyMatch(anchor -> isAnchorInRange(anchor, playerPos));
  }

  @Override
  public void learnAnchor(BlockPos anchor) {
    if (knownAnchors == null) {
      knownAnchors = new HashSet<>();
    }
    knownAnchors.add(anchor);
  }

  @Override
  public void forgetAnchor(BlockPos anchor) {
    if (knownAnchors != null) {
      knownAnchors.remove(anchor);
    }
  }

  @Override
  public void copyFrom(IAnchorObserver other) {
    deserializeNBT(other.serializeNBT());
  }

  @Override
  public ListTag serializeNBT() {
    var tag = new ListTag();
    Optional.ofNullable(knownAnchors).stream()
        .flatMap(Set::stream)
        .map(NbtUtils::writeBlockPos)
        .forEach(tag::add);
    return tag;
  }

  @Override
  public void deserializeNBT(ListTag listTag) {
    knownAnchors = listTag.stream()
        .map(tag -> (CompoundTag) tag)
        .map(NbtUtils::readBlockPos)
        .collect(Collectors.toSet());
  }
}
