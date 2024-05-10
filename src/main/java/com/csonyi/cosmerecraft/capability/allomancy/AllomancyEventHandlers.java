package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.anchors.AnchorObserver;
import com.csonyi.cosmerecraft.capability.anchors.ChunkAnchors;
import com.csonyi.cosmerecraft.gui.AllomancyGui;
import com.csonyi.cosmerecraft.gui.AllomanticLineRenderer;
import com.csonyi.cosmerecraft.networking.AnchorUpdateHandler;
import com.csonyi.cosmerecraft.networking.ClientMetalStateQueryHandler;
import com.csonyi.cosmerecraft.networking.WellLocationQueryHandler;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.csonyi.cosmerecraft.registry.CosmereCraftKeyMappings;
import com.csonyi.cosmerecraft.util.ErrorUtils;
import com.csonyi.cosmerecraft.util.MathUtils;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyEventHandlers {

  private final static int HALF_NIGHT_LENGTH = 5000;
  private final static int DAY_START = 1000;
  private final static int DAY_END = 13000;
  private final static int MIDNIGHT = 18000;

  private final static int MIN_NEAR_PLANE_DISTANCE = 1;
  private final static int MIN_FAR_PLANE_DISTANCE = 21;


  @SubscribeEvent
  public static void syncAttachmentsOnLogin(ClientPlayerNetworkEvent.LoggingIn event) {
    ClientMetalStateQueryHandler.queryMetalStatesFromServer();
  }

  @SubscribeEvent
  public static void syncAttachmentsOnClone(ClientPlayerNetworkEvent.Clone event) {
    new MetalStateManager(event.getNewPlayer()).copyFromOldPlayer(event.getOldPlayer());
  }

  @SubscribeEvent
  public static void handleAllomancyUpdates(final LivingEvent.LivingTickEvent event) {
    if (event.getEntity() instanceof Player player) {
      Allomancy.of(player).tick();
    }
  }

  @SubscribeEvent
  public static void handleAllomancyGuiOpen(final TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      while (CosmereCraftKeyMappings.OPEN_ALLOMANCY_GUI.get().consumeClick()) {
        Minecraft.getInstance().setScreen(new AllomancyGui());
      }
    }
  }

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
      if (Allomancy.of(localPlayer).isBurningAnyOf(AllomanticMetal.STEEL, AllomanticMetal.IRON)) {
        var anchors = new AnchorObserver(localPlayer).getAnchorsInRange();
        new AllomanticLineRenderer(event, anchors).renderLines();
      }
    }
  }

  /**
   * This listener is used to manipulate the player's field of view to lead them to a<br> Well Of Ascension (WOA) structure. It applies a
   * pulsating effect that changes depending on several factors:
   * <ul>
   *   <li>Whether or not there is an Ancient Medallion in the players inventory</li>
   *   <li>How far the player is from the closest WOA</li>
   *   <li>How precisely is the player looking in the direction of the closest WOA</li>
   * </ul>
   *
   * @param event The NeoForge event used to hook into the FOV computation.
   */
  @SubscribeEvent
  public static void shakeFovForAncientMedallion(ViewportEvent.ComputeFov event) {
    var minecraft = event.getRenderer().getMinecraft();
    var localPlayer = minecraft.player;
    var level = minecraft.level;
    if (localPlayer == null
        || level == null
        || !isHoldingAncientMedallion(localPlayer)) {
      return;
    }

    var wellPosition = getWellPosition(localPlayer);
    if (BlockPos.ZERO.equals(wellPosition)) {
      return;
    }

    var maxScaledFov = MathUtils.lorp(getViewToWellDirectionAngle(localPlayer, wellPosition) / 180, 5, 0.1);
    if (maxScaledFov > 0) {
      var clampedWellDistance = Mth.clamp((double) localPlayer.blockPosition().distManhattan(wellPosition), 0, 1000);
      var pulseDelaySeconds = Mth.lerp(clampedWellDistance / 1000, 1, 5);
      var ticksSincePulseStart = level.getGameTime() % (Mth.ceil(20 * pulseDelaySeconds));
      var newFov = getNewFov(event.getFOV(), maxScaledFov, ticksSincePulseStart);
      event.setFOV(newFov);
    }
  }

  @SubscribeEvent
  public static void playWellOfAscensionSound(LivingEvent.LivingTickEvent event) {
    if (!(event.getEntity() instanceof LocalPlayer localPlayer)
        || !isHoldingAncientMedallion(localPlayer)) {
      return;
    }

    try (var level = localPlayer.level()) {
      var wellPosition = getWellPosition(localPlayer);
      if (BlockPos.ZERO.equals(wellPosition)) {
        return;
      }

      var clampedWellDistance = Mth.clamp((double) localPlayer.blockPosition().distManhattan(wellPosition), 0, 1000);
      var pulseDelaySeconds = Mth.lerp(clampedWellDistance / 1000, 1.0, 5.0);
      var ticksSincePulseStart = level.getGameTime() % (Mth.ceil(20 * pulseDelaySeconds));
      var volume = (float) MathUtils.lorp(getViewToWellDirectionAngle(localPlayer, wellPosition) / 180, 2, 0.1);
      if (volume > 0 && (ticksSincePulseStart == 0 || ticksSincePulseStart == 10)) {
        level.playLocalSound(localPlayer, SoundEvents.NOTE_BLOCK_BASEDRUM.value(), SoundSource.AMBIENT, volume, 0.5f);
      }
    } catch (IOException e) {
      ErrorUtils.handleError("playWellOfAscensionSound", e);
    }
  }

  @SubscribeEvent
  public static void renderScadrialMistBasedOnTimeOfDay(ViewportEvent.RenderFog event) {
    // TODO: test till you die
    if (FogRenderer.FogMode.FOG_TERRAIN.equals(event.getMode())) {
      var currentTimeOfDay = event.getCamera().getEntity().level().dayTime();
      if (!isFogTime(currentTimeOfDay)) {
        return;
      }
      var currentNearPlaneDistance = event.getNearPlaneDistance();
      var currentFarPlaneDistance = event.getFarPlaneDistance();
      if (isBeforeMidnight(currentTimeOfDay)) {
        var ticksSinceDayEnd = (float) currentTimeOfDay - DAY_END;
        var newNearPlaneDistance = calculateFogDistance(ticksSinceDayEnd, MIN_NEAR_PLANE_DISTANCE, currentNearPlaneDistance);
        var newFarPlaneDistance = calculateFogDistance(ticksSinceDayEnd, MIN_FAR_PLANE_DISTANCE, currentFarPlaneDistance);
        event.setNearPlaneDistance(newNearPlaneDistance);
        event.setFarPlaneDistance(newFarPlaneDistance);
      } else {
        var ticksSinceMidnight = (float) currentTimeOfDay - MIDNIGHT;
        var newNearPlaneDistance = calculateFogDistance(ticksSinceMidnight, currentNearPlaneDistance, MIN_NEAR_PLANE_DISTANCE);
        var newFarPlaneDistance = calculateFogDistance(ticksSinceMidnight, currentFarPlaneDistance, MIN_FAR_PLANE_DISTANCE);
        event.setNearPlaneDistance(newNearPlaneDistance);
        event.setFarPlaneDistance(newFarPlaneDistance);
      }
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void collectAnchorsInChunk(ChunkEvent.Load event) {
    var chunkAnchors = new ChunkAnchors(event.getChunk(), event.isNewChunk());
    chunkAnchors.scanIfNeeded();
    chunkAnchors.saveAnchors();
  }

  @SubscribeEvent
  public static void addAnchorToChunk(BlockEvent.EntityPlaceEvent event) {
    if (!event.getPlacedBlock().is(CosmereCraftBlocks.ANCHOR_TAG)) {
      return;
    }
    var blockPos = event.getPos();
    var chunk = event.getLevel().getChunk(blockPos);
    var chunkAnchors = ChunkAnchors.of(chunk);
    chunkAnchors.addAnchor(blockPos);
    chunkAnchors.saveAnchors();
  }

  @SubscribeEvent
  public static void removeAnchorFromChunk(BlockEvent.BreakEvent event) {
    if (!event.getState().is(CosmereCraftBlocks.ANCHOR_TAG)) {
      return;
    }
    var blockPos = event.getPos();
    var chunk = event.getLevel().getChunk(blockPos);
    var chunkAnchors = ChunkAnchors.of(chunk);
    chunkAnchors.removeAnchor(blockPos);
    chunkAnchors.saveAnchors();
  }

  @SubscribeEvent
  public static void removeExplodedAnchors(ExplosionEvent.Detonate event) {
    var level = event.getLevel();
    var destroyedAnchors = event.getAffectedBlocks().stream()
        .filter(blockPos -> level.getBlockState(blockPos).is(CosmereCraftBlocks.ANCHOR_TAG))
        .collect(Collectors.groupingBy(ChunkPos::new));

    destroyedAnchors.forEach((chunkPos, blockPosList) -> {
      var chunk = level.getChunk(chunkPos.x, chunkPos.z);
      var chunkAnchors = ChunkAnchors.of(chunk);
      chunkAnchors.removeAll(blockPosList);
      chunkAnchors.saveAnchors();
    });
  }

  @SubscribeEvent
  public static void updateClientsWithAnchorData(ChunkWatchEvent.Sent event) {
    var chunkAnchors = ChunkAnchors.of(event.getChunk());
    chunkAnchors.scanIfNeeded();
    AnchorUpdateHandler.updateClient(event.getPlayer(), event.getChunk().getPos(), chunkAnchors.getAnchors());
  }

  private static BlockPos getWellPosition(LocalPlayer localPlayer) {
    return localPlayer.getExistingData(CosmereCraftAttachments.TRACKED_WELL)
        .orElseGet(() -> {
          WellLocationQueryHandler.queryWellLocation(localPlayer.blockPosition());
          return localPlayer.getData(CosmereCraftAttachments.TRACKED_WELL);
        });
  }

  private static double getViewToWellDirectionAngle(LocalPlayer localPlayer, BlockPos wellPosition) {
    var wellDirectionVector = Vec3.atCenterOf(wellPosition.subtract(localPlayer.blockPosition())).normalize();
    return MathUtils.vectorAngleInDegrees(localPlayer.getLookAngle(), wellDirectionVector);
  }

  private static double getNewFov(double baseFov, double maxScaledFov, long ticksSincePulseStart) {
    if (isInTimeWindowSincePulseStart(ticksSincePulseStart, 0, 5)) {
      return baseFov + Mth.lerp(ticksSincePulseStart / 5d, 0, maxScaledFov);
    }
    if (isInTimeWindowSincePulseStart(ticksSincePulseStart, 5, 10)) {
      return baseFov + Mth.lerp((ticksSincePulseStart - 5) / 5d, maxScaledFov, 0);
    }
    if (isInTimeWindowSincePulseStart(ticksSincePulseStart, 10, 15)) {
      return baseFov + Mth.lerp((ticksSincePulseStart - 10) / 5d, 0, maxScaledFov);
    }
    if (isInTimeWindowSincePulseStart(ticksSincePulseStart, 15, 20)) {
      return baseFov + Mth.lerp((ticksSincePulseStart - 15) / 5d, maxScaledFov, 0);
    }
    return baseFov;
  }

  /**
   * Checks if the current tick is within the time window relative to the start of a pulse.
   *
   * @param ticksSinceStart The number of ticks since the start of the pulse.
   * @param startTick       How many ticks after the start of the pulse the window starts.
   * @param endTick         How many ticks after the start of the pulse the window ends.
   * @return True if the current tick is within the time window, false otherwise.
   */
  private static boolean isInTimeWindowSincePulseStart(long ticksSinceStart, int startTick, int endTick) {
    return ticksSinceStart >= startTick && ticksSinceStart < endTick;
  }


  private static boolean isHoldingAncientMedallion(LocalPlayer localPlayer) {
    return Stream.of(
            localPlayer.getMainHandItem(),
            localPlayer.getOffhandItem())
        .anyMatch(itemStack -> itemStack.is(CosmereCraftItems.ANCIENT_MEDALLION));
  }

  private static boolean isFogTime(long timeOfDay) {
    return timeOfDay >= DAY_END || timeOfDay <= DAY_START;
  }

  private static boolean isBeforeMidnight(long timeOfDay) {
    return timeOfDay < MIDNIGHT;
  }

  private static float calculateFogDistance(float elapsedTime, float start, float end) {
    return Mth.lerp(elapsedTime / HALF_NIGHT_LENGTH, start, end);
  }
}
