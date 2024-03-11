package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.item.EdibleMetalNugget;
import com.csonyi.cosmerecraft.item.MetalVial;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftItems {

  public static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(CosmereCraft.MOD_ID);

  public static final Holder<Item> METAL_VIAL = ITEMS.register(
      "metal_vial",
      MetalVial::new);
  public static final Holder<Item> DEBUG_VIAL = ITEMS.register(
      "debug_vial",
      () -> new MetalVial(AllomanticMetal.STEEL, AllomanticMetal.IRON, AllomanticMetal.PEWTER));
  public static final Holder<Item> INVESTITURE_BUCKET = ITEMS.register(
      "bucket_investiture",
      bucketItem(CosmereCraftFluids.INVESTITURE::value));
  public static final Holder<Item> LERASIUM_NUGGET = ITEMS.register(
      "lerasium_nugget",
      EdibleMetalNugget::new);
  public static final Holder<Item> ASH_PILE = ITEMS.registerSimpleItem("ash_pile");
  public static final Holder<Item> ASH_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.ASH_BLOCK);
  public static final Holder<Item> ASH_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.ASH);

  public static void generateDisplayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
    Stream.of(METAL_VIAL, DEBUG_VIAL, INVESTITURE_BUCKET, ASH_PILE, ASH_BLOCK_ITEM, ASH_ITEM, LERASIUM_NUGGET)
        .map(Holder::value)
        .map(Item::getDefaultInstance)
        .forEach(output::accept);
  }

  public static void register(IEventBus modEventBus) {
    ITEMS.register(modEventBus);
  }

  private static Supplier<BucketItem> bucketItem(Supplier<Fluid> fluid) {
    return () -> new BucketItem(fluid, new Item.Properties());
  }

}
