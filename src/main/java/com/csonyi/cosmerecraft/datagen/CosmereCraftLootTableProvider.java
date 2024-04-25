package com.csonyi.cosmerecraft.datagen;

import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getRawMetalItemHolder;

import com.csonyi.cosmerecraft.block.AshLayerBlock;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaArchaeologyLoot;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class CosmereCraftLootTableProvider extends LootTableProvider {


  public CosmereCraftLootTableProvider(PackOutput pOutput) {
    super(
        pOutput, Collections.emptySet(),
        List.of(
            BlockLootTables.entry(),
            EntityLootTables.entry(),
            ArcheologyLootTables.entry()));
  }

  public static class BlockLootTables extends BlockLootSubProvider {

    protected BlockLootTables() {
      super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
      generateOreLootTables();
      generateAshLootTables();
    }

    private void generateOreLootTables() {
      CosmereCraftBlocks.METAL_ORES
          .forEach(this::addMetalOreLootTable);
      add(CosmereCraftBlocks.LEAD_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_LEAD.value()));
      add(CosmereCraftBlocks.NICKEL_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_NICKEL.value()));
      add(CosmereCraftBlocks.SILVER_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_SILVER.value()));
      add(CosmereCraftBlocks.BISMUTH_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_BISMUTH.value()));
      CosmereCraftBlocks.DEEPSLATE_METAL_ORES
          .forEach(this::addMetalOreLootTable);
      add(CosmereCraftBlocks.DEEPSLATE_LEAD_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_LEAD.value()));
      add(CosmereCraftBlocks.DEEPSLATE_NICKEL_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_NICKEL.value()));
      add(CosmereCraftBlocks.DEEPSLATE_SILVER_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_SILVER.value()));
      add(CosmereCraftBlocks.DEEPSLATE_BISMUTH_ORE.value(), block -> createOreDrop(block, CosmereCraftItems.RAW_BISMUTH.value()));
      CosmereCraftBlocks.METAL_BLOCKS.values().stream()
          .map(Holder::value)
          .forEach(this::dropSelf);
      dropSelf(CosmereCraftBlocks.LEAD_BLOCK.value());
      dropSelf(CosmereCraftBlocks.NICKEL_BLOCK.value());
      dropSelf(CosmereCraftBlocks.SILVER_BLOCK.value());
      dropSelf(CosmereCraftBlocks.BISMUTH_BLOCK.value());
      CosmereCraftBlocks.RAW_METAL_BLOCKS.values().stream()
          .map(Holder::value)
          .forEach(this::dropSelf);
      dropSelf(CosmereCraftBlocks.RAW_LEAD_BLOCK.value());
      dropSelf(CosmereCraftBlocks.RAW_NICKEL_BLOCK.value());
      dropSelf(CosmereCraftBlocks.RAW_SILVER_BLOCK.value());
      dropSelf(CosmereCraftBlocks.RAW_BISMUTH_BLOCK.value());
    }

    private void addMetalOreLootTable(AllomanticMetal metal, Holder<Block> metalOreBlockHolder) {
      add(metalOreBlockHolder.value(), block -> createOreDrop(block, getRawMetalItemHolder(metal).value()));
    }

    private void generateAshLootTables() {
      add(CosmereCraftBlocks.ASH.value(), this::ashLootTable);
      add(
          CosmereCraftBlocks.ASH_BLOCK.value(),
          block -> createSingleItemTableWithSilkTouch(
              block,
              CosmereCraftItems.ASH_PILE.value(),
              ConstantValue.exactly(4.0F)));
    }

    /**
     * Creates loot table of the ash layer block. Follows the same pattern as the loot table for {@link Blocks#SNOW} in
     * {@link VanillaBlockLoot}.
     *
     * @param block the block to create the loot table for
     * @return the loot table builder
     */
    private LootTable.Builder ashLootTable(Block block) {
      return LootTable.lootTable()
          .withPool(
              LootPool.lootPool()
                  // The block was broken by an entity
                  .when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS))
                  .add(
                      AlternativesEntry.alternatives(
                          // The block was broken without silk touch
                          AlternativesEntry.alternatives(
                                  // For all possible values of layers, drop the same amount of ash piles
                                  AshLayerBlock.LAYERS.getPossibleValues(),
                                  layers -> LootItem.lootTableItem(CosmereCraftItems.ASH_PILE.value())
                                      .when(
                                          LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                              .setProperties(
                                                  StatePropertiesPredicate.Builder.properties()
                                                      .hasProperty(AshLayerBlock.LAYERS, layers)))
                                      .apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers)))
                              )
                              .when(HAS_NO_SILK_TOUCH),
                          // The block was broken with silk touch
                          AlternativesEntry.alternatives(
                              AshLayerBlock.LAYERS.getPossibleValues(),
                              layers -> layers == 8
                                  // If the block has 8 layers, drop an ash block
                                  ? LootItem.lootTableItem(CosmereCraftBlocks.ASH_BLOCK.value())
                                  // Otherwise, drop the same amount of ash layers
                                  : LootItem.lootTableItem(CosmereCraftBlocks.ASH.value())
                                      .apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers)))
                                      .when(
                                          LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                              .setProperties(
                                                  StatePropertiesPredicate.Builder.properties()
                                                      .hasProperty(AshLayerBlock.LAYERS, layers)))))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
      return CosmereCraftBlocks.BLOCKS.getEntries().stream()
          .map(DeferredHolder::value)
          .map(block -> (Block) block)
          ::iterator;
    }

    private static SubProviderEntry entry() {
      return new SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK);
    }
  }

  public static class EntityLootTables extends EntityLootSubProvider {

    protected EntityLootTables() {
      super(FeatureFlags.REGISTRY.allFlags());
    }


    @Override
    public void generate() {
      generateInquisitorLootTable();
    }

    private void generateInquisitorLootTable() {
      // Create loot pool from all metal vials
      var metalVialPool = LootPool.lootPool()
          // Uniform distribution of 0 to 4 rolls
          .setRolls(UniformGenerator.between(0, 4));
      CosmereCraftItems.METAL_VIALS.values().stream()
          .map(Holder::value)
          .map(LootItem::lootTableItem)
          .forEach(metalVialPool::add);
      add(
          CosmereCraftEntities.INQUISITOR_ENTITY_TYPE.value(),
          LootTable.lootTable()
              .withPool(metalVialPool));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
      return CosmereCraftEntities.ENTITY_TYPES.getEntries().stream()
          .map(DeferredHolder::value);
    }

    private static SubProviderEntry entry() {
      return new SubProviderEntry(EntityLootTables::new, LootContextParamSets.ENTITY);
    }
  }

  public static class ArcheologyLootTables implements LootTableSubProvider {

    /**
     * Copied from {@link VanillaArchaeologyLoot}, otherwise generating it would override vanilla loot tables completely.<br> Medallions can
     * be found in any archeology site, but they have a weight value of 2 in the trail ruins "rare" table.
     * TODO: Implement via GlobalLootModifierProvider
     *
     * @param builder
     */
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> builder) {
      builder.accept(
          BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(ConstantValue.exactly(1.0F))
                      .add(LootItem.lootTableItem(Items.ARMS_UP_POTTERY_SHERD).setWeight(2))
                      .add(LootItem.lootTableItem(Items.BREWER_POTTERY_SHERD).setWeight(2))
                      .add(LootItem.lootTableItem(Items.BRICK))
                      .add(LootItem.lootTableItem(Items.EMERALD))
                      .add(LootItem.lootTableItem(Items.STICK))
                      .add(
                          LootItem.lootTableItem(Items.SUSPICIOUS_STEW)
                              .apply(
                                  SetStewEffectFunction.stewEffect()
                                      .withEffect(MobEffects.NIGHT_VISION, UniformGenerator.between(7.0F, 10.0F))
                                      .withEffect(MobEffects.JUMP, UniformGenerator.between(7.0F, 10.0F))
                                      .withEffect(MobEffects.WEAKNESS, UniformGenerator.between(6.0F, 8.0F))
                                      .withEffect(MobEffects.BLINDNESS, UniformGenerator.between(5.0F, 7.0F))
                                      .withEffect(MobEffects.POISON, UniformGenerator.between(10.0F, 20.0F))
                                      .withEffect(MobEffects.SATURATION, UniformGenerator.between(7.0F, 10.0F))))
                      .add(LootItem.lootTableItem(CosmereCraftItems.ANCIENT_MEDALLION.value()))));
      builder.accept(
          BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(ConstantValue.exactly(1.0F))
                      .add(LootItem.lootTableItem(Items.ARCHER_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.MINER_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.PRIZE_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.SKULL_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.DIAMOND))
                      .add(LootItem.lootTableItem(Items.TNT))
                      .add(LootItem.lootTableItem(Items.GUNPOWDER))
                      .add(LootItem.lootTableItem(Items.EMERALD))
                      .add(LootItem.lootTableItem(CosmereCraftItems.ANCIENT_MEDALLION.value()))));
      builder.accept(
          BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(ConstantValue.exactly(1.0F))
                      .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                      .add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
                      .add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.CLAY).setWeight(2))
                      .add(LootItem.lootTableItem(Items.BRICK).setWeight(2))
                      .add(LootItem.lootTableItem(Items.YELLOW_DYE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.BLUE_DYE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.LIGHT_BLUE_DYE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.WHITE_DYE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.ORANGE_DYE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.RED_CANDLE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.GREEN_CANDLE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.PURPLE_CANDLE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.BROWN_CANDLE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.MAGENTA_STAINED_GLASS_PANE))
                      .add(LootItem.lootTableItem(Items.PINK_STAINED_GLASS_PANE))
                      .add(LootItem.lootTableItem(Items.BLUE_STAINED_GLASS_PANE))
                      .add(LootItem.lootTableItem(Items.LIGHT_BLUE_STAINED_GLASS_PANE))
                      .add(LootItem.lootTableItem(Items.RED_STAINED_GLASS_PANE))
                      .add(LootItem.lootTableItem(Items.YELLOW_STAINED_GLASS_PANE))
                      .add(LootItem.lootTableItem(Items.PURPLE_STAINED_GLASS_PANE))
                      .add(LootItem.lootTableItem(Items.SPRUCE_HANGING_SIGN))
                      .add(LootItem.lootTableItem(Items.OAK_HANGING_SIGN))
                      .add(LootItem.lootTableItem(Items.GOLD_NUGGET))
                      .add(LootItem.lootTableItem(Items.COAL))
                      .add(LootItem.lootTableItem(Items.WHEAT_SEEDS))
                      .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS))
                      .add(LootItem.lootTableItem(Items.DEAD_BUSH))
                      .add(LootItem.lootTableItem(Items.FLOWER_POT))
                      .add(LootItem.lootTableItem(Items.STRING))
                      .add(LootItem.lootTableItem(Items.LEAD))));
      builder.accept(
          BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(ConstantValue.exactly(1.0F))
                      .add(LootItem.lootTableItem(CosmereCraftItems.ANCIENT_MEDALLION.value()).setWeight(2))
                      .add(LootItem.lootTableItem(Items.BURN_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.DANGER_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.FRIEND_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.HEART_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.HEARTBREAK_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.HOWL_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.SHEAF_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE))
                      .add(LootItem.lootTableItem(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE))
                      .add(LootItem.lootTableItem(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE))
                      .add(LootItem.lootTableItem(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE))
                      .add(LootItem.lootTableItem(Items.MUSIC_DISC_RELIC))));
      builder.accept(
          BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(ConstantValue.exactly(1.0F))
                      .add(LootItem.lootTableItem(Items.ANGLER_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.SHELTER_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.SNORT_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.SNIFFER_EGG))
                      .add(LootItem.lootTableItem(Items.IRON_AXE))
                      .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                      .add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
                      .add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.COAL).setWeight(2))
                      .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2))
                      .add(LootItem.lootTableItem(CosmereCraftItems.ANCIENT_MEDALLION.value()))));
      builder.accept(
          BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .setRolls(ConstantValue.exactly(1.0F))
                      .add(LootItem.lootTableItem(Items.BLADE_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.EXPLORER_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.MOURNER_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.PLENTY_POTTERY_SHERD))
                      .add(LootItem.lootTableItem(Items.IRON_AXE))
                      .add(LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                      .add(LootItem.lootTableItem(Items.WHEAT).setWeight(2))
                      .add(LootItem.lootTableItem(Items.WOODEN_HOE).setWeight(2))
                      .add(LootItem.lootTableItem(Items.COAL).setWeight(2))
                      .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2))
                      .add(LootItem.lootTableItem(CosmereCraftItems.ANCIENT_MEDALLION.value()))));
    }

    private static SubProviderEntry entry() {
      return new SubProviderEntry(ArcheologyLootTables::new, LootContextParamSets.ARCHAEOLOGY);
    }
  }
}
