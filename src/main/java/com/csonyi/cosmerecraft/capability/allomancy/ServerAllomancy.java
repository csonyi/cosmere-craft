package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.ALUMINUM;

import com.csonyi.cosmerecraft.networking.ClientMetalStateQueryHandler;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class ServerAllomancy implements IAllomancy {

  private static final int INFINITE_DURATION = -1;
  public static final int EXTRA_TICKS = 8;
  public static final float MOB_SLOW_FACTOR = 0.25f;

  private final ServerPlayer serverPlayer;
  private final MetalStateManager metalStateManager;

  private ServerAllomancy(Player serverPlayer) {
    this.serverPlayer = (ServerPlayer) serverPlayer;
    this.metalStateManager = new MetalStateManager(this.serverPlayer);
  }

  public static ServerAllomancy register(Player player, Void nothing) {
    return player instanceof ServerPlayer serverPlayer
        ? new ServerAllomancy(serverPlayer)
        : null;
  }

  public void tick() {
    if (!metalStateManager.isBurningMetal()) {
      removeEffects(AllomanticMetal.allEffects());
      return;
    }
    if (metalStateManager.isActive(ALUMINUM)) {
      metalStateManager.emptyReserves();
      ClientMetalStateQueryHandler.wipeReservesOnClient(serverPlayer);
    }
    var isCadmiumActive = metalStateManager.isActive(AllomanticMetal.CADMIUM);
    var isBendalloyActive = metalStateManager.isActive(AllomanticMetal.BENDALLOY);
    if (isCadmiumActive ^ isBendalloyActive) {
      if (isCadmiumActive) {
        var level = (ServerLevel) serverPlayer.level();
        var newDayTime = (level.getDayTime() + 40) % 24_000;
        level.setDayTime(newDayTime);
      }
      if (isBendalloyActive) {
        var playerAABB = serverPlayer.getBoundingBox();
        TickUtils.speedUpRandomTicks(
            (ServerLevel) serverPlayer.level(),
            EXTRA_TICKS,
            playerAABB.inflate(4));
        TickUtils.speedUpBlockEntities(
            serverPlayer.level(),
            EXTRA_TICKS,
            playerAABB.inflate(4));
        TickUtils.slowDownMobs(
            serverPlayer.level(),
            MOB_SLOW_FACTOR,
            playerAABB.inflate(EXTRA_TICKS),
            playerAABB.inflate(2));
      }
    }
    applyNewMetalEffects();
    metalStateManager.drainActive();
    metalStateManager.emptyBurningMetals()
        .forEach(metal -> {
          metalStateManager.setBurnState(metal, false);
          // ClientMetalStateQueryHandler.turnOffMetalOnClient(serverPlayer, metal);
        });
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
  public void copyFrom(Player oldPlayer, boolean isDeath) {
    metalStateManager.copyFrom(oldPlayer, isDeath);
  }

  @Override
  public void setStates(Set<AllomanticMetal.State> state) {
    metalStateManager.setStates(state);
  }

  @Override
  public Set<AllomanticMetal.State> getStates() {
    return metalStateManager.getMetalStates();
  }

  private MobEffectInstance effectInstance(Holder<MobEffect> effect) {
    return new MobEffectInstance(
        effect,
        INFINITE_DURATION,
        0, true, true);
  }

  private void applyNewMetalEffects() {
    var currentMetalEffects = serverPlayer.getActiveEffects().stream()
        .map(MobEffectInstance::getEffect)
        .filter(AllomanticMetal::isMetalEffect)
        .collect(Collectors.toSet());
    var activeMetalEffects = metalStateManager.activeMetals()
        .flatMap(AllomanticMetal::getEffects)
        .collect(Collectors.toSet());

    removeEffects(
        currentMetalEffects.stream()
            .filter(effect -> !activeMetalEffects.contains(effect))
            .collect(Collectors.toSet()));
    activeMetalEffects.stream()
        .filter(effect -> !currentMetalEffects.contains(effect))
        .map(this::effectInstance)
        .forEach(serverPlayer::addEffect);
  }

  private void removeEffects(Collection<Holder<MobEffect>> effects) {
    effects.forEach(serverPlayer::removeEffect);
  }
}
