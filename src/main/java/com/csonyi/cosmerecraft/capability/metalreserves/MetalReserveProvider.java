package com.csonyi.cosmerecraft.capability.metalreserves;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MetalReserveProvider implements ICapabilitySerializable<CompoundTag> {

  public static Capability<IMetalReserves> METAL_RESERVES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });

  private MetalReserves metalReserves = null;
  private final LazyOptional<IMetalReserves> metalReservesOptional = LazyOptional.of(this::createMetalReserve);

  public static void ifPlayerHasCapability(Player player, NonNullConsumer<IMetalReserves> runnable) {
    player.getCapability(METAL_RESERVES_CAPABILITY).ifPresent(runnable);
  }

  public static boolean hasCapability(Player player) {
    return player.getCapability(METAL_RESERVES_CAPABILITY).isPresent();
  }

  private MetalReserves createMetalReserve() {
    if (metalReserves == null) {
      metalReserves = new MetalReserves();
    }
    return metalReserves;
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
    if (capability == METAL_RESERVES_CAPABILITY) {
      return metalReservesOptional.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundTag serializeNBT() {
    return metalReserves.serializeNBT();
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    createMetalReserve().deserializeNBT(nbt);
  }
}
