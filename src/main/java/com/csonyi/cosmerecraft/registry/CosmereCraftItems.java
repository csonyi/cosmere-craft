package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.createResourceLocation;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.item.EdibleMetalNugget;
import com.csonyi.cosmerecraft.item.MetalVial;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftItems {

  public static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(CosmereCraft.MOD_ID);

  public static final TagKey<Item> METAL_POWDERS_TAG = ItemTags.create(createResourceLocation("metal_powders"));
  public static final TagKey<Item> METAL_INGOTS_TAG = ItemTags.create(createResourceLocation("metal_ingots"));
  public static final Holder<Item> INVESTITURE_BUCKET = ITEMS.register(
      "bucket_investiture",
      bucketItem(CosmereCraftFluids.INVESTITURE::value));
  public static final Holder<Item> LERASIUM_NUGGET = ITEMS.register(
      "lerasium_nugget",
      EdibleMetalNugget::new);
  public static final Holder<Item> ASH_PILE = ITEMS.registerSimpleItem("ash_pile");
  public static final Holder<Item> MORTAR_AND_PESTLE = ITEMS.registerSimpleItem("mortar_and_pestle");
  public static final Holder<Item> ASH_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.ASH_BLOCK);
  public static final Holder<Item> ASH_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.ASH);

  public static final Map<AllomanticMetal, Holder<Item>> METAL_POWDERS = AllomanticMetal.stream()
      .filter(metal -> !metal.isGodMetal())
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.registerSimpleItem("%s_powder".formatted(metal.lowerCaseName()))));

  public static final Map<AllomanticMetal, Holder<Item>> METAL_INGOTS = AllomanticMetal.stream()
      .filter(metal -> !metal.isGodMetal())
      .filter(metal -> !metal.hasVanillaImplementation)
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.registerSimpleItem("%s_ingot".formatted(metal.lowerCaseName()))));
  public static final Map<AllomanticMetal, Holder<Item>> METAL_VIALS = AllomanticMetal.stream()
      .filter(metal -> !metal.isGodMetal())
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.register("%s_vial".formatted(metal.lowerCaseName()), () -> new MetalVial(metal))));
  public static final Map<AllomanticMetal, Holder<Item>> RAW_METALS = AllomanticMetal.stream()
      .filter(metal -> !metal.isGodMetal())
      .filter(metal -> !metal.hasVanillaImplementation)
      .filter(metal -> !metal.isAlloy)
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.registerSimpleItem("raw_%s".formatted(metal.lowerCaseName()))));

  public static final Map<AllomanticMetal, Holder<Item>> METAL_ORE_BLOCK_ITEMS =
      CosmereCraftBlocks.METAL_ORES.entrySet().stream()
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              entry -> ITEMS.registerSimpleBlockItem(entry.getValue())));
  public static final Map<AllomanticMetal, Holder<Item>> DEEPSLATE_METAL_ORE_BLOCK_ITEMS =
      CosmereCraftBlocks.DEEPSLATE_METAL_ORES.entrySet().stream()
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              entry -> ITEMS.registerSimpleBlockItem(entry.getValue())));


  public static void generateDisplayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
    Stream.of(
            METAL_POWDERS,
            METAL_INGOTS,
            METAL_VIALS,
            RAW_METALS,
            METAL_ORE_BLOCK_ITEMS,
            DEEPSLATE_METAL_ORE_BLOCK_ITEMS)
        .map(Map::values)
        .flatMap(Collection::stream)
        .map(Holder::value)
        .map(Item::getDefaultInstance)
        .forEach(output::accept);
    Stream.of(
            ASH_BLOCK_ITEM,
            ASH_ITEM,
            ASH_PILE,
            INVESTITURE_BUCKET,
            LERASIUM_NUGGET,
            MORTAR_AND_PESTLE)
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
