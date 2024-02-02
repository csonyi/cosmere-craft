package com.csonyi.cosmerecraft.capability.metalreserves;

import com.csonyi.cosmerecraft.Config;
import com.csonyi.cosmerecraft.allomancy.AllomanticMetal;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class MetalReserves implements IMetalReserves {

  protected EnumMap<AllomanticMetal, MetalReserveState> metalReserveStates = new EnumMap<>(AllomanticMetal.class);

  @Override
  public void receiveMetal(AllomanticMetal metal) {
    if (canReceiveMetal()) {
      addMetalReserveAmount(metal, Config.Server.metalPortionBurnTicks);
    }
  }

  @Override
  public void expendMetal(AllomanticMetal metal, int amount) {
    if (canExpendMetal(metal)) {
      addMetalReserveAmount(metal, -amount);
    }
  }

  @Override
  public int getMetalAmount(AllomanticMetal metal) {
    return metalReserveStates.getOrDefault(metal, MetalReserveState.defaultState()).amount;
  }

  @Override
  public boolean isMetalInUse(AllomanticMetal metal) {
    return metalReserveStates.getOrDefault(metal, MetalReserveState.defaultState()).isInUse;
  }

  @Override
  public boolean canReceiveMetal() {
    return Config.Server.collectiveAllomanticCapacity <= getCollectiveMetalAmount();
  }

  @Override
  public boolean canExpendMetal(AllomanticMetal metal) {
    return getMetalAmount(metal) > 0;
  }

  @Override
  public CompoundTag serializeNBT() {
    var rootTag = new CompoundTag();
    if (metalReserveStates == null) {
      return rootTag;
    }
    metalReserveStates.entrySet().stream()
        .filter(entry -> entry.getValue().amount > 0)
        .forEach(entry -> rootTag.put(entry.getKey().name(), entry.getValue().serializeNBT()));
    return rootTag;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    if (metalReserveStates == null) {
      metalReserveStates = new EnumMap<>(AllomanticMetal.class);
    }
    Arrays.stream(AllomanticMetal.values())
        .forEach(metal -> metalReserveStates.put(metal, MetalReserveState.fromNBT(nbt.getCompound(metal.name()))));
  }

  private int getCollectiveMetalAmount() {
    return metalReserveStates.values().stream()
        .map(metalReserveState -> metalReserveState.amount)
        .mapToInt(Integer::intValue)
        .sum();
  }

  private void addMetalReserveAmount(AllomanticMetal metal, int amount) {
    var metalReserveState = metalReserveStates.get(metal);
    metalReserveState.amount += amount;
    metalReserveStates.put(metal, metalReserveState);
  }

  @Override
  public Set<AllomanticMetal> getMetalsWithReserves() {
    return metalReserveStates.entrySet().stream()
        .filter(entry -> entry.getValue().amount > 0)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

  public void copyFrom(IMetalReserves metalReserves) {
    deserializeNBT(metalReserves.serializeNBT());
  }

  public static class MetalReserveState implements INBTSerializable<CompoundTag> {

    private int amount;
    private boolean isInUse;

    public MetalReserveState() {
      this.amount = 0;
      this.isInUse = false;
    }

    private static MetalReserveState defaultState() {
      return new MetalReserveState();
    }

    public static MetalReserveState fromNBT(CompoundTag nbt) {
      var metalReserveState = new MetalReserveState();
      metalReserveState.deserializeNBT(nbt);
      return metalReserveState;
    }

    @Override
    public CompoundTag serializeNBT() {
      var rootTag = new CompoundTag();
      rootTag.putInt("amount", amount);
      rootTag.putBoolean("isInUse", isInUse);
      return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
      amount = nbt.getInt("amount");
      isInUse = nbt.getBoolean("isInUse");
    }
  }
}
