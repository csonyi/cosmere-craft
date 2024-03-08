package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.Config;
import net.minecraft.world.entity.player.Player;

public class MetalStateManager {

  private final Player player;

  public MetalStateManager(Player player) {
    this.player = player;
  }

  public AllomanticMetal.State getState(AllomanticMetal metal) {
    return new AllomanticMetal.State(metal,
        getReserve(metal),
        getBurnStrength(metal),
        isAvailable(metal));
  }

  public void setState(AllomanticMetal.State state) {
    state.getReserve()
        .ifPresent(reserve -> setReserve(state.metal(), reserve));
    state.getBurnStrength()
        .ifPresent(burnStrength -> setBurnStrength(state.metal(), burnStrength));
    state.isAvailable()
        .ifPresent(available -> setAvailable(state.metal(), available));
  }

  public int getReserve(AllomanticMetal metal) {
    return player.getData(metal.RESERVE);
  }

  public void setReserve(AllomanticMetal metal, int amount) {
    player.setData(metal.RESERVE, amount);
  }

  public int getBurnStrength(AllomanticMetal metal) {
    return player.getData(metal.BURN_STRENGTH);
  }

  public void setBurnStrength(AllomanticMetal metal, int amount) {
    player.setData(metal.BURN_STRENGTH, amount);
  }

  public boolean isAvailable(AllomanticMetal metal) {
    return metal.isGodMetal() || player.getData(metal.AVAILABLE);
  }

  public void setAvailable(AllomanticMetal metal, boolean available) {
    player.setData(metal.AVAILABLE, available);
  }

  public boolean isActive(AllomanticMetal metal) {
    return getReserve(metal) > 0
        && getBurnStrength(metal) > 0
        && isAvailable(metal);
  }

  public void drainActive() {
    AllomanticMetal.stream()
        .filter(this::isActive)
        .forEach(this::drain);
  }

  public void drain(AllomanticMetal metal) {
    var currentReserve = getReserve(metal);
    if (currentReserve <= 0) {
      return;
    }
    var drain = currentReserve - metal.maxBurnStrength;
    setReserve(metal, Math.max(0, drain));
  }

  public void ingest(AllomanticMetal metal, int amount) {
    var newCollectiveReserve = getCollectiveMetalAmount() + amount;
    if (newCollectiveReserve > Config.Server.collectiveAllomanticCapacity) {
      return;
    }
    setReserve(metal, getReserve(metal) + amount);
  }

  public boolean canIngest(int amount) {
    return Config.Server.collectiveAllomanticCapacity > getCollectiveMetalAmount() + amount;
  }

  private int getCollectiveMetalAmount() {
    return AllomanticMetal.stream()
        .mapToInt(this::getReserve)
        .sum();
  }

}
