package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.util.ResourceUtils.modLocation;
import static java.util.function.Predicate.not;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.block.AshLayerBlock;
import com.csonyi.cosmerecraft.block.InvestiturePortalBlock;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.entity.Inquisitor;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftBlocks {

  public static final DeferredRegister.Blocks BLOCKS =
      DeferredRegister.createBlocks(CosmereCraft.MOD_ID);

  public static final TagKey<Block> ANCHOR_TAG = BlockTags.create(modLocation("anchor"));
  public static final TagKey<Block> ASH_TAG = BlockTags.create(modLocation("ash"));
  public static final TagKey<Block> INVESTITURE_LIQUID_TAG = BlockTags.create(modLocation("investiture_liquid"));

  public static final Map<AllomanticMetal, Holder<Block>> METAL_ORES =
      AllomanticMetal.stream(
              not(AllomanticMetal::isGodMetal),
              not(AllomanticMetal::isVanilla),
              not(AllomanticMetal::isAlloy))
          .collect(Collectors.toMap(
              Function.identity(),
              metal -> BLOCKS.registerSimpleBlock(
                  "%s_ore".formatted(metal.lowerCaseName()),
                  BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)
                      .destroyTime(3.0F))));
  public static final Holder<Block> LEAD_ORE = BLOCKS.registerSimpleBlock(
      "lead_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
  public static final Holder<Block> NICKEL_ORE = BLOCKS.registerSimpleBlock(
      "nickel_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
  public static final Holder<Block> SILVER_ORE = BLOCKS.registerSimpleBlock(
      "silver_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
  public static final Holder<Block> BISMUTH_ORE = BLOCKS.registerSimpleBlock(
      "bismuth_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));

  public static final Map<AllomanticMetal, Holder<Block>> DEEPSLATE_METAL_ORES =
      AllomanticMetal.stream(
              not(AllomanticMetal::isGodMetal),
              not(AllomanticMetal::isVanilla),
              not(AllomanticMetal::isAlloy))
          .collect(Collectors.toMap(
              Function.identity(),
              metal -> BLOCKS.registerSimpleBlock(
                  "deepslate_%s_ore".formatted(metal.lowerCaseName()),
                  BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE))));
  public static final Holder<Block> DEEPSLATE_LEAD_ORE = BLOCKS.registerSimpleBlock(
      "deepslate_lead_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
  public static final Holder<Block> DEEPSLATE_NICKEL_ORE = BLOCKS.registerSimpleBlock(
      "deepslate_nickel_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
  public static final Holder<Block> DEEPSLATE_SILVER_ORE = BLOCKS.registerSimpleBlock(
      "deepslate_silver_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
  public static final Holder<Block> DEEPSLATE_BISMUTH_ORE = BLOCKS.registerSimpleBlock(
      "deepslate_bismuth_ore",
      BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));

  public static final Map<AllomanticMetal, Holder<Block>> METAL_BLOCKS =
      AllomanticMetal.stream(
              not(AllomanticMetal::isGodMetal),
              not(AllomanticMetal::isVanilla))
          .collect(Collectors.toMap(
              Function.identity(),
              metal -> BLOCKS.registerSimpleBlock(
                  "%s_block".formatted(metal.lowerCaseName()),
                  BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
  public static final Holder<Block> LEAD_BLOCK = BLOCKS.registerSimpleBlock(
      "lead_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
  public static final Holder<Block> NICKEL_BLOCK = BLOCKS.registerSimpleBlock(
      "nickel_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
  public static final Holder<Block> SILVER_BLOCK = BLOCKS.registerSimpleBlock(
      "silver_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
  public static final Holder<Block> BISMUTH_BLOCK = BLOCKS.registerSimpleBlock(
      "bismuth_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));

  public static final Map<AllomanticMetal, Holder<Block>> RAW_METAL_BLOCKS =
      AllomanticMetal.stream(
              not(AllomanticMetal::isGodMetal),
              not(AllomanticMetal::isVanilla),
              not(AllomanticMetal::isAlloy))
          .collect(Collectors.toMap(
              Function.identity(),
              metal -> BLOCKS.registerSimpleBlock(
                  "raw_%s_block".formatted(metal.lowerCaseName()),
                  BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK))));
  public static final Holder<Block> RAW_LEAD_BLOCK = BLOCKS.registerSimpleBlock(
      "raw_lead_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK));
  public static final Holder<Block> RAW_NICKEL_BLOCK = BLOCKS.registerSimpleBlock(
      "raw_nickel_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK));
  public static final Holder<Block> RAW_SILVER_BLOCK = BLOCKS.registerSimpleBlock(
      "raw_silver_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK));
  public static final Holder<Block> RAW_BISMUTH_BLOCK = BLOCKS.registerSimpleBlock(
      "raw_bismuth_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK));

  public static final Holder<Block> ASH = BLOCKS.registerBlock(
      "ash",
      AshLayerBlock::new,
      BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW)
          .mapColor(DyeColor.LIGHT_GRAY));

  public static final Holder<Block> ASH_BLOCK = BLOCKS.registerSimpleBlock(
      "ash_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK)
          .isValidSpawn(Inquisitor::isValidSpawn)
          .mapColor(DyeColor.LIGHT_GRAY));
  public static final Holder<Block> ASHY_DIRT = BLOCKS.registerSimpleBlock(
      "ashy_dirt",
      BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
          .isValidSpawn(Inquisitor::isValidSpawn));

  public static final DeferredBlock<LiquidBlock> INVESTITURE_LIQUID = BLOCKS.register(
      "investiture_liquid",
      () -> new LiquidBlock(
          CosmereCraftFluids.INVESTITURE.value(),
          BlockBehaviour.Properties.of()
              .noLootTable()
              .liquid()
              .mapColor(MapColor.TERRACOTTA_YELLOW)
              .lightLevel(state -> 15)
              .explosionResistance(100.0F)));
  public static final DeferredBlock<LiquidBlock> INVESTITURE_PORTAL_BLOCK = BLOCKS.register(
      "investiture_portal",
      InvestiturePortalBlock::new);

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}
