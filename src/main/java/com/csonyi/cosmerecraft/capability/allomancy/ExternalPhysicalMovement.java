package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.Config;
import com.csonyi.cosmerecraft.capability.anchors.AnchorObserver;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.Lazy;

public class ExternalPhysicalMovement {

  private final Player player;
  private final Lazy<AnchorObserver> lazyAnchorObserver;

  public ExternalPhysicalMovement(Player player) {
    this.player = player;
    this.lazyAnchorObserver = Lazy.of(() -> new AnchorObserver(this.player));
  }

  public void applySteelPush() {
    var playerPos = player.blockPosition();
    getClosestAnchorBelowPlayer().ifPresent(closestAnchorBelow -> {
      var pushForce = calculateImpulseStrength(playerPos, closestAnchorBelow);
      player.moveRelative(pushForce, new Vec3(player.xxa, 1, player.zza));
    });
  }

  private static float calculateImpulseStrength(BlockPos playerPos, BlockPos anchorPos) {
    var distanceSquared = playerPos.distSqr(anchorPos);
    var maxDistanceSquared = Math.pow(Config.Server.maxSteelPushDistance, 2);
    var distance = Math.min(distanceSquared, maxDistanceSquared);
    return (float) (0.3 * (1 - distance / maxDistanceSquared));
  }

  private Optional<BlockPos> getClosestAnchorBelowPlayer() {
    var anchorObserver = this.lazyAnchorObserver.get();
    return anchorObserver.getAnchorsInRange().stream()
        .filter(anchorPos -> anchorPos.getY() < player.getY()) // anchors under the player
        .min((anchor1, anchor2) -> {
          var distance1 = player.distanceToSqr(Vec3.atCenterOf(anchor1));
          var distance2 = player.distanceToSqr(Vec3.atCenterOf(anchor2));
          return Double.compare(distance1, distance2);
        });
  }
}
