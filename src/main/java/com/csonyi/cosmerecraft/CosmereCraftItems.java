package com.csonyi.cosmerecraft;

import static com.csonyi.cosmerecraft.Registry.items;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.item.EdibleMetalNugget;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class CosmereCraftItems {


  public static final Holder<Item> METAL_VIAL = items().register(
      "metal_vial",
      MetalVial::new);
  public static final Holder<Item> DEBUG_VIAL = items().register(
      "debug_vial",
      () -> new MetalVial(AllomanticMetal.STEEL, AllomanticMetal.IRON, AllomanticMetal.PEWTER));
  public static final Holder<Item> INVESTITURE_BUCKET = registerBucketItem(
      "investiture_bucket",
      CosmereCraftFluids.INVESTITURE_FLUID_SOURCE);

  public static final Holder<Item> LERASIUM_NUGGET = items().register("lerasium_nugget", EdibleMetalNugget::new);

  public static void generateDisplayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
    Stream.of(METAL_VIAL, DEBUG_VIAL)
        .map(Holder::value)
        .map(Item::getDefaultInstance)
        .forEach(output::accept);
  }

  private static Holder<Item> registerBucketItem(String name, Holder<Fluid> fluid) {
    return items().register(name, () -> new BucketItem(fluid::value, new Item.Properties()));
  }
}
