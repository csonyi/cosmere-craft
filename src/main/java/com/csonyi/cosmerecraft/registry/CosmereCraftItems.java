package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.createResourceLocation;
import static java.util.function.Predicate.not;

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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
  public static final Holder<Item> ASH_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.ASH_BLOCK);
  public static final Holder<Item> ASH_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.ASH);
  public static final Holder<Item> MORTAR_AND_PESTLE = ITEMS.registerSimpleItem("mortar_and_pestle");
  public static final Holder<Item> COAL_POWDER = ITEMS.registerSimpleItem("coal_powder");

  public static final Map<AllomanticMetal, Holder<Item>> METAL_POWDERS = AllomanticMetal.stream()
      .filter(not(AllomanticMetal::isGodMetal))
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.registerSimpleItem("%s_powder".formatted(metal.lowerCaseName()))));
  public static final Holder<Item> LEAD_POWDER = ITEMS.registerSimpleItem("lead_powder");
  public static final Holder<Item> NICKEL_POWDER = ITEMS.registerSimpleItem("nickel_powder");
  public static final Holder<Item> SILVER_POWDER = ITEMS.registerSimpleItem("silver_powder");
  public static final Holder<Item> BISMUTH_POWDER = ITEMS.registerSimpleItem("bismuth_powder");
  public static final Map<AllomanticMetal, Holder<Item>> METAL_INGOTS = AllomanticMetal.stream()
      .filter(not(AllomanticMetal::isGodMetal))
      .filter(not(AllomanticMetal::isVanilla))
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.registerSimpleItem("%s_ingot".formatted(metal.lowerCaseName()))));
  public static final Holder<Item> LEAD_INGOT = ITEMS.registerSimpleItem("lead_ingot");
  public static final Holder<Item> NICKEL_INGOT = ITEMS.registerSimpleItem("nickel_ingot");
  public static final Holder<Item> SILVER_INGOT = ITEMS.registerSimpleItem("silver_ingot");
  public static final Holder<Item> BISMUTH_INGOT = ITEMS.registerSimpleItem("bismuth_ingot");
  public static final Map<AllomanticMetal, Holder<Item>> METAL_VIALS = AllomanticMetal.stream()
      .filter(not(AllomanticMetal::isGodMetal))
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.register("%s_vial".formatted(metal.lowerCaseName()), () -> new MetalVial(metal))));
  public static final Map<AllomanticMetal, Holder<Item>> RAW_METALS = AllomanticMetal.stream()
      .filter(not(AllomanticMetal::isGodMetal))
      .filter(not(AllomanticMetal::isVanilla))
      .filter(not(AllomanticMetal::isAlloy))
      .collect(Collectors.toMap(
          Function.identity(),
          metal -> ITEMS.registerSimpleItem("raw_%s".formatted(metal.lowerCaseName()))));
  public static final Holder<Item> RAW_LEAD = ITEMS.registerSimpleItem("raw_lead");
  public static final Holder<Item> RAW_NICKEL = ITEMS.registerSimpleItem("raw_nickel");
  public static final Holder<Item> RAW_SILVER = ITEMS.registerSimpleItem("raw_silver");
  public static final Holder<Item> RAW_BISMUTH = ITEMS.registerSimpleItem("raw_bismuth");
  public static final Map<AllomanticMetal, Holder<Item>> METAL_ORE_BLOCK_ITEMS =
      CosmereCraftBlocks.METAL_ORES.entrySet().stream()
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              entry -> ITEMS.registerSimpleBlockItem(entry.getValue())));
  public static final Holder<Item> LEAD_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.LEAD_ORE);
  public static final Holder<Item> NICKEL_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.NICKEL_ORE);
  public static final Holder<Item> SILVER_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.SILVER_ORE);
  public static final Holder<Item> BISMUTH_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.BISMUTH_ORE);
  public static final Map<AllomanticMetal, Holder<Item>> DEEPSLATE_METAL_ORE_BLOCK_ITEMS =
      CosmereCraftBlocks.DEEPSLATE_METAL_ORES.entrySet().stream()
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              entry -> ITEMS.registerSimpleBlockItem(entry.getValue())));
  public static final Holder<Item> DEEPSLATE_LEAD_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.DEEPSLATE_LEAD_ORE);
  public static final Holder<Item> DEEPSLATE_NICKEL_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.DEEPSLATE_NICKEL_ORE);
  public static final Holder<Item> DEEPSLATE_SILVER_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.DEEPSLATE_SILVER_ORE);
  public static final Holder<Item> DEEPSLATE_BISMUTH_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
      CosmereCraftBlocks.DEEPSLATE_BISMUTH_ORE);
  public static final Map<AllomanticMetal, Holder<Item>> METAL_BLOCK_ITEMS =
      CosmereCraftBlocks.METAL_BLOCKS.entrySet().stream()
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              entry -> ITEMS.registerSimpleBlockItem(entry.getValue())));
  public static final Holder<Item> LEAD_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.LEAD_BLOCK);
  public static final Holder<Item> NICKEL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.NICKEL_BLOCK);
  public static final Holder<Item> SILVER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.SILVER_BLOCK);
  public static final Holder<Item> BISMUTH_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.BISMUTH_BLOCK);

  public static final Map<AllomanticMetal, Holder<Item>> RAW_METAL_BLOCK_ITEMS =
      CosmereCraftBlocks.RAW_METAL_BLOCKS.entrySet().stream()
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              entry -> ITEMS.registerSimpleBlockItem(entry.getValue())));
  public static final Holder<Item> RAW_LEAD_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.RAW_LEAD_BLOCK);
  public static final Holder<Item> RAW_NICKEL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.RAW_NICKEL_BLOCK);
  public static final Holder<Item> RAW_SILVER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.RAW_SILVER_BLOCK);
  public static final Holder<Item> RAW_BISMUTH_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(CosmereCraftBlocks.RAW_BISMUTH_BLOCK);


  public static void generateDisplayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
    Stream.of(
            // Holder collections
            Stream.of(
                    METAL_POWDERS,
                    METAL_INGOTS,
                    METAL_VIALS,
                    RAW_METALS,
                    METAL_ORE_BLOCK_ITEMS,
                    DEEPSLATE_METAL_ORE_BLOCK_ITEMS,
                    METAL_BLOCK_ITEMS,
                    RAW_METAL_BLOCK_ITEMS)
                .map(Map::values)
                .flatMap(Collection::stream),
            // Other item holders
            Stream.of(
                ASH_BLOCK_ITEM,
                ASH_ITEM,
                ASH_PILE,
                INVESTITURE_BUCKET,
                LERASIUM_NUGGET,
                MORTAR_AND_PESTLE,
                COAL_POWDER,
                LEAD_POWDER,
                NICKEL_POWDER,
                SILVER_POWDER,
                BISMUTH_POWDER,
                LEAD_INGOT,
                NICKEL_INGOT,
                SILVER_INGOT,
                BISMUTH_INGOT,
                RAW_LEAD,
                RAW_NICKEL,
                RAW_SILVER,
                RAW_BISMUTH,
                LEAD_ORE_BLOCK_ITEM,
                NICKEL_ORE_BLOCK_ITEM,
                SILVER_ORE_BLOCK_ITEM,
                BISMUTH_ORE_BLOCK_ITEM,
                DEEPSLATE_LEAD_ORE_BLOCK_ITEM,
                DEEPSLATE_NICKEL_ORE_BLOCK_ITEM,
                DEEPSLATE_SILVER_ORE_BLOCK_ITEM,
                DEEPSLATE_BISMUTH_ORE_BLOCK_ITEM,
                LEAD_BLOCK_ITEM,
                NICKEL_BLOCK_ITEM,
                SILVER_BLOCK_ITEM,
                BISMUTH_BLOCK_ITEM,
                RAW_LEAD_BLOCK_ITEM,
                RAW_NICKEL_BLOCK_ITEM,
                RAW_SILVER_BLOCK_ITEM,
                RAW_BISMUTH_BLOCK_ITEM))
        .flatMap(Function.identity())
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


  public static Holder<Item> getPowderItemHolder(AllomanticMetal metal) {
    return CosmereCraftItems.METAL_POWDERS.get(metal);
  }

  public static Holder<Item> getIngotItemHolder(AllomanticMetal metal) {
    return getVanillaImplementedItemHolder(
        metal,
        CosmereCraftItems.METAL_INGOTS,
        Items.IRON_INGOT,
        Items.GOLD_INGOT,
        Items.COPPER_INGOT);
  }

  public static Holder<Item> getRawMetalItemHolder(AllomanticMetal metal) {
    return getVanillaImplementedItemHolder(
        metal,
        CosmereCraftItems.RAW_METALS,
        Items.RAW_IRON,
        Items.RAW_GOLD,
        Items.RAW_COPPER);
  }

  public static Holder<Item> getOreBlockItemHolder(AllomanticMetal metal) {
    return getVanillaImplementedItemHolder(
        metal,
        CosmereCraftItems.METAL_ORE_BLOCK_ITEMS,
        Items.IRON_ORE,
        Items.GOLD_ORE,
        Items.COPPER_ORE);
  }

  public static Holder<Item> getDeepslateOreBlockItemHolder(AllomanticMetal metal) {
    return getVanillaImplementedItemHolder(
        metal,
        CosmereCraftItems.DEEPSLATE_METAL_ORE_BLOCK_ITEMS,
        Items.DEEPSLATE_IRON_ORE,
        Items.DEEPSLATE_GOLD_ORE,
        Items.DEEPSLATE_COPPER_ORE);
  }

  private static Holder<Item> getVanillaImplementedItemHolder(
      AllomanticMetal metal,
      Map<AllomanticMetal, Holder<Item>> moddedItems,
      Item ironItem,
      Item goldItem,
      Item copperItem) {
    return switch (metal) {
      case IRON -> BuiltInRegistries.ITEM.wrapAsHolder(ironItem);
      case GOLD -> BuiltInRegistries.ITEM.wrapAsHolder(goldItem);
      case COPPER -> BuiltInRegistries.ITEM.wrapAsHolder(copperItem);
      default -> moddedItems.get(metal);
    };
  }

}
