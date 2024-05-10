package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.STEEL;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.MetalAmount;
import com.csonyi.cosmerecraft.capability.anchors.AnchorObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.Lazy;
import org.apache.commons.lang3.ArrayUtils;

public class Allomancy {

  private static final int EFFECT_DURATION = -1;

  private static final Map<UUID, Allomancy> INSTANCES = new HashMap<>();

  private final Player player;
  private final Lazy<ExternalPhysicalMovement> externalPhysicalMovement;
  private final Lazy<MetalStateManager> metalStateManager;
  private final Lazy<AnchorObserver> anchorObserver;

  private Allomancy(Player player) {
    this.player = player;
    this.metalStateManager = Lazy.of(() -> new MetalStateManager(this.player));
    this.externalPhysicalMovement = Lazy.of(() -> new ExternalPhysicalMovement(this.player));
    this.anchorObserver = Lazy.of(() -> new AnchorObserver(this.player));
  }

  private ExternalPhysicalMovement externalPhysicalMovement() {
    return externalPhysicalMovement.get();
  }

  private MetalStateManager metalStateManager() {
    return metalStateManager.get();
  }

  private AnchorObserver anchorObserver() {
    return anchorObserver.get();
  }

  public static Allomancy of(Player player) {
    return INSTANCES.computeIfAbsent(
        player.getUUID(),
        uuid -> new Allomancy(player));
  }

  public boolean canIngestMetal(int amount) {
    return metalStateManager().canIngest(amount);
  }

  public void ingestMetal(AllomanticMetal metal, MetalAmount metalAmount) {
    ingestMetal(metal, metalAmount.amount);
  }

  public void ingestMetal(AllomanticMetal metal, int amount) {
    metalStateManager().ingest(metal, amount);
  }

  private void applyActiveMetalEffects() {
    var currentEffects = player.getActiveEffects().stream()
        .map(MobEffectInstance::getEffect)
        .collect(Collectors.toSet());
    var activeEffects = AllomanticMetal.stream(
            AllomanticMetal::hasEffect,
            metalStateManager()::isActive)
        .flatMap(AllomanticMetal::getEffects)
        .collect(Collectors.toSet());
    currentEffects.stream()
        .filter(effect -> !activeEffects.contains(effect))
        .forEach(player::removeEffect);
    activeEffects.stream()
        .filter(effect -> !currentEffects.contains(effect))
        .map(this::effectInstance)
        .forEach(player::addEffect);

  }

  private MobEffectInstance effectInstance(MobEffect effect) {
    return new MobEffectInstance(
        effect,
        EFFECT_DURATION,
        0, true, true);
  }

  public boolean isBurningMetal() {
    return metalStateManager().activeMetals()
        .findAny()
        .isPresent();
  }

  public boolean isBurningAnyOf(AllomanticMetal... metals) {
    return metalStateManager().activeMetals()
        .anyMatch(metal -> ArrayUtils.contains(metals, metal));
  }

  public void tick() {

    if (metalStateManager().isActive(STEEL)) {
      if (player.jumping) {
        player.resetFallDistance();
        if (anchorObserver().hasAnchorInRange()) {
          externalPhysicalMovement().applySteelPush();
        }
      }
    }
    applyActiveMetalEffects();
    metalStateManager().drainActive();
  }
}
