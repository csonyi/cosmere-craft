package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftItems {

  public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(CosmereCraft.MOD_ID);
  public static final Holder<Item> METAL_VIAL = ITEMS.register("metal_vial", MetalVial::new);
  public static final Holder<Item> DEBUG_VIAL = ITEMS.register(
      "debug_vial",
      () -> new MetalVial(AllomanticMetal.STEEL, AllomanticMetal.IRON, AllomanticMetal.PEWTER)
  );

  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
  }

  public static void generateDisplayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
    Stream.of(METAL_VIAL, DEBUG_VIAL)
        .map(Holder::value)
        .map(Item::getDefaultInstance)
        .forEach(output::accept);
  }
}
