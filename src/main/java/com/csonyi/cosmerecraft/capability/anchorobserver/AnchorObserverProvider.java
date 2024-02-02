package com.csonyi.cosmerecraft.capability.anchorobserver;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnchorObserverProvider implements ICapabilitySerializable<ListTag> {

  public static Capability<IAnchorObserver> ANCHOR_OBSERVER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });
  private AnchorObserver anchorObserver = null;
  private final LazyOptional<IAnchorObserver> anchorObserverOptional = LazyOptional.of(this::createAnchorObserver);

  public static void ifPlayerHasCapability(Player player, NonNullConsumer<IAnchorObserver> runnable) {
    player.getCapability(ANCHOR_OBSERVER_CAPABILITY).ifPresent(runnable);
  }

  public static boolean hasCapability(Player player) {
    return player.getCapability(ANCHOR_OBSERVER_CAPABILITY).isPresent();
  }

  private AnchorObserver createAnchorObserver() {
    if (anchorObserver == null) {
      anchorObserver = new AnchorObserver();
    }
    return anchorObserver;
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
    if (capability == ANCHOR_OBSERVER_CAPABILITY) {
      return anchorObserverOptional.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public ListTag serializeNBT() {
    return anchorObserver.serializeNBT();
  }

  @Override
  public void deserializeNBT(ListTag listTag) {
    createAnchorObserver().deserializeNBT(listTag);
  }
}
