package com.csonyi.cosmerecraft.datagen.client.lang;

import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.ASH;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.ASHY_DIRT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.ASH_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.BISMUTH_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.BISMUTH_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.DEEPSLATE_BISMUTH_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.DEEPSLATE_LEAD_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.DEEPSLATE_METAL_ORES;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.DEEPSLATE_NICKEL_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.DEEPSLATE_SILVER_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.INVESTITURE_LIQUID;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.INVESTITURE_PORTAL_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.LEAD_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.LEAD_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.METAL_BLOCKS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.METAL_ORES;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.NICKEL_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.NICKEL_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.RAW_BISMUTH_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.RAW_LEAD_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.RAW_METAL_BLOCKS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.RAW_NICKEL_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.RAW_SILVER_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.SILVER_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.SILVER_ORE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ANCIENT_MEDALLION;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_PILE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.COAL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INQUISITOR_SPAWN_EGG;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INVESTITURE_BUCKET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INVESTITURE_PORTAL_BUCKET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LERASIUM_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_INGOTS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_NUGGETS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_POWDERS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_VIALS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.MORTAR_AND_PESTLE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.OBSIDIAN_INQUISITOR_AXE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_BISMUTH;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_LEAD;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_METALS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_NICKEL;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_SILVER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_POWDER;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import net.minecraft.data.PackOutput;

public class EnUsLanguageProvider extends BaseLanguageProvider {

  private static final String LOCALE = "en_US";

  public EnUsLanguageProvider(PackOutput output) {
    super(output, LOCALE);
  }

  @Override
  protected void addTranslations() {
    super.addTranslations();
    add("fml.menu.mods.info.description.%s".formatted(CosmereCraft.MOD_ID), "A mod inspired by Brandon Sanderson's Cosmere universe.");
    addItems();
    addBlocks();
    addEntityTypes();
    addBiomes();
    addMetalNames();
    addGuiText();
  }

