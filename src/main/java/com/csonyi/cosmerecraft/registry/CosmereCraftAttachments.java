package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.anchors.ChunkAnchors;
import com.mojang.serialization.Codec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CosmereCraftAttachments {

  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

  public static final Supplier<AttachmentType<List<BlockPos>>> CHUNK_ANCHORS =
      ATTACHMENT_TYPES.register(
          "chunk_anchors",
          () -> AttachmentType.<List<BlockPos>>builder(() -> new ArrayList<>())
              .serialize(ChunkAnchors.CODEC)
              .build());

  public static final Supplier<AttachmentType<BlockPos>> TRACKED_WELL =
      ATTACHMENT_TYPES.register(
          "tracked_well",
          () -> AttachmentType.builder(() -> BlockPos.ZERO)
              .build());

  public static final Supplier<AttachmentType<Instant>> FOUND_MEDALLION =
      ATTACHMENT_TYPES.register(
          "found_medallion",
          () -> AttachmentType.builder(() -> Instant.EPOCH)
              .serialize(Codec.LONG.xmap(Instant::ofEpochMilli, Instant::toEpochMilli))
              .build());

  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Integer>>> METAL_RESERVES =
      new EnumMap<>(AllomanticMetal.class);
  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Boolean>>> METAL_BURN_STATE =
      new EnumMap<>(AllomanticMetal.class);
  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Boolean>>> METAL_AVAILABLE =
      new EnumMap<>(AllomanticMetal.class);

  static {
    AllomanticMetal.stream()
        .forEach(metal -> {
          METAL_RESERVES.put(metal, registerIntegerAttachmentForMetal(metal, "reserve"));
          METAL_BURN_STATE.put(metal, registerBooleanAttachmentForMetal(metal, "burn_state"));
          METAL_AVAILABLE.put(metal, registerBooleanAttachmentForMetal(metal, "available", true));
        });
  }

  public static Supplier<AttachmentType<Integer>> metalReserve(AllomanticMetal metal) {
    return METAL_RESERVES.get(metal);
  }

  public static Supplier<AttachmentType<Boolean>> metalBurnState(AllomanticMetal metal) {
    return METAL_BURN_STATE.get(metal);
  }

  public static Supplier<AttachmentType<Boolean>> metalAvailable(AllomanticMetal metal) {
    return METAL_AVAILABLE.get(metal);
  }

  private static Supplier<AttachmentType<Boolean>> registerBooleanAttachmentForMetal(AllomanticMetal metal, String attachmentSuffix) {
    return registerBooleanAttachmentForMetal(metal, attachmentSuffix, false);
  }

  private static Supplier<AttachmentType<Boolean>> registerBooleanAttachmentForMetal(
      AllomanticMetal metal, String attachmentSuffix, boolean copyOnDeath) {
    var attachmentBuilder = AttachmentType.builder(() -> false)
        .serialize(Codec.BOOL);
    if (copyOnDeath) {
      attachmentBuilder.copyOnDeath();
    }
    return ATTACHMENT_TYPES.register(
        "%s_%s".formatted(metal.name().toLowerCase(), attachmentSuffix),
        attachmentBuilder::build);
  }

  private static Supplier<AttachmentType<Integer>> registerIntegerAttachmentForMetal(AllomanticMetal metal, String attachmentSuffix) {
    return ATTACHMENT_TYPES.register(
        "%s_%s".formatted(metal.name().toLowerCase(), attachmentSuffix),
        () -> AttachmentType.builder(() -> 0)
            .serialize(Codec.INT)
            .build());
  }

  public static <T> void copyAttachments(Entity source, Entity target, Supplier<AttachmentType<T>> attachment) {
    source.setData(attachment, target.getData(attachment));
  }

  public static void register(IEventBus modEventBus) {
    ATTACHMENT_TYPES.register(modEventBus);
  }
}
