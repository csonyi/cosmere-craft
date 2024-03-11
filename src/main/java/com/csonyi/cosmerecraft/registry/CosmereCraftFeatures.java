package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.worldgen.feature.AshTopLayer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftFeatures {

  public static final DeferredRegister<Feature<?>> FEATURES =
      DeferredRegister.create(Registries.FEATURE, CosmereCraft.MOD_ID);

  public static final Holder<Feature<?>> ASH_TOP_LAYER = FEATURES.register(
      "ash_top_layer",
      AshTopLayer::new);

  public static void register(IEventBus modEventBus) {
    FEATURES.register(modEventBus);
  }

}