  /**
   * Add all item translations. Block item translations are inherited from the block translations.
   */
  private void addItems() {
    METAL_POWDERS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, " Powder"));
    addItem(LEAD_POWDER::value, "Lead Powder");
    addItem(NICKEL_POWDER::value, "Nickel Powder");
    addItem(SILVER_POWDER::value, "Silver Powder");
    addItem(BISMUTH_POWDER::value, "Bismuth Powder");

    METAL_INGOTS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, " Ingot"));
    addItem(LEAD_INGOT::value, "Lead Ingot");
    addItem(NICKEL_INGOT::value, "Nickel Ingot");
    addItem(SILVER_INGOT::value, "Silver Ingot");
    addItem(BISMUTH_INGOT::value, "Bismuth Ingot");

    METAL_NUGGETS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, " Nugget"));
    addItem(LEAD_NUGGET::value, "Lead Nugget");
    addItem(NICKEL_NUGGET::value, "Nickel Nugget");
    addItem(SILVER_NUGGET::value, "Silver Nugget");
    addItem(BISMUTH_NUGGET::value, "Bismuth Nugget");

    METAL_VIALS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, " Vial"));

    RAW_METALS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, "Raw ", item));
    addItem(RAW_LEAD::value, "Raw Lead");
    addItem(RAW_NICKEL::value, "Raw Nickel");
    addItem(RAW_SILVER::value, "Raw Silver");
    addItem(RAW_BISMUTH::value, "Raw Bismuth");

    addItem(ASH_PILE::value, "Ash Pile");
    addItem(MORTAR_AND_PESTLE::value, "Mortar and Pestle");
    addItem(COAL_POWDER::value, "Coal Powder");

    addItem(LERASIUM_NUGGET::value, "Lerasium Nugget");
    addItem(OBSIDIAN_INQUISITOR_AXE::value, "Obsidian Axe");
    addItem(INQUISITOR_SPAWN_EGG::value, "Inquisitor Spawn Egg");
    addItem(INVESTITURE_BUCKET::value, "Investiture Bucket");
    addItem(INVESTITURE_PORTAL_BUCKET::value, "Investiture Portal Bucket");
    addItem(ANCIENT_MEDALLION::value, "Ancient Medallion");
  }

  /**
   * Add all block translations.
   */
  private void addBlocks() {
    METAL_ORES
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, block, " Ore"));
    addBlock(LEAD_ORE::value, "Lead Ore");
    addBlock(NICKEL_ORE::value, "Nickel Ore");
    addBlock(SILVER_ORE::value, "Silver Ore");
    addBlock(BISMUTH_ORE::value, "Bismuth Ore");

    DEEPSLATE_METAL_ORES
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, "Deepslate ", block, " Ore"));
    addBlock(DEEPSLATE_LEAD_ORE::value, "Deepslate Lead Ore");
    addBlock(DEEPSLATE_NICKEL_ORE::value, "Deepslate Nickel Ore");
    addBlock(DEEPSLATE_SILVER_ORE::value, "Deepslate Silver Ore");
    addBlock(DEEPSLATE_BISMUTH_ORE::value, "Deepslate Bismuth Ore");

    METAL_BLOCKS
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, "Block of ", block));
    addBlock(LEAD_BLOCK::value, "Block of Lead");
    addBlock(NICKEL_BLOCK::value, "Block of Nickel");
    addBlock(SILVER_BLOCK::value, "Block of Silver");
    addBlock(BISMUTH_BLOCK::value, "Block of Bismuth");

    RAW_METAL_BLOCKS
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, "Block of Raw ", block));
    addBlock(RAW_LEAD_BLOCK::value, "Block of Raw Lead");
    addBlock(RAW_NICKEL_BLOCK::value, "Block of Raw Nickel");
    addBlock(RAW_SILVER_BLOCK::value, "Block of Raw Silver");
    addBlock(RAW_BISMUTH_BLOCK::value, "Block of Raw Bismuth");

    addBlock(ASH::value, "Ash");
    addBlock(ASH_BLOCK::value, "Ash Block");
    addBlock(ASHY_DIRT::value, "Ashy Dirt");

    addBlock(INVESTITURE_LIQUID::value, "Liquid Investiture");
    addBlock(INVESTITURE_PORTAL_BLOCK::value, "Liquid Investiture");
  }

  /**
   * Add all entity type translations.
   */
  private void addEntityTypes() {
    addEntityType(CosmereCraftEntities.INQUISITOR_ENTITY_TYPE, "Steel Inquisitor");
  }

  /**
   * Add all biome translations.
   */
  private void addBiomes() {
    add("biome.cosmerecraft.scadrial_plains", "Scadrial Plains");
  }

  /**
   * Add all metal-related translations.
   */
  private void addMetalNames() {
    AllomanticMetal.stream()
        .forEach(metal -> add(metal.getNameAsComponent().getString(), getTranslatedMetalName(metal)));

    add("cosmerecraft.metals.lead", "Lead");
    add("cosmerecraft.metals.nickel", "Nickel");
    add("cosmerecraft.metals.silver", "Silver");
    add("cosmerecraft.metals.bismuth", "Bismuth");

    add("cosmerecraft.metals.type", "Metal Type");
    add("cosmerecraft.metals.type.physical", "Physical");
    add("cosmerecraft.metals.type.mental", "Mental");
    add("cosmerecraft.metals.type.enhancement", "Enhancement");
    add("cosmerecraft.metals.type.temporal", "Temporal");
    add("cosmerecraft.metals.type.god", "God");

    add("cosmerecraft.metals.direction", "Effect Direction");
    add("cosmerecraft.metals.direction.pushing", "Pushing");
    add("cosmerecraft.metals.direction.pulling", "Pulling");
    add("cosmerecraft.metals.direction.god", "God");

    add("cosmerecraft.metals.side", "Effect Side");
    add("cosmerecraft.metals.side.internal", "Internal");
    add("cosmerecraft.metals.side.external", "External");
    add("cosmerecraft.metals.side.god", "God");

    add("cosmerecraft.metals.unavailable", "Unavailable");
  }

  private void addGuiText() {
    add("cosmerecraft.gui.allomancy", "Allomancy");
    add("item.cosmerecraft.feruchemical_medallion.ancient.tooltip",
        "When I hold it in my hand, I can hear a faint pulsing in the distance...");
  }

  @Override
  protected String getTranslatedMetalName(AllomanticMetal metal) {
    return metal.lowerCaseName();
  }
}
