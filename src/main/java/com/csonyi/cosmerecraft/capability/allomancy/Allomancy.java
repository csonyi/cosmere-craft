package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.STEEL;

import com.csonyi.cosmerecraft.capability.anchorobserver.AnchorObserver;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class Allomancy {

  private static final int EFFECT_DURATION = TickUtils.secondsToTicks(4);

  private final ServerPlayer player;
  private final ExternalPhysicalMovement externalPhysicalMovement;
  private final MetalStateManager metalStateManager;
  private final AnchorObserver anchorObserver;
  private final TickUtils tickUtils;

  public Allomancy(Player player) {
    this.player = (ServerPlayer) player;
    this.metalStateManager = new MetalStateManager(this.player);
    this.externalPhysicalMovement = new ExternalPhysicalMovement(this.player);
    this.anchorObserver = new AnchorObserver(this.player);
    this.tickUtils = TickUtils.forLevel(this.player.level());
  }

  public static Allomancy of(Player player) {
    return new Allomancy(player);
  }

  public boolean canIngestMetal(int amount) {
    return metalStateManager.canIngest(amount);
  }

  public void ingestMetal(AllomanticMetal metal, int amount) {
    metalStateManager.ingest(metal, amount);
  }

  private void applyActiveMetalEffects() {
    AllomanticMetal.stream()
        .filter(AllomanticMetal::hasEffect)
        .filter(metalStateManager::isActive)
        .map(this::metalEffects)
        .flatMap(Set::stream)
        .forEach(player::addEffect);
  }

  private Set<MobEffectInstance> metalEffects(AllomanticMetal metal) {
    return metal.getEffects()
        .map(effect -> new MobEffectInstance(
            effect,
            EFFECT_DURATION,
            metalStateManager.getBurnStrength(metal),
            true, true))
        .collect(Collectors.toSet());
  }

  public void tick() {
    if (tickUtils.everyNthSecond(8)) {
      anchorObserver.scan();
    }
    if (metalStateManager.isActive(STEEL) && anchorObserver.isAnchorInRange()) {
      externalPhysicalMovement.applySteelPush();
    }
    applyActiveMetalEffects();
    metalStateManager.drainActive();
  }
}
