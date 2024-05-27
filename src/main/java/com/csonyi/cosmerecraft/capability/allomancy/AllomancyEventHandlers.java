package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.gui.AllomancyGui;
import com.csonyi.cosmerecraft.networking.ClientMetalStateQueryHandler;
import com.csonyi.cosmerecraft.networking.WellLocationQueryHandler;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.csonyi.cosmerecraft.registry.CosmereCraftKeyMappings;
import com.csonyi.cosmerecraft.util.ErrorUtils;
import com.csonyi.cosmerecraft.util.LevelUtils;
import com.csonyi.cosmerecraft.util.MathUtils;
import com.google.common.collect.Streams;
import java.io.IOException;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class AllomancyEventHandlers {

  private final static int FOG_TRANSITION_TIME = 3000;
  private final static int FOG_GETTING_DENSER = 12000;
  private final static int FOG_STOPS_GETTING_DENSER = 15000;
  private final static int FOG_STARTS_THINNING = 22000;
  private final static int FOG_FINISHES_THINNING = 1000;

  private final static int MIN_NEAR_PLANE_DISTANCE = 1;
  private final static int MIN_FAR_PLANE_DISTANCE = 5;


  @SubscribeEvent
  public static void syncAttachmentsOnLogin(ClientPlayerNetworkEvent.LoggingIn event) {
    ClientMetalStateQueryHandler.initializeLocalMetalStates();
  }

  @SubscribeEvent
  public static void syncAttachmentsOnClone(ClientPlayerNetworkEvent.Clone event) {
    IAllomancy.of(event.getNewPlayer()).copyFrom(event.getOldPlayer(), true);
  }

  @SubscribeEvent
  public static void handlePlayerCloneEvent(PlayerEvent.Clone event) {
    var originalPlayer = event.getOriginal();
    var newPlayer = event.getEntity();
    IAllomancy.of(newPlayer).copyFrom(originalPlayer, event.isWasDeath());
    if (event.isWasDeath() && !hadAncientMedal(originalPlayer)) {
      CosmereCraftAttachments.copyAttachments(originalPlayer, newPlayer, CosmereCraftAttachments.FOUND_MEDALLION);
    }
  }

  @SubscribeEvent
  public static void handleAllomancyUpdates(final PlayerTickEvent.Pre event) {
    IAllomancy.of(event.getEntity()).tick();
  }

  @SubscribeEvent
  public static void handleAllomancyGuiOpen(final ClientTickEvent.Pre event) {
    while (CosmereCraftKeyMappings.OPEN_ALLOMANCY_GUI.get().consumeClick()) {
      Minecraft.getInstance().setScreen(new AllomancyGui());
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
        || notHoldingAncientMedallion(localPlayer)) {
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
  public static void playWellOfAscensionSound(PlayerTickEvent.Post event) {
    if (!(event.getEntity() instanceof LocalPlayer localPlayer)
        || notHoldingAncientMedallion(localPlayer)) {
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
    if (!LevelUtils.isEntityInDimension(event.getCamera().getEntity(), CosmereCraft.SCADRIAL)) {
      return;
    }
    var currentTimeOfDay = event.getCamera().getEntity().level().dayTime();
    if (!isFogTime(currentTimeOfDay)) {
      return;
    }
    var tinCompensation = new MetalStateManager((Player) event.getCamera().getEntity()).isActive(AllomanticMetal.TIN)
        ? 50
        : 0;
    var currentNearPlaneDistance = event.getNearPlaneDistance();
    var currentFarPlaneDistance = event.getFarPlaneDistance();
    var compensatedMinNearPlaneDistance = MIN_NEAR_PLANE_DISTANCE + tinCompensation;
    var compensatedMinFarPlaneDistance = MIN_FAR_PLANE_DISTANCE + tinCompensation;
    if (fogGettingDenser(currentTimeOfDay)) {
      var ticksSinceFogStart = (float) currentTimeOfDay - FOG_GETTING_DENSER;
      var newNearPlaneDistance = calculateFogDistance(ticksSinceFogStart, currentNearPlaneDistance, compensatedMinNearPlaneDistance);
      var newFarPlaneDistance = calculateFogDistance(ticksSinceFogStart, currentFarPlaneDistance, compensatedMinFarPlaneDistance);
      event.setNearPlaneDistance(newNearPlaneDistance);
      event.setFarPlaneDistance(newFarPlaneDistance);
    } else if (fogGettingThinner(currentTimeOfDay)) {
      var ticksSinceFogThinning = (float) currentTimeOfDay - FOG_STARTS_THINNING;
      var newNearPlaneDistance = calculateFogDistance(ticksSinceFogThinning, compensatedMinNearPlaneDistance, currentNearPlaneDistance);
      var newFarPlaneDistance = calculateFogDistance(ticksSinceFogThinning, compensatedMinFarPlaneDistance, currentFarPlaneDistance);
      event.setNearPlaneDistance(newNearPlaneDistance);
      event.setFarPlaneDistance(newFarPlaneDistance);
    } else {
      event.setNearPlaneDistance(compensatedMinNearPlaneDistance);
      event.setFarPlaneDistance(compensatedMinFarPlaneDistance);
    }
    event.setCanceled(true);
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

  private static boolean hadAncientMedal(Player player) {
    return Streams.stream(player.getAllSlots())
        .anyMatch(CosmereCraftItems.ANCIENT_MEDALLION.value().getDefaultInstance()::equals);
  }

  private static boolean notHoldingAncientMedallion(LocalPlayer localPlayer) {
    return Stream.of(
            localPlayer.getMainHandItem(),
            localPlayer.getOffhandItem())
        .noneMatch(itemStack -> itemStack.is(CosmereCraftItems.ANCIENT_MEDALLION));
  }

  private static boolean isFogTime(long timeOfDay) {
    return timeOfDay >= FOG_GETTING_DENSER || timeOfDay <= FOG_FINISHES_THINNING;
  }

  private static boolean fogGettingDenser(long timeOfDay) {
    return timeOfDay > FOG_GETTING_DENSER && timeOfDay < FOG_STOPS_GETTING_DENSER;
  }

  private static boolean fogGettingThinner(long timeOfDay) {
    return timeOfDay > FOG_STARTS_THINNING || timeOfDay < FOG_FINISHES_THINNING;
  }

  private static float calculateFogDistance(float elapsedTime, float start, float end) {
    return Mth.clamp(Mth.lerp(elapsedTime / FOG_TRANSITION_TIME, start, end), 0, Math.max(start, end));
  }
}
