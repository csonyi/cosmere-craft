package com.csonyi.cosmerecraft.allomancy;

import static com.csonyi.cosmerecraft.CosmereCraftBlocks.ANCHOR_TAG;

import com.csonyi.cosmerecraft.Config;
import com.csonyi.cosmerecraft.capability.anchorobserver.AnchorObserverProvider;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class Allomancy {

  private static final Logger LOGGER = LogUtils.getLogger();

  public static void applySteelPush(Player player) {
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

  public static void scanForAnchors(Player player) {
    try (var level = player.level()) {
      var anchors = getAnchorsInRange(player.blockPosition(), level);
      AnchorObserverProvider.ifPlayerHasCapability(player, anchorObserver -> {
        anchors.forEach(anchor -> {
          Optional.ofNullable(anchor)
              .ifPresent(anchorObserver::learnAnchor);
        });
      });
    } catch (IOException e) {
      LOGGER.error("IOException while retrieving level in scanForAnchor: {}", e.getMessage());
    }
  }

  private static float calculatePushForce(BlockPos playerPos, BlockPos anchorPos) {
    var distanceSquared = playerPos.distSqr(anchorPos);
    var maxDistanceSquared = Math.pow(Config.Server.maxSteelPushDistance, 2);
    var distance = Math.min(distanceSquared, maxDistanceSquared);
    return (float) (0.3 * (1 - distance / maxDistanceSquared));
  }

  private static List<BlockPos> getAnchorsInRange(BlockPos playerPos, Level level) {
    var range = Config.Server.maxSteelPushDistance;
    var startX = playerPos.getX() - range;
    var startY = playerPos.getY() - range;
    var startZ = playerPos.getZ() - range;
    var endX = playerPos.getX() + range;
    var endY = playerPos.getY() + range;
    var endZ = playerPos.getZ() + range;
    var anchors = new ArrayList<BlockPos>();
    for (int x = startX; x < endX; x++) {
      for (int y = startY; y < endY; y++) {
        for (int z = startZ; z < endZ; z++) {
          var pos = new BlockPos(x, y, z);
          if (level.getBlockState(pos).getTags().anyMatch(ANCHOR_TAG::equals)) {
            anchors.add(pos);
          }
        }
      }
    }
    return anchors;
  }

}
