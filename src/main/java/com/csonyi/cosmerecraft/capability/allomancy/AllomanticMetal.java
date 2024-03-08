package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.Registry.attachmentTypes;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PULLING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Direction.PUSHING;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.EXTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Side.INTERNAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.ENHANCEMENT;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.MENTAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.PHYSICAL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.Type.TEMPORAL;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.attachment.AttachmentType;

public enum AllomanticMetal {

  STEEL(PHYSICAL, PUSHING, EXTERNAL),
  IRON(PHYSICAL, PULLING, EXTERNAL),
  ZINC(MENTAL, PULLING, EXTERNAL),
  BRASS(MENTAL, PUSHING, EXTERNAL),

  PEWTER(PHYSICAL, PUSHING, INTERNAL,
      MobEffects.MOVEMENT_SPEED,
      MobEffects.DIG_SPEED,
      MobEffects.DAMAGE_BOOST,
      MobEffects.REGENERATION,
      MobEffects.DAMAGE_RESISTANCE),
  TIN(PHYSICAL, PULLING, INTERNAL,
      MobEffects.NIGHT_VISION),
  COPPER(MENTAL, PULLING, INTERNAL),
  BRONZE(MENTAL, PUSHING, INTERNAL),

  DURALUMIN(ENHANCEMENT, PUSHING, INTERNAL),
  ALUMINUM(ENHANCEMENT, PULLING, INTERNAL),
  GOLD(TEMPORAL, PULLING, INTERNAL),
  ELECTRUM(TEMPORAL, PUSHING, INTERNAL),

  NICROSIL(ENHANCEMENT, PUSHING, EXTERNAL),
  CHROMIUM(ENHANCEMENT, PULLING, EXTERNAL),
  CADMIUM(TEMPORAL, PULLING, EXTERNAL),
  BENDALLOY(TEMPORAL, PUSHING, EXTERNAL),

  ATIUM, LERASIUM;

  public final Type type;
  public final Direction direction;
  public final Side side;

  public final int maxBurnStrength;
  public final Set<MobEffect> effects;

  public final Supplier<AttachmentType<Integer>> RESERVE = attachmentTypes().register(
      "%s_reserve".formatted(name().toLowerCase()),
      () -> AttachmentType.builder(() -> 0)
          .serialize(Codec.INT)
          .build());

  public final Supplier<AttachmentType<Integer>> BURN_STRENGTH = attachmentTypes().register(
      "%s_burn_strength".formatted(name().toLowerCase()),
      () -> AttachmentType.builder(() -> 0)
          .serialize(Codec.INT)
          .build());

  public final Supplier<AttachmentType<Boolean>> AVAILABLE = attachmentTypes().register(
      "%s_available".formatted(name().toLowerCase()),
      () -> AttachmentType.builder(() -> false)
          .serialize(Codec.BOOL)
          .copyOnDeath()
          .build());

  AllomanticMetal(Type type, Direction direction, Side side, MobEffect... effects) {
    this.type = type;
    this.direction = direction;
    this.side = side;
    this.maxBurnStrength = 4; // TODO: move to config
    this.effects = Set.of(effects);
  }

  AllomanticMetal() {
    this(Type.GOD, Direction.GOD, Side.GOD);
  }

  public boolean isGodMetal() {
    return Type.GOD.equals(type);
  }

  public Stream<MobEffect> getEffects() {
    return effects.stream();
  }

  public boolean hasEffect() {
    return !effects.isEmpty();
  }

  private String lowerCaseName() {
    return name().toLowerCase();
  }

  public ResourceLocation textureLocation() {
    return CosmereCraft.createResourceLocation("icon/metal/%s_icon".formatted(lowerCaseName()));
  }

  public String getTranslationKey() {
    return "cosmerecraft.metals.%s".formatted(lowerCaseName());
  }

  public static Stream<AllomanticMetal> stream() {
    return Arrays.stream(values());
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
