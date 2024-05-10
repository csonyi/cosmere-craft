package com.csonyi.cosmerecraft.capability.anchors;

import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

public class ChunkAnchors {

  private final ChunkAccess chunk;
  private final HashSet<BlockPos> anchors;
  private boolean scanned;

  private ChunkAnchors(ChunkAccess chunk) {
    this(chunk, false);
  }

  public ChunkAnchors(ChunkAccess chunk, boolean newChunk) {
    this.chunk = chunk;
    this.scanned = !newChunk && hasNonEmptyData();
    this.anchors = loadAnchors();
  }

  public static ChunkAnchors of(ChunkAccess chunk) {
    return new ChunkAnchors(chunk);
  }

  public void scanIfNeeded() {
    if (!scanned) {
      chunk.findBlocks(
          blockState -> blockState.is(CosmereCraftBlocks.ANCHOR_TAG),
          (blockPos, blockState) -> anchors.add(blockPos.immutable()));
      scanned = true;
    }
  }

  public void addAnchor(BlockPos anchor) {
    anchors.add(anchor.immutable());
  }

  public void removeAnchor(BlockPos anchor) {
    anchors.remove(anchor.immutable());
  }

  public void removeAll(Collection<BlockPos> blockPos) {
    var immutableBlockPos = blockPos.stream()
        .map(BlockPos::immutable)
        .toList();
    immutableBlockPos.forEach(anchors::remove);
  }

  public HashSet<BlockPos> getAnchors() {
    return anchors;
  }

  public void setAnchors(HashSet<BlockPos> anchors) {
    this.anchors.clear();
    var immutableAnchors = anchors.stream()
        .map(BlockPos::immutable)
        .toList();
    this.anchors.addAll(immutableAnchors);
  }

  private HashSet<BlockPos> loadAnchors() {
    return chunk.getData(CosmereCraftAttachments.CHUNK_ANCHORS);
  }

  public void saveAnchors() {
    chunk.setData(CosmereCraftAttachments.CHUNK_ANCHORS, anchors);
  }

  private boolean hasNonEmptyData() {
    return chunk.getExistingData(CosmereCraftAttachments.CHUNK_ANCHORS)
        .map(data -> !data.isEmpty())
        .orElse(false);
  }

  public ChunkPos getPos() {
    return chunk.getPos();
  }

}
