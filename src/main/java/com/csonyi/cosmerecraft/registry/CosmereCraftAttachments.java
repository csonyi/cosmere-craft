package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.mojang.serialization.Codec;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CosmereCraftAttachments {

  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

  public static final Supplier<AttachmentType<HashSet<BlockPos>>> KNOWN_ANCHORS =
      ATTACHMENT_TYPES.register(
          "known_anchors",
          () -> AttachmentType.builder(() -> new HashSet<BlockPos>())
              .serialize(new AnchorMapSerializer())
              .build());

  public static final Supplier<AttachmentType<BlockPos>> TRACKED_WELL =
      ATTACHMENT_TYPES.register(
          "tracked_well",
          () -> AttachmentType.builder(() -> BlockPos.ZERO)
              .build());

  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Integer>>> METAL_RESERVES =
      new EnumMap<>(AllomanticMetal.class);
  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Integer>>> METAL_BURN_STRENGTH =
      new EnumMap<>(AllomanticMetal.class);
  public static final EnumMap<AllomanticMetal, Supplier<AttachmentType<Boolean>>> METAL_AVAILABLE =
      new EnumMap<>(AllomanticMetal.class);

  static {
    AllomanticMetal.stream()
        .forEach(metal -> {
          METAL_RESERVES.put(metal, registerIntegerAttachmentForMetal(metal, "reserve"));
          METAL_BURN_STRENGTH.put(metal, registerIntegerAttachmentForMetal(metal, "burn_strength"));
          METAL_AVAILABLE.put(metal, registerBooleanAttachmentForMetal(metal, "available"));
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

  private static Supplier<AttachmentType<Boolean>> registerBooleanAttachmentForMetal(AllomanticMetal metal, String attachmentSuffix) {
    return ATTACHMENT_TYPES.register(
        "%s_%s".formatted(metal.name().toLowerCase(), attachmentSuffix),
        () -> AttachmentType.builder(() -> false)
            .serialize(Codec.BOOL)
            .build());
  }

  private static Supplier<AttachmentType<Integer>> registerIntegerAttachmentForMetal(AllomanticMetal metal, String attachmentSuffix) {
    return ATTACHMENT_TYPES.register(
        "%s_%s".formatted(metal.name().toLowerCase(), attachmentSuffix),
        () -> AttachmentType.builder(() -> 0)
            .serialize(Codec.INT)
            .build());
  }

  public static void register(IEventBus modEventBus) {
    ATTACHMENT_TYPES.register(modEventBus);
  }

  static class AnchorMapSerializer implements IAttachmentSerializer<ListTag, HashSet<BlockPos>> {

    @Override
    public @NotNull HashSet<BlockPos> read(IAttachmentHolder holder, ListTag listTag) {
      return Stream.of(
              listTag.stream()
                  .map(CompoundTag.class::cast)
                  .map(NbtUtils::readBlockPos),
              holder.getExistingData(KNOWN_ANCHORS).stream()
                  .flatMap(Set::stream)
          )
          .flatMap(Stream::distinct)
          .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public @Nullable ListTag write(HashSet<BlockPos> attachment) {
      var listTag = new ListTag();
      attachment.stream()
          .map(NbtUtils::writeBlockPos)
          .forEach(listTag::add);
      return listTag;
    }
  }

  static class TrackedWellSerializer implements IAttachmentSerializer<CompoundTag, BlockPos> {

    @Override
    public @NotNull BlockPos read(IAttachmentHolder holder, CompoundTag tag) {
      if (holder.hasData(TRACKED_WELL)) {
        return holder.getData(TRACKED_WELL);
      }
      return NbtUtils.readBlockPos(tag);
    }

    @Override
    public @Nullable CompoundTag write(BlockPos attachment) {
      return NbtUtils.writeBlockPos(attachment);
    }
  }
}
