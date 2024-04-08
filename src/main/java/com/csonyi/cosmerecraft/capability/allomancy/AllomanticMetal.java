package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PULLING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PUSHING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.EXTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.INTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.ENHANCEMENT;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.MENTAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.PHYSICAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.TEMPORAL;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
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
      MobEffects.DAMAGE_RESISTANCE),
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

  public final int maxBurnStrength;
  public final boolean isVanilla;
  public final boolean isAlloy;
  public final Set<MobEffect> effects;

  AllomanticMetal(Type type, Direction direction, Side side, boolean isVanilla, boolean isAlloy, MobEffect... effects) {
    this.type = type;
    this.direction = direction;
    this.side = side;
    this.maxBurnStrength = 4; // TODO: move to config
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

  public ResourceLocation textureLocation() {
    return CosmereCraft.createResourceLocation("textures/icon/metal/%s_icon.png".formatted(lowerCaseName()));
  }

  public String getTranslationKey() {
    return "cosmerecraft.metals.%s".formatted(lowerCaseName());
  }

  public static Stream<AllomanticMetal> stream() {
    return Arrays.stream(values());
  }

  public static Stream<AllomanticMetal> stream(Predicate<AllomanticMetal> filter) {
    return stream().filter(filter);
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

  public record State(AllomanticMetal metal, Integer reserve, Integer burnStrength, Boolean available) {

    public void write(FriendlyByteBuf buffer) {
      buffer.writeEnum(metal);
      buffer.writeInt(reserve);
      buffer.writeInt(burnStrength);
      buffer.writeBoolean(available);
    }

    public static State read(FriendlyByteBuf buffer) {
      return new State(
          buffer.readEnum(AllomanticMetal.class),
          buffer.readInt(),
          buffer.readInt(),
          buffer.readBoolean());
    }

    public static State defaultState(AllomanticMetal metal) {
      return new State(metal, 0, 0, false);
    }

    public Optional<Integer> getReserve() {
      return Optional.ofNullable(reserve);
    }

    public Optional<Integer> getBurnStrength() {
      return Optional.ofNullable(burnStrength);
    }

    public Optional<Boolean> isAvailable() {
      return Optional.ofNullable(available);
    }
  }

}
