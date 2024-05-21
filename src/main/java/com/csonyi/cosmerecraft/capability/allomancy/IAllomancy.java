package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.registry.CosmereCraftCapabilities;
import java.util.Set;
import net.minecraft.world.entity.player.Player;

public interface IAllomancy {

  void tick();

  boolean isBurningAnyOf(AllomanticMetal... metal);

  boolean canIngestMetalAmount(int powerTicks);

  void ingestMetal(AllomanticMetal metal, int amount);

  Set<AllomanticMetal.State> getMetalStates();

  static IAllomancy of(Player player) {
    return CosmereCraftCapabilities.ALLOMANCY.getCapability(player, null);
  }

  void copyFrom(Player oldPlayer, boolean isDeath);
}
