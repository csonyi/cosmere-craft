package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftStructures {

  public static final DeferredRegister<PoiType> POI_TYPES =
      DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, CosmereCraft.MOD_ID);

  public static final Holder<PoiType> WELL_OF_ASCENSION_POI = POI_TYPES.register(
      "well_of_ascension",
      () -> new PoiType(
          ImmutableSet.copyOf(CosmereCraftBlocks.INVESTITURE_PORTAL_BLOCK.value().getStateDefinition().getPossibleStates()),
          0, 1));

  public static void register(IEventBus eventBus) {
    POI_TYPES.register(eventBus);
  }
}
