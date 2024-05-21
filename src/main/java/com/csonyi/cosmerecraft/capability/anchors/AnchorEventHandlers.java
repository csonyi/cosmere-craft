package com.csonyi.cosmerecraft.capability.anchors;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.gui.AllomanticLineRenderer;
import com.csonyi.cosmerecraft.networking.AnchorUpdateHandler;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class AnchorEventHandlers {

  /**
   * Hooks into the world rendering process to draw the allomantic lines.
   *
   * @param event The NeoForge event used to hook into the renderer.
   */
  @SubscribeEvent
  public static void renderAllomanticLines(RenderLevelStageEvent event) {
    if (RenderLevelStageEvent.Stage.AFTER_PARTICLES.equals(event.getStage())) {
      var localPlayer = Minecraft.getInstance().player;
      if (localPlayer == null) {
        return;
      }
      if (IAllomancy.of(localPlayer).isBurningAnyOf(AllomanticMetal.STEEL, AllomanticMetal.IRON)) {
        var anchors = new AnchorObserver(localPlayer).getAnchorsInRange();
        new AllomanticLineRenderer(event, anchors).renderLines();
      }
    }
  }

  @SubscribeEvent
  public static void collectAnchorsInChunk(ChunkEvent.Load event) {
    var chunkAnchors = ChunkAnchors.getOrCreate(event.getChunk(), event.isNewChunk());
    chunkAnchors.scanIfNeeded();
    chunkAnchors.saveAnchors();
  }

  @SubscribeEvent
  public static void addAnchorToChunk(BlockEvent.EntityPlaceEvent event) {
    if (!event.getPlacedBlock().is(CosmereCraftBlocks.ANCHOR_TAG)) {
      return;
    }
    var blockPos = event.getPos();
    var level = event.getLevel();
    var chunk = level.getChunk(blockPos);
    var chunkAnchors = ChunkAnchors.ofExisting(chunk);
    chunkAnchors.addAnchor(blockPos);
    chunkAnchors.saveAnchors();

    if (level.getServer() != null) {
      level.getServer().getPlayerList().getPlayers()
          .forEach(serverPlayer ->
              AnchorUpdateHandler.replaceClientAnchors(serverPlayer, chunk.getPos(), chunkAnchors.getAnchors()));
    }
  }

  @SubscribeEvent
  public static void removeAnchorFromChunk(BlockEvent.BreakEvent event) {
    if (!event.getState().is(CosmereCraftBlocks.ANCHOR_TAG)) {
      return;
    }
    var blockPos = event.getPos();
    var chunkAnchors = ChunkAnchors.ofExisting(event.getLevel().getChunk(blockPos));
    chunkAnchors.removeAnchor(blockPos);
    chunkAnchors.saveAnchors();

    var level = event.getLevel();
    if (level.getServer() != null) {
      var chunk = level.getChunk(event.getPos());
      level.getServer().getPlayerList().getPlayers()
          .forEach(serverPlayer ->
              AnchorUpdateHandler.replaceClientAnchors(serverPlayer, chunk.getPos(), chunkAnchors.getAnchors()));
    }
  }

  @SubscribeEvent
  public static void removeExplodedAnchors(ExplosionEvent.Detonate event) {
    var level = event.getLevel();
    var destroyedAnchors = event.getAffectedBlocks().stream()
        .filter(blockPos -> level.getBlockState(blockPos).is(CosmereCraftBlocks.ANCHOR_TAG))
        .collect(Collectors.groupingBy(ChunkPos::new));

    destroyedAnchors.forEach((chunkPos, blockPosList) -> {
      var chunkAnchors = ChunkAnchors.ofExisting(level.getChunk(chunkPos.x, chunkPos.z));
      chunkAnchors.removeAll(blockPosList);
      chunkAnchors.saveAnchors();
      if (level.getServer() != null) {
        level.getServer().getPlayerList().getPlayers()
            .forEach(serverPlayer ->
                AnchorUpdateHandler.replaceClientAnchors(serverPlayer, chunkPos, chunkAnchors.getAnchors()));
      }
    });
  }

  @SubscribeEvent
  public static void updateClientsWithAnchorData(ChunkWatchEvent.Sent event) {
    var chunkAnchors = ChunkAnchors.ofExisting(event.getChunk());
    chunkAnchors.scanIfNeeded();
    AnchorUpdateHandler.replaceClientAnchors(event.getPlayer(), event.getChunk().getPos(), chunkAnchors.getAnchors());
  }

}
