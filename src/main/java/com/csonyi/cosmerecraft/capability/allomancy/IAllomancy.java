package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.registry.CosmereCraftCapabilities;
import java.util.Set;
import net.minecraft.world.entity.player.Player;

public interface IAllomancy {

  void tick();

  boolean isBurningAnyOf(AllomanticMetal... metals);

  boolean canIngestMetalAmount(int powerTicks);

  void ingestMetal(AllomanticMetal metal, int amount);

  Set<AllomanticMetal> getAvailableMetals();

  void setBurnState(AllomanticMetal metal, boolean state);

  boolean getBurnState(AllomanticMetal metal);

  int getReserve(AllomanticMetal metal);

  void setStates(Set<AllomanticMetal.State> state);

  Set<AllomanticMetal.State> getStates();

  void copyFrom(Player oldPlayer, boolean isDeath);

  static IAllomancy of(Player player) {
    return CosmereCraftCapabilities.ALLOMANCY.getCapability(player, null);
  }
}
