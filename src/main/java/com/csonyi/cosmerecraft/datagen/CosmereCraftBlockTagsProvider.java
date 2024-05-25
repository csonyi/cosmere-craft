package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CosmereCraftBlockTagsProvider extends BlockTagsProvider {

  private static final Set<String> ALLOMANTIC_METAL_NAMES = Sets.union(
      AllomanticMetal.names(),
      Set.of("lead", "nickel", "silver", "bismuth"));
  private static final Set<String> MATCHING_NAME_PATTERNS = Set.of("%s_", "_%s");
  public static final Stream<Block> VANILLA_BLOCKS_WITH_METAL_IN_RECIPE = Stream.of(
      Blocks.SMITHING_TABLE,
      Blocks.POWERED_RAIL,
      Blocks.CAULDRON,
      Blocks.LANTERN,
      Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
      Blocks.SOUL_LANTERN,
      Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
      Blocks.SMITHING_TABLE,
      Blocks.BLAST_FURNACE,
      Blocks.LIGHTNING_ROD,
      Blocks.CHAIN,
      Blocks.ACTIVATOR_RAIL,
      Blocks.HOPPER,
      Blocks.DETECTOR_RAIL,
      Blocks.STONECUTTER,
      Blocks.RAIL,
      Blocks.TRIPWIRE_HOOK,
      Blocks.ANVIL,
      Blocks.PISTON);
  public static final Stream<Holder<Block>> STORAGE_BLOCKS = Stream.of(
          CosmereCraftBlocks.METAL_BLOCKS.values().stream(),
          CosmereCraftBlocks.RAW_METAL_BLOCKS.values().stream(),
          Stream.of(
              CosmereCraftBlocks.LEAD_BLOCK,
              CosmereCraftBlocks.NICKEL_BLOCK,
              CosmereCraftBlocks.SILVER_BLOCK,
              CosmereCraftBlocks.BISMUTH_BLOCK,
              CosmereCraftBlocks.RAW_LEAD_BLOCK,
              CosmereCraftBlocks.RAW_NICKEL_BLOCK,
              CosmereCraftBlocks.RAW_SILVER_BLOCK,
              CosmereCraftBlocks.RAW_BISMUTH_BLOCK))
      .flatMap(Function.identity());
  public static final Stream<Holder<Block>> STONE_ORE_BLOCKS = Stream.of(
          CosmereCraftBlocks.METAL_ORES.values().stream(),
          Stream.of(
              CosmereCraftBlocks.LEAD_ORE,
              CosmereCraftBlocks.NICKEL_ORE,
              CosmereCraftBlocks.SILVER_ORE,
              CosmereCraftBlocks.BISMUTH_ORE))
      .flatMap(Function.identity());
  public static final Stream<Holder<Block>> DEEPSLATE_ORE_BLOCKS = Stream.of(
          CosmereCraftBlocks.DEEPSLATE_METAL_ORES.values().stream(),
          Stream.of(
              CosmereCraftBlocks.DEEPSLATE_LEAD_ORE,
              CosmereCraftBlocks.DEEPSLATE_NICKEL_ORE,
              CosmereCraftBlocks.DEEPSLATE_SILVER_ORE,
              CosmereCraftBlocks.DEEPSLATE_BISMUTH_ORE))
      .flatMap(Function.identity());

  public CosmereCraftBlockTagsProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, CosmereCraft.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookupProvider) {
    generateForMatchingBlockKeys();
    addForBlocksWithMetalInRecipe();
    addForModdedBlocks();
  }

  private void addForModdedBlocks() {
    STORAGE_BLOCKS.forEach(block -> {
      tagAsRequiresStoneTool(block);
      tagAsPickaxeMineable(block);
      tagAsStorageBlock(block);
      tagAsAnchor(block);
    });
    STONE_ORE_BLOCKS.forEach((block) -> {
      tagAsRequiresStoneTool(block);
      tagAsPickaxeMineable(block);
      tagAsOre(block);
      tagAsOreInGroundStone(block);
      tagAsAnchor(block);
    });
    DEEPSLATE_ORE_BLOCKS.forEach(block -> {
      tagAsRequiresIronTool(block);
      tagAsPickaxeMineable(block);
      tagAsOre(block);
      tagAsOreInGroundDeepslate(block);
      tagAsAnchor(block);
    });
  }

  private void addForBlocksWithMetalInRecipe() {
    VANILLA_BLOCKS_WITH_METAL_IN_RECIPE.forEach(this::tagAsAnchor);
  }

  private void generateForMatchingBlockKeys() {
    BuiltInRegistries.BLOCK.stream()
        .filter(CosmereCraftBlockTagsProvider::hasMetalInName)
        .forEach(this::tagAsAnchor);
  }

  private static boolean hasMetalInName(Block block) {
    return ALLOMANTIC_METAL_NAMES.stream()
        .anyMatch(name -> blockNameContains(block, name));
  }

  private static boolean blockNameContains(Block block, String name) {
    return MATCHING_NAME_PATTERNS.stream()
        .map(pattern -> pattern.formatted(name))
        .anyMatch(BuiltInRegistries.BLOCK.getKey(block).getPath()::contains);
  }

  private void tagAsAnchor(Holder<Block> block) {
    tagAsAnchor(block.value());
  }

  private void tagAsAnchor(Block block) {
    tag(CosmereCraftBlocks.ANCHOR_TAG).add(block);
  }

  private void tagAsPickaxeMineable(Holder<Block> block) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.value());
  }

  private void tagAsRequiresStoneTool(Holder<Block> block) {
    tag(BlockTags.NEEDS_STONE_TOOL).add(block.value());
  }

  private void tagAsRequiresIronTool(Holder<Block> block) {
    tag(BlockTags.NEEDS_IRON_TOOL).add(block.value());
  }

  private void tagAsOre(Holder<Block> block) {
    tag(Tags.Blocks.ORES).add(block.value());
  }

  private void tagAsOreInGroundStone(Holder<Block> block) {
    tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(block.value());
  }

  private void tagAsOreInGroundDeepslate(Holder<Block> block) {
    tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(block.value());
  }

  private void tagAsStorageBlock(Holder<Block> block) {
    tag(Tags.Blocks.STORAGE_BLOCKS).add(block.value());
  }
}
