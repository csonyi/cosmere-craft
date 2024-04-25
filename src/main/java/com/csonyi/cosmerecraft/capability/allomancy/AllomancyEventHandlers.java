package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.gui.AllomancyGui;
import com.csonyi.cosmerecraft.networking.WellLocationQueryHandler;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.csonyi.cosmerecraft.registry.CosmereCraftKeyMappings;
import com.csonyi.cosmerecraft.util.ErrorUtils;
import com.csonyi.cosmerecraft.util.MathUtils;
import java.io.IOException;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyEventHandlers {

  @SubscribeEvent
  public static void handleAllomancyUpdates(final LivingEvent.LivingTickEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
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
   * This listener is used to manipulate the player's field of view to lead them to a<br> Well Of Ascension (WOA) structure. It applies a
   * pulsating effect that changes depending on several factors:
   * <ul>
   *   <li>Whether or not there is an Ancient Medallion in the players inventory</li>
   *   <li>How far the player is from the closest WOA</li>
   *   <li>How precisely is the player looking in the direction of the closest WOA</li>
   * </ul>
   *
   * @param event The NeoForge event used to hook into the renderer.
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

  private static BlockPos getWellPosition(LocalPlayer localPlayer) {
    return localPlayer.getExistingData(CosmereCraftAttachments.TRACKED_WELL)
        .orElseGet(() -> {
          PacketDistributor.SERVER.noArg()
              .send(new WellLocationQueryHandler.WellLocationQuery(localPlayer.blockPosition()));
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
}
