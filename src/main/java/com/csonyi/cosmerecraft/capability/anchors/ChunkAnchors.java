package com.csonyi.cosmerecraft.capability.anchors;

import com.csonyi.cosmerecraft.capability.ChunkCapability;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftCapabilities;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

public class ChunkAnchors {

  public static final Codec<List<BlockPos>> CODEC = Codec.list(BlockPos.CODEC);

  private final ChunkAccess chunk;
  private final List<BlockPos> anchors;
  private boolean scanned;

  private ChunkAnchors(ChunkAccess chunk, boolean newChunk) {
    this.chunk = chunk;
    this.scanned = !newChunk && hasNonEmptyData();
    this.anchors = new ArrayList<>();
    this.anchors.addAll(chunk.getData(CosmereCraftAttachments.CHUNK_ANCHORS));
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
    blockPos.stream()
        .map(BlockPos::immutable)
        .forEach(anchors::remove);
  }

  public void addAll(Collection<BlockPos> blockPos) {
    blockPos.stream()
        .map(BlockPos::immutable)
        .forEach(this::addAnchor);
  }

  public List<BlockPos> getAnchors() {
    return anchors;
  }

  public void setAnchors(Collection<BlockPos> anchors) {
    this.anchors.clear();
    var immutableAnchors = anchors.stream()
        .map(BlockPos::immutable)
        .toList();
    this.anchors.addAll(immutableAnchors);
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

  public static void register(ChunkAccess chunk) {
    ChunkCapability.registerChunk(CosmereCraftCapabilities.CHUNK_ANCHORS, chunk, ChunkAnchors::new);
  }

  public static ChunkAnchors ofExisting(ChunkAccess chunk) {
    return getOrCreate(chunk, false);
  }

  public static ChunkAnchors getOrCreate(ChunkAccess chunk, boolean isNewChunk) {
    return CosmereCraftCapabilities.CHUNK_ANCHORS.getCapability(chunk, isNewChunk)
        .orElseGet(() -> {
          register(chunk);
          return ofExisting(chunk);
        });
  }

}
