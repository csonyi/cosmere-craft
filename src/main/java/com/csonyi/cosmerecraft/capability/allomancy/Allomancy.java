package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.ALUMINUM;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.STEEL;

import com.csonyi.cosmerecraft.capability.anchors.AnchorObserver;
import com.csonyi.cosmerecraft.networking.ClientMetalStateQueryHandler;
import com.csonyi.cosmerecraft.networking.SteelJumpHandler;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.Lazy;

public class Allomancy implements IAllomancy {

  private static final int INFINITE_DURATION = -1;
  public static final int EXTRA_TICKS = 8;
  public static final float MOB_SLOW_FACTOR = 0.25f;

  private final ServerPlayer player;
  private final Lazy<MetalStateManager> lazyMetalStateManager;
  private final Lazy<AnchorObserver> lazyAnchorObserver;

  private Allomancy(Player player) {
    this.player = (ServerPlayer) player;
    this.lazyMetalStateManager = Lazy.of(() -> new MetalStateManager(this.player));
    this.lazyAnchorObserver = Lazy.of(() -> new AnchorObserver(this.player));
  }

  private MetalStateManager metalStateManager() {
    return lazyMetalStateManager.get();
  }

  private AnchorObserver anchorObserver() {
    return lazyAnchorObserver.get();
  }

  public static Allomancy register(Player player, Void nothing) {
    return player instanceof ServerPlayer serverPlayer
        ? new Allomancy(serverPlayer)
        : null;
  }

  public void ingestMetal(AllomanticMetal metal, int amount) {
    metalStateManager().ingest(metal, amount);
  }

  @Override
  public Set<AllomanticMetal.State> getMetalStates() {
    return metalStateManager().getMetalStates();
  }

  @Override
  public void copyFrom(Player oldPlayer, boolean isDeath) {
    metalStateManager().copyFrom(oldPlayer, isDeath);
  }

  private MobEffectInstance effectInstance(Holder<MobEffect> effect) {
    return new MobEffectInstance(
        effect,
        INFINITE_DURATION,
        0, true, true);
  }

  public boolean isBurningMetal() {
    return metalStateManager().isBurningMetal();
  }

  public boolean isBurningAnyOf(AllomanticMetal... metals) {
    return metalStateManager().isBurningAnyOf(metals);
  }

  @Override
  public boolean canIngestMetalAmount(int powerTicks) {
    return metalStateManager().canIngestMetalAmount(powerTicks);
  }

  public void tick() {
    if (!isBurningMetal()) {
      removeEffects(AllomanticMetal.allEffects());
      return;
    }
    if (metalStateManager().isActive(ALUMINUM)) {
      metalStateManager().emptyReserves();
      ClientMetalStateQueryHandler.wipeReservesOnClient(player);
    }
    if (metalStateManager().isActive(STEEL)) {
      if (Minecraft.getInstance().options.keyJump.isDown()) {
        player.resetFallDistance();
        if (anchorObserver().hasAnchorInRange()) {
          SteelJumpHandler.jump(player);
        }
      }
    }
    var isCadmiumActive = metalStateManager().isActive(AllomanticMetal.CADMIUM);
    var isBendalloyActive = metalStateManager().isActive(AllomanticMetal.BENDALLOY);
    if (isCadmiumActive ^ isBendalloyActive) {
      if (isCadmiumActive) {
        var level = (ServerLevel) player.level();
        level.setDayTime(Math.min(level.getDayTime() + 4, Long.MAX_VALUE));
      }
      if (isBendalloyActive) {
        var playerAABB = player.getBoundingBox();
        TickUtils.speedUpRandomTicks(
            (ServerLevel) player.level(),
            EXTRA_TICKS,
            playerAABB.inflate(4));
        TickUtils.speedUpBlockEntities(
            player.level(),
            EXTRA_TICKS,
            playerAABB.inflate(4));
        TickUtils.slowDownMobs(
            player.level(),
            MOB_SLOW_FACTOR,
            playerAABB.inflate(EXTRA_TICKS),
            playerAABB.inflate(2));
      }
    }
    applyNewMetalEffects();
    metalStateManager().drainActive();
    metalStateManager().emptyBurningMetals()
        .forEach(metal -> {
          metalStateManager().setBurnState(metal, false);
          ClientMetalStateQueryHandler.turnOffMetalOnClient(player, metal);
        });
  }

  private void removeEffects(Collection<Holder<MobEffect>> effects) {
    effects.forEach(player::removeEffect);
  }

  private void applyNewMetalEffects() {
    var currentMetalEffects = player.getActiveEffects().stream()
        .map(MobEffectInstance::getEffect)
        .filter(AllomanticMetal::isMetalEffect)
        .collect(Collectors.toSet());
    var activeMetalEffects = metalStateManager().activeMetals()
        .flatMap(AllomanticMetal::getEffects)
        .collect(Collectors.toSet());

    removeEffects(
        currentMetalEffects.stream()
            .filter(effect -> !activeMetalEffects.contains(effect))
            .collect(Collectors.toSet()));
    activeMetalEffects.stream()
        .filter(effect -> !currentMetalEffects.contains(effect))
        .map(this::effectInstance)
        .forEach(player::addEffect);
  }


}
