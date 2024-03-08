package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.Config;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class ExternalPhysicalMovement {
  
  private static final Logger LOGGER = LogUtils.getLogger();

  private final ServerPlayer player;

  public ExternalPhysicalMovement(ServerPlayer player) {
    this.player = player;
  }

  public void applySteelPush() {
    try (var level = player.level()) {
      var playerPos = player.blockPosition();
      var groundYLevel = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, playerPos);
      var pushForce = calculatePushForce(playerPos, groundYLevel);
      var sprintBoost = player.isSprinting() ? 0.5f : 0;
      var xImpulse = player.getDeltaMovement().x() * sprintBoost;
      var yImpulse = player.getDeltaMovement().y() * sprintBoost;
      player.moveRelative(pushForce, new Vec3(xImpulse, 1, yImpulse));
    } catch (IOException e) {
      LOGGER.error("IOException while retrieving level in applySteelPush: {}", e.getMessage());
    }
  }

  private static float calculatePushForce(BlockPos playerPos, BlockPos anchorPos) {
    var distanceSquared = playerPos.distSqr(anchorPos);
    var maxDistanceSquared = Math.pow(Config.Server.maxSteelPushDistance, 2);
    var distance = Math.min(distanceSquared, maxDistanceSquared);
    return (float) (0.3 * (1 - distance / maxDistanceSquared));
  }
}
