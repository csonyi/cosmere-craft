package com.csonyi.cosmerecraft.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.CapabilityRegistry;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;

public class ChunkCapability<T, C> extends BaseCapability<T, C> {

  private static final CapabilityRegistry<ChunkCapability<?, ?>> registry = new CapabilityRegistry<>(ChunkCapability::new);

  final Map<ChunkPos, List<ICapabilityProvider<ChunkAccess, C, T>>> providers = new HashMap<>();

  public ChunkCapability(ResourceLocation resourceLocation, Class<T> capabilityType, Class<C> contextType) {
    super(resourceLocation, capabilityType, contextType);
  }

  @SuppressWarnings("unchecked")
  public static <T, C> ChunkCapability<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
    return (ChunkCapability<T, C>) registry.create(name, typeClass, contextClass);
  }

  public static <T> ChunkCapability<T, Boolean> createBoolean(ResourceLocation name, Class<T> typeClass) {
    return create(name, typeClass, Boolean.class);
  }

  public static synchronized List<ChunkCapability<?, ?>> getAll() {
    return registry.getAll();
  }

  public Optional<T> getCapability(ChunkAccess chunk, C context) {
    return providers.getOrDefault(chunk.getPos(), List.of()).stream().findFirst()
        .map(provider -> provider.getCapability(chunk, context));
  }

  public static <T, C> void registerChunk(ChunkCapability<T, C> capability, ChunkAccess chunk,
      ICapabilityProvider<ChunkAccess, C, T> provider) {
    Objects.requireNonNull(provider);

    capability.providers
        .computeIfAbsent(chunk.getPos(), pos -> new ArrayList<>())
        .add(provider);
  }
}
