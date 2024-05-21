package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PULLING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PUSHING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.EXTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.INTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.ENHANCEMENT;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.MENTAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.PHYSICAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.TEMPORAL;

import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.csonyi.cosmerecraft.util.StreamUtils;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum AllomanticMetal {

  STEEL(PHYSICAL, PUSHING, EXTERNAL, false, true),
  IRON(PHYSICAL, PULLING, EXTERNAL, true, false),
  ZINC(MENTAL, PULLING, EXTERNAL),
  BRASS(MENTAL, PUSHING, EXTERNAL, false, true),

  PEWTER(PHYSICAL, PUSHING, INTERNAL,
      false, true,
      MobEffects.MOVEMENT_SPEED,
      MobEffects.DIG_SPEED,
      MobEffects.DAMAGE_BOOST,
      MobEffects.REGENERATION,
      MobEffects.DAMAGE_RESISTANCE,
      MobEffects.JUMP),
  TIN(PHYSICAL, PULLING, INTERNAL,
      MobEffects.NIGHT_VISION),
  COPPER(MENTAL, PULLING, INTERNAL, true, false),
  BRONZE(MENTAL, PUSHING, INTERNAL, false, true),

  DURALUMIN(ENHANCEMENT, PUSHING, INTERNAL, false, true),
  ALUMINUM(ENHANCEMENT, PULLING, INTERNAL),
  GOLD(TEMPORAL, PULLING, INTERNAL, true, false),
  ELECTRUM(TEMPORAL, PUSHING, INTERNAL, false, true),

  NICROSIL(ENHANCEMENT, PUSHING, EXTERNAL, false, true),
  CHROMIUM(ENHANCEMENT, PULLING, EXTERNAL),
  CADMIUM(TEMPORAL, PULLING, EXTERNAL),
  BENDALLOY(TEMPORAL, PUSHING, EXTERNAL, false, true),

  ATIUM, LERASIUM;

  public static final StreamCodec<FriendlyByteBuf, AllomanticMetal> CODEC = NeoForgeStreamCodecs.enumCodec(AllomanticMetal.class);

  public final Type type;
  public final Direction direction;
  public final Side side;

  public final boolean isVanilla;
  public final boolean isAlloy;
  public final int tickDrain;
  public final HolderSet<MobEffect> effects;

  @SafeVarargs
  AllomanticMetal(
      Type type, Direction direction, Side side,
      boolean isVanilla, boolean isAlloy,
      int tickDrain, Holder<MobEffect>... effects) {
    this.type = type;
    this.direction = direction;
    this.side = side;
    this.isVanilla = isVanilla;
    this.isAlloy = isAlloy;
    this.tickDrain = tickDrain;
    this.effects = HolderSet.direct(effects);
  }

  @SafeVarargs
  AllomanticMetal(
      Type type, Direction direction, Side side,
      boolean isVanilla, boolean isAlloy,
      Holder<MobEffect>... effects) {
    this(type, direction, side, isVanilla, isAlloy, 1, effects);
  }

  @SafeVarargs
  AllomanticMetal(Type type, Direction direction, Side side, int tickDrain, Holder<MobEffect>... effects) {
    this(type, direction, side, false, false, tickDrain, effects);
  }

  @SafeVarargs
  AllomanticMetal(Type type, Direction direction, Side side, Holder<MobEffect>... effects) {
    this(type, direction, side, 1, effects);
  }

  AllomanticMetal() {
    this(Type.GOD, Direction.GOD, Side.GOD, false, false, 0);
  }

  public boolean isGodMetal() {
    return Type.GOD.equals(type);
  }

  public boolean isVanilla() {
    return isVanilla;
  }

  public boolean isAlloy() {
    return isAlloy;
  }

  public Stream<Holder<MobEffect>> getEffects() {
    return effects.stream();
  }

  public boolean hasEffect() {
    return getEffects()
        .findAny()
        .isPresent();
  }

  public String lowerCaseName() {
    return name().toLowerCase();
  }

  public ResourceLocation texture(String color) {
    return ResourceUtils.modLocation("textures/icon/metal/%s_icon_%s.png".formatted(lowerCaseName(), color));
  }

  public Component getNameAsComponent() {
    return Component.translatable("cosmerecraft.metals.%s".formatted(lowerCaseName()));
  }

  public int getTableXIndex() {
    return ordinal() % 4;
  }

  public int getTableYIndex() {
    return ordinal() / 4;
  }

  @SafeVarargs
  public static Stream<AllomanticMetal> stream(Predicate<AllomanticMetal>... filters) {
    return Arrays.stream(values())
        .filter(StreamUtils.combinePredicates(filters));
  }

  public static Set<String> names() {
    return stream()
        .map(AllomanticMetal::lowerCaseName)
        .collect(Collectors.toSet());
  }

  public enum Type {
    PHYSICAL, MENTAL,
    ENHANCEMENT, TEMPORAL,
    GOD;

    public boolean of(AllomanticMetal metal) {
      return this.equals(metal.type);
    }

    public Component getNameAsComponent() {
      return Component.translatable("cosmerecraft.metals.type.%s".formatted(name().toLowerCase()));
    }
  }

  public enum Direction {
    PUSHING, PULLING, GOD
  }

  public enum Side {
    INTERNAL, EXTERNAL, GOD
  }

  public record State(AllomanticMetal metal, Integer reserve, Boolean burnState, Boolean available) {

    public static final StreamCodec<FriendlyByteBuf, State> CODEC = StreamCodec.composite(
        AllomanticMetal.CODEC, State::metal,
        ByteBufCodecs.INT, State::reserve,
        ByteBufCodecs.BOOL, State::burnState,
        ByteBufCodecs.BOOL, State::available,
        State::new);

    public Optional<Integer> getReserve() {
      return Optional.ofNullable(reserve);
    }

    public Optional<Boolean> getBurnState() {
      return Optional.ofNullable(burnState);
    }

    public Optional<Boolean> isAvailable() {
      return Optional.ofNullable(available);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof State state) {
        return metal.equals(state.metal)
            && (reserve == null || reserve.equals(state.reserve))
            && (burnState == null || burnState.equals(state.burnState))
            && (available == null || available.equals(state.available));
      }
      return false;
    }
  }

}
