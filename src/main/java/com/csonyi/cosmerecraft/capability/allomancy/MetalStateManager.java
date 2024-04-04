package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.registry.CosmereCraftAttachments.metalAvailable;
import static com.csonyi.cosmerecraft.registry.CosmereCraftAttachments.metalBurnStrength;
import static com.csonyi.cosmerecraft.registry.CosmereCraftAttachments.metalReserve;

import com.csonyi.cosmerecraft.Config;
import java.util.function.Predicate;
import java.util.stream.Stream;
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
    return player.getData(metalReserve(metal));
  }

  public void setReserve(AllomanticMetal metal, int amount) {
    player.setData(metalReserve(metal), amount);
  }

  public int getBurnStrength(AllomanticMetal metal) {
    return player.getData(metalBurnStrength(metal));
  }

  public void setBurnStrength(AllomanticMetal metal, int amount) {
    player.setData(metalBurnStrength(metal), amount);
  }

  public boolean isAvailable(AllomanticMetal metal) {
    return metal.isGodMetal() || player.getData(metalAvailable(metal));
  }

  public void setAvailable(AllomanticMetal metal, boolean available) {
    player.setData(metalAvailable(metal), available);
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

  public Stream<AllomanticMetal> metals() {
    return AllomanticMetal.stream()
        .filter(Predicate.not(AllomanticMetal::isGodMetal))
        .filter(this::isAvailable);
  }

  public Stream<AllomanticMetal> activeMetals() {
    return metals()
        .filter(this::isActive);
  }

  private int getCollectiveMetalAmount() {
    return AllomanticMetal.stream()
        .mapToInt(this::getReserve)
        .sum();
  }

}
