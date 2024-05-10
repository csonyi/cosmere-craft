package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.registry.CosmereCraftAttachments.metalAvailable;
import static com.csonyi.cosmerecraft.registry.CosmereCraftAttachments.metalBurnState;
import static com.csonyi.cosmerecraft.registry.CosmereCraftAttachments.metalReserve;
import static java.util.function.Predicate.not;

import com.csonyi.cosmerecraft.Config;
import com.csonyi.cosmerecraft.networking.ClientMetalStateQueryHandler;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.util.StreamUtils;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class MetalStateManager {

  private final Player player;
  private final boolean isLocal;

  public MetalStateManager(Player player) {
    this.player = player;
    this.isLocal = player instanceof LocalPlayer;
    sync();
  }

  private void sync() {
    if (isLocal) {
      ClientMetalStateQueryHandler.queryMetalStatesFromServer();
    }
  }

  public void tick() {
    sync();
  }

  public void setStates(Set<AllomanticMetal.State> state) {
    state.forEach(this::setState);
  }

  public void setState(AllomanticMetal.State state) {
    state.getReserve()
        .ifPresent(reserve -> setReserve(state.metal(), reserve));
    state.getBurnState()
        .ifPresent(burnState -> setBurnState(state.metal(), burnState));
    state.isAvailable()
        .ifPresent(available -> setAvailable(state.metal(), available));
  }

  public void setReserve(AllomanticMetal metal, int amount) {
    player.setData(metalReserve(metal), amount);
  }

  public void setBurnState(AllomanticMetal metal, boolean state) {
    player.setData(metalBurnState(metal), state);
  }

  public void setAvailable(AllomanticMetal metal, boolean available) {
    player.setData(metalAvailable(metal), available);
  }

  public int getReserve(AllomanticMetal metal) {
    return player.getData(metalReserve(metal));
  }

  public boolean getBurnState(AllomanticMetal metal) {
    return player.getData(metalBurnState(metal));
  }

  public boolean getAvailable(AllomanticMetal metal) {
    return player.getData(metalAvailable(metal));
  }

  public void makeAvailable(AllomanticMetal metal) {
    setAvailable(metal, true);
  }

  public void drainActive() {
    AllomanticMetal.stream(this::isActive)
        .forEach(this::drain);
  }

  public boolean isAvailable(AllomanticMetal metal) {
    return metal.isGodMetal() || getAvailable(metal);
  }

  public boolean isActive(AllomanticMetal metal) {
    return isAvailable(metal)
        && getReserve(metal) > 0
        && getBurnState(metal);
  }

  public AllomanticMetal.State getState(AllomanticMetal metal) {
    return new AllomanticMetal.State(metal,
        getReserve(metal),
        getBurnState(metal),
        isAvailable(metal));
  }

  public Set<AllomanticMetal.State> getStates() {
    return metals()
        .map(this::getState)
        .collect(Collectors.toSet());
  }

  public boolean hasAllAttachments() {
    return AllomanticMetal.stream(not(AllomanticMetal::isGodMetal))
        .flatMap(metal -> Stream.of(
            CosmereCraftAttachments.metalBurnState(metal),
            CosmereCraftAttachments.metalReserve(metal),
            CosmereCraftAttachments.metalAvailable(metal)))
        .map(Supplier::get)
        .allMatch(player::hasData);
  }


  public boolean canIngest(int amount) {
    return Config.Server.collectiveAllomanticCapacity > getCollectiveMetalAmount() + amount;
  }

  @SafeVarargs
  public final Stream<AllomanticMetal> metals(Predicate<AllomanticMetal>... filters) {
    return AllomanticMetal.stream(
        StreamUtils.combinePredicates(filters),
        this::isAvailable);
  }

  public Stream<AllomanticMetal> activeMetals() {
    return metals()
        .filter(this::isActive);
  }

  protected int getCollectiveMetalAmount() {
    return AllomanticMetal.stream()
        .mapToInt(this::getReserve)
        .sum();
  }

  public void drain(AllomanticMetal metal) {
    var currentReserve = getReserve(metal);
    if (currentReserve <= 0) {
      return;
    }
    setReserve(metal, Mth.clamp(currentReserve - 1, 0, currentReserve));
  }

  public void ingest(AllomanticMetal metal, int amount) {
    if (AllomanticMetal.LERASIUM.equals(metal)) {
      AllomanticMetal.stream(not(AllomanticMetal::isGodMetal))
          .forEach(this::makeAvailable);
    }
    var newCollectiveReserve = getCollectiveMetalAmount() + amount;
    if (newCollectiveReserve > Config.Server.collectiveAllomanticCapacity) {
      return;
    }
    setReserve(metal, getReserve(metal) + amount);
  }

  public void copyFromOldPlayer(Player oldPlayer) {
    AllomanticMetal.stream()
        .forEach(metal -> setAvailable(metal, oldPlayer.getData(metalAvailable(metal))));
  }

}
