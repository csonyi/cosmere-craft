package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.mojang.serialization.Codec;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CosmereCraftAttachments {

  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

  public static final Supplier<AttachmentType<HashSet<BlockPos>>> KNOWN_ANCHORS = ATTACHMENT_TYPES.register(
      "known_anchors",
      () -> AttachmentType.builder(() -> new HashSet<BlockPos>()).build());

  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Integer>>> METAL_RESERVES =
      new EnumMap<>(AllomanticMetal.class);
  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Integer>>> METAL_BURN_STRENGTH =
      new EnumMap<>(AllomanticMetal.class);
  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Boolean>>> METAL_AVAILABLE =
      new EnumMap<>(AllomanticMetal.class);

  static {
    AllomanticMetal.stream()
        .forEach(metal -> {
          METAL_RESERVES.put(metal, registerIntegerAttachment(metal, "reserve"));
          METAL_BURN_STRENGTH.put(metal, registerIntegerAttachment(metal, "burn_strength"));
          METAL_AVAILABLE.put(metal, registerBooleanAttachment(metal, "available"));
        });
  }

  public static Supplier<AttachmentType<Integer>> metalReserve(AllomanticMetal metal) {
    return METAL_RESERVES.get(metal);
  }

  public static Supplier<AttachmentType<Integer>> metalBurnStrength(AllomanticMetal metal) {
    return METAL_BURN_STRENGTH.get(metal);
  }

  public static Supplier<AttachmentType<Boolean>> metalAvailable(AllomanticMetal metal) {
    return METAL_AVAILABLE.get(metal);
  }

  private static Supplier<AttachmentType<Boolean>> registerBooleanAttachment(AllomanticMetal metal, String attachmentSuffix) {
    return ATTACHMENT_TYPES.register(
        "%s_%s".formatted(metal.name().toLowerCase(), attachmentSuffix),
        () -> AttachmentType.builder(() -> false)
            .serialize(Codec.BOOL)
            .build());
  }

  private static Supplier<AttachmentType<Integer>> registerIntegerAttachment(AllomanticMetal metal, String attachmentSuffix) {
    return ATTACHMENT_TYPES.register(
        "%s_%s".formatted(metal.name().toLowerCase(), attachmentSuffix),
        () -> AttachmentType.builder(() -> 0)
            .serialize(Codec.INT)
            .build());
  }

  public static void register(IEventBus modEventBus) {
    ATTACHMENT_TYPES.register(modEventBus);
  }
}
