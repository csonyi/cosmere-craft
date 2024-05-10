package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PULLING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PUSHING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.EXTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.INTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.ENHANCEMENT;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.MENTAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.PHYSICAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.TEMPORAL;

import com.csonyi.cosmerecraft.util.ColorUtils;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.csonyi.cosmerecraft.util.StreamUtils;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

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

  public final Type type;
  public final Direction direction;
  public final Side side;

  public final boolean isVanilla;
  public final boolean isAlloy;
  public final Set<MobEffect> effects;

  AllomanticMetal(Type type, Direction direction, Side side, boolean isVanilla, boolean isAlloy, MobEffect... effects) {
    this.type = type;
    this.direction = direction;
    this.side = side;
    this.isVanilla = isVanilla;
    this.isAlloy = isAlloy;
    this.effects = Set.of(effects);
  }

  AllomanticMetal(Type type, Direction direction, Side side, MobEffect... effects) {
    this(type, direction, side, false, false, effects);
  }

  AllomanticMetal() {
    this(Type.GOD, Direction.GOD, Side.GOD, false, false);
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

  public Stream<MobEffect> getEffects() {
    return effects.stream();
  }

  public boolean hasEffect() {
    return !effects.isEmpty();
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

  public float[] getColor() {
    return ColorUtils.getColor(ColorUtils.ALLOMANTIC_METALS[ordinal()]);
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

  public static AllomanticMetal read(FriendlyByteBuf buffer) {
    return buffer.readEnum(AllomanticMetal.class);
  }

  public static Set<MobEffect> allMetalEffects() {
    return stream(AllomanticMetal::hasEffect)
        .flatMap(AllomanticMetal::getEffects)
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

  public enum MetalAmount {
    NUGGET(TickUtils.minutesToTicks(16) / 9), VIAL(TickUtils.minutesToTicks(16));

    MetalAmount(int amount) {
      this.amount = amount;
    }

    public final int amount;
  }

  public record State(AllomanticMetal metal, Integer reserve, Boolean burnState, Boolean available) {

    public void write(FriendlyByteBuf buffer) {
      buffer.writeEnum(metal);
      buffer.writeInt(reserve);
      buffer.writeBoolean(burnState);
      buffer.writeBoolean(available);
    }

    public static State read(FriendlyByteBuf buffer) {
      return new State(
          buffer.readEnum(AllomanticMetal.class),
          buffer.readInt(),
          buffer.readBoolean(),
          buffer.readBoolean());
    }

    public static State defaultState(AllomanticMetal metal) {
      return new State(metal, 0, false, false);
    }

    public static State burnStateUpdate(AllomanticMetal metal) {
      return new State(metal, null, true, null);
    }

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
