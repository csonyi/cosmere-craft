package com.csonyi.cosmerecraft.datagen.client.lang;

import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.ASH;
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
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.OBSIDIAN_AXE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_BISMUTH;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_LEAD;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_METALS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_NICKEL;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_SILVER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_POWDER;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import net.minecraft.data.PackOutput;

public class HuHuLanguageProvider extends BaseLanguageProvider {

  private static final String LOCALE = "hu_HU";

  public HuHuLanguageProvider(PackOutput output) {
    super(output, LOCALE);
  }

  @Override
  protected void addTranslations() {
    super.addTranslations();
    add("fml.menu.mods.info.description.%s".formatted(CosmereCraft.MOD_ID), "Egy Brandon Sanderson Ködszerzet cimű regénye ihlette mod.");
    addCustomTooltips();
    addItems();
    addBlocks();
    addEntityTypes();
    addBiomes();
    addMetalNames();
  }

  /**
   * Add all item translations. Block item translations are inherited from the block translations.
   */
  private void addItems() {
    METAL_POWDERS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, "por"));
    addItem(LEAD_POWDER::value, "Ólompor");
    addItem(NICKEL_POWDER::value, "Nikkelpor");
    addItem(SILVER_POWDER::value, "Ezüstpor");
    addItem(BISMUTH_POWDER::value, "Bizmutpor");

    METAL_INGOTS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, "rúd"));
    addItem(LEAD_INGOT::value, "Ólomrúd");
    addItem(NICKEL_INGOT::value, "Nikkelrúd");
    addItem(SILVER_INGOT::value, "Ezüstrúd");
    addItem(BISMUTH_INGOT::value, "Bizmutrúd");

    METAL_NUGGETS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, "rög"));
    addItem(LEAD_NUGGET::value, "Ólomrög");
    addItem(NICKEL_NUGGET::value, "Nikkelrög");
    addItem(SILVER_NUGGET::value, "Ezüströg");
    addItem(BISMUTH_NUGGET::value, "Bizmutrög");

    METAL_VIALS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, item, " Fiola"));

    RAW_METALS
        .forEach((metal, item) -> addAllomanticMetalItemName(metal, "Nyers", item));
    addItem(RAW_LEAD::value, "Nyersólom");
    addItem(RAW_NICKEL::value, "Nyersnikkel");
    addItem(RAW_SILVER::value, "Nyersezüst");
    addItem(RAW_BISMUTH::value, "Nyersbizmut");

    addItem(ASH_PILE::value, "Hamukupac");
    addItem(MORTAR_AND_PESTLE::value, "Mozsár");
    addItem(COAL_POWDER::value, "Szénpor");

    addItem(LERASIUM_NUGGET::value, "Lerasiumrög");
    addItem(OBSIDIAN_AXE::value, "Obszidián Balta");
    addItem(INQUISITOR_SPAWN_EGG::value, "Inkvizítoridéző Tojás");
    addItem(INVESTITURE_BUCKET::value, "Invesztitúra Vödör");
    addItem(INVESTITURE_PORTAL_BUCKET::value, "Invesztitúra Portál Vödör");
    addItem(ANCIENT_MEDALLION::value, "Ősi Medallion");
  }

  /**
   * Add all block translations.
   */
  private void addBlocks() {
    METAL_ORES
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, block, "érc"));
    addBlock(LEAD_ORE::value, "Ólomérc");
    addBlock(NICKEL_ORE::value, "Nikkelérc");
    addBlock(SILVER_ORE::value, "Ezüstérc");
    addBlock(BISMUTH_ORE::value, "Bizmutérc");

    DEEPSLATE_METAL_ORES
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, block, "érc mélypalában"));
    addBlock(DEEPSLATE_LEAD_ORE::value, "Ólomérc mélypalában");
    addBlock(DEEPSLATE_NICKEL_ORE::value, "Nikkelérc mélypalában");
    addBlock(DEEPSLATE_SILVER_ORE::value, "Ezüstérc mélypalában");
    addBlock(DEEPSLATE_BISMUTH_ORE::value, "Bizmutérc mélypalában");

    METAL_BLOCKS
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, block, "blokk"));
    addBlock(LEAD_BLOCK::value, "Ólomblokk");
    addBlock(NICKEL_BLOCK::value, "Nikkelblokk");
    addBlock(SILVER_BLOCK::value, "Ezüstblokk");
    addBlock(BISMUTH_BLOCK::value, "Bizmutblokk");

    RAW_METAL_BLOCKS
        .forEach((metal, block) -> addAllomanticMetalBlockName(metal, "Nyers", block, "blokk"));
    addBlock(RAW_LEAD_BLOCK::value, "Nyersólomblokk");
    addBlock(RAW_NICKEL_BLOCK::value, "Nyersnikkelblokk");
    addBlock(RAW_SILVER_BLOCK::value, "Nyersezüstblokk");
    addBlock(RAW_BISMUTH_BLOCK::value, "Nyersbizmutblokk");

    addBlock(ASH::value, "Hamu");
    addBlock(ASH_BLOCK::value, "Hamublokk");

    addBlock(INVESTITURE_LIQUID::value, "Folyékony Invesztitúra");
    addBlock(INVESTITURE_PORTAL_BLOCK::value, "Folyékony Invesztitúra");
  }

  /**
   * Add all entity type translations.
   */
  private void addEntityTypes() {
    addEntityType(CosmereCraftEntities.INQUISITOR_ENTITY_TYPE, "Acél Inkvizítor");
  }

  /**
   * Add all biome translations.
   */
  private void addBiomes() {
    add("biome.cosmerecraft.scadrial_plains", "Scadriali mezők");
  }

  /**
   * Add all metal-related translations.
   */
  private void addMetalNames() {
    add("cosmerecraft.metals.steel", "Acél");
    add("cosmerecraft.metals.iron", "Vas");
    add("cosmerecraft.metals.zinc", "Cink");
    add("cosmerecraft.metals.brass", "Sárgaréz");

    add("cosmerecraft.metals.pewter", "Forrasz");
    add("cosmerecraft.metals.tin", "Ón");
    add("cosmerecraft.metals.copper", "Vörösréz");
    add("cosmerecraft.metals.bronze", "Bronz");

    add("cosmerecraft.metals.duralumin", "Dúralumínium");
    add("cosmerecraft.metals.aluminum", "Alumínium");
    add("cosmerecraft.metals.gold", "Arany");
    add("cosmerecraft.metals.electrum", "Elektrum");

    add("cosmerecraft.metals.nicrosil", "Nikroszil");
    add("cosmerecraft.metals.chromium", "Krómium");
    add("cosmerecraft.metals.cadmium", "Kadmium");
    add("cosmerecraft.metals.bendalloy", "Hajlafém");

    add("cosmerecraft.metals.atium", "Atium");
    add("cosmerecraft.metals.lerasium", "Lerasium");

    add("cosmerecraft.metals.lead", "Ólom");
    add("cosmerecraft.metals.nickel", "Nikkel");
    add("cosmerecraft.metals.silver", "Ezüst");
    add("cosmerecraft.metals.bismuth", "Bizmut");

    add("cosmerecraft.metals.type", "Fém típus");
    add("cosmerecraft.metals.type.physical", "Testi");
    add("cosmerecraft.metals.type.mental", "Szellemi");
    add("cosmerecraft.metals.type.enhancement", "Erősítő");
    add("cosmerecraft.metals.type.temporal", "Időbeli");
    add("cosmerecraft.metals.type.god", "Isteni");

    add("cosmerecraft.metals.direction", "Hatás iránya");
    add("cosmerecraft.metals.direction.pushing", "Taszító");
    add("cosmerecraft.metals.direction.pulling", "Vonzó");
    add("cosmerecraft.metals.direction.god", "Isteni");

    add("cosmerecraft.metals.side", "Hatás helye");
    add("cosmerecraft.metals.side.internal", "Belső");
    add("cosmerecraft.metals.side.external", "Kulső");
    add("cosmerecraft.metals.side.god", "Isteni");

    add("cosmerecraft.metals.unavailable", "Nem elérhető");
  }

  private void addCustomTooltips() {
    add(ANCIENT_MEDALLION_TOOLTIP, "Mikor kezemben tartom, mintha egy távoli lüktetést halanék...");
  }

}
