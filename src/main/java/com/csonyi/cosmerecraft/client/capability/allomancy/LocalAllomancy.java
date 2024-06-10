package com.csonyi.cosmerecraft.client.capability.allomancy;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.ExternalPhysicalMovement;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.capability.anchors.AnchorObserver;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class LocalAllomancy implements IAllomancy {

  private static final Minecraft MC = Minecraft.getInstance();

  private final LocalPlayer localPlayer;
  private final ExternalPhysicalMovement externalPhysicalMovement;
  private final MetalStateManager metalStateManager;
  private final AnchorObserver anchorObserver;


  private LocalAllomancy(Player player) {
    this.localPlayer = (LocalPlayer) player;
    this.externalPhysicalMovement = new ExternalPhysicalMovement(localPlayer);
    this.metalStateManager = new MetalStateManager(localPlayer);
    this.anchorObserver = new AnchorObserver(localPlayer);
  }

  public static LocalAllomancy register(Player player, Void nothing) {
    return player instanceof LocalPlayer localPlayer
        ? new LocalAllomancy(localPlayer)
        : null;
  }

  @Override
  public void tick() {
    if (metalStateManager.isActive(AllomanticMetal.STEEL)) {
      if (isHoldingJump() && anchorObserver.hasAnchorInRange()) {
        localPlayer.resetFallDistance();
        externalPhysicalMovement.applySteelPush();
      }
    }
    metalStateManager.drainActive();
    metalStateManager.emptyBurningMetals()
        .forEach(metal -> metalStateManager.setBurnState(metal, false));
  }

  @Override
  public boolean isBurningAnyOf(AllomanticMetal... metals) {
    return metalStateManager.isBurningAnyOf(metals);
  }

  @Override
  public boolean canIngestMetalAmount(int powerTicks) {
    return metalStateManager.canIngestMetalAmount(powerTicks);
  }

  @Override
  public void ingestMetal(AllomanticMetal metal, int amount) {
    metalStateManager.ingest(metal, amount);
  }

  @Override
  public Set<AllomanticMetal> getAvailableMetals() {
    return metalStateManager.availableMetals()
        .collect(Collectors.toSet());
  }

  @Override
  public void setBurnState(AllomanticMetal metal, boolean state) {
    metalStateManager.setBurnState(metal, state);
  }

  @Override
  public boolean getBurnState(AllomanticMetal metal) {
    return metalStateManager.getBurnState(metal);
  }

  @Override
  public int getReserve(AllomanticMetal metal) {
    return metalStateManager.getReserve(metal);
  }

  @Override
  public void setStates(Set<AllomanticMetal.State> state) {
    metalStateManager.setStates(state);
  }

  @Override
  public Set<AllomanticMetal.State> getStates() {
    return metalStateManager.getMetalStates();
  }

  @Override
  public void copyFrom(Player oldPlayer, boolean isDeath) {
    metalStateManager.copyFrom(oldPlayer, isDeath);
  }

  private static boolean isHoldingJump() {
    return MC.options.keyJump.isDown();
  }
}
