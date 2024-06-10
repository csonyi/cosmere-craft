package com.csonyi.cosmerecraft.client.datagen.lang;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseLanguageProvider extends LanguageProvider {

  public BaseLanguageProvider(PackOutput output, String locale) {
    super(output, CosmereCraft.MOD_ID, locale);
  }


  @Override
  protected void addTranslations() {
    add("itemGroup.cosmerecraft", "CosmereCraft");
    add("fml.menu.mods.info.displayname.%s".formatted(CosmereCraft.MOD_ID), "CosmereCraft");
  }

  protected void addAllomanticMetalItemName(AllomanticMetal metal, String prefixItemName, Holder<Item> item) {
    addAllomanticMetalItemName(metal, prefixItemName, item, "");
  }

  protected void addAllomanticMetalItemName(AllomanticMetal metal, Holder<Item> item, String postfixItemName) {
    addAllomanticMetalItemName(metal, "", item, postfixItemName);
  }

  protected void addAllomanticMetalItemName(AllomanticMetal metal, String prefix, Holder<Item> item, String postfix) {
    var metalName = formattedMetalName(metal, prefix);
    addItem(item::value, "%s%s%s".formatted(prefix, metalName, postfix).strip());
  }

  protected void addAllomanticMetalBlockName(AllomanticMetal metal, String prefix, Holder<Block> block) {
    addAllomanticMetalBlockName(metal, prefix, block, "");
  }

  protected void addAllomanticMetalBlockName(AllomanticMetal metal, Holder<Block> block, String postfix) {
    addAllomanticMetalBlockName(metal, "", block, postfix);
  }

  protected void addAllomanticMetalBlockName(AllomanticMetal metal, String prefix, Holder<Block> block, String postfix) {
    var metalName = formattedMetalName(metal, prefix);
    addBlock(block::value, "%s%s%s".formatted(prefix, metalName, postfix).strip());
  }

  private String formattedMetalName(AllomanticMetal metal, String prefix) {
    var translatedName = getTranslatedMetalName(metal);
    var capitalizedName = StringUtils.capitalize(translatedName);
    if (StringUtils.isEmpty(prefix)) {
      return capitalizedName;
    }
    return prefix.charAt(prefix.length() - 1) == ' '
        ? capitalizedName
        : translatedName;
  }

  protected abstract String getTranslatedMetalName(AllomanticMetal metal);

}
