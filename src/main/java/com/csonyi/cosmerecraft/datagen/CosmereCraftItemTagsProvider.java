package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CosmereCraftItemTagsProvider extends ItemTagsProvider {

  // TODO: is this needed?
  public CosmereCraftItemTagsProvider(
      PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(
        packOutput,
        lookupProvider,
        CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()),
        CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()),
        CosmereCraft.MOD_ID,
        existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookupProvider) {
    CosmereCraftItems.METAL_POWDERS.values().stream()
        .map(Holder::value)
        .forEach(this::tagAsMetalPowder);
    CosmereCraftItems.METAL_INGOTS.values().stream()
        .map(Holder::value)
        .forEach(this::tagAsMetalIngot);
    Stream.of(
            Items.IRON_INGOT,
            Items.GOLD_INGOT,
            Items.COPPER_INGOT)
        .forEach(this::tagAsMetalIngot);
    Stream.of(
            Items.GLASS_BOTTLE,
            Items.GLASS,
            Items.TINTED_GLASS,
            Items.GLASS_PANE,
            Items.WHITE_STAINED_GLASS,
            Items.ORANGE_STAINED_GLASS,
            Items.MAGENTA_STAINED_GLASS,
            Items.LIGHT_BLUE_STAINED_GLASS,
            Items.YELLOW_STAINED_GLASS,
            Items.LIME_STAINED_GLASS,
            Items.PINK_STAINED_GLASS,
            Items.GRAY_STAINED_GLASS,
            Items.LIGHT_GRAY_STAINED_GLASS,
            Items.CYAN_STAINED_GLASS,
            Items.PURPLE_STAINED_GLASS,
            Items.BLUE_STAINED_GLASS,
            Items.BROWN_STAINED_GLASS,
            Items.GREEN_STAINED_GLASS,
            Items.RED_STAINED_GLASS,
            Items.BLACK_STAINED_GLASS,
            Items.WHITE_STAINED_GLASS_PANE,
            Items.ORANGE_STAINED_GLASS_PANE,
            Items.MAGENTA_STAINED_GLASS_PANE,
            Items.LIGHT_BLUE_STAINED_GLASS_PANE,
            Items.YELLOW_STAINED_GLASS_PANE,
            Items.LIME_STAINED_GLASS_PANE,
            Items.PINK_STAINED_GLASS_PANE,
            Items.GRAY_STAINED_GLASS_PANE,
            Items.LIGHT_GRAY_STAINED_GLASS_PANE,
            Items.CYAN_STAINED_GLASS_PANE,
            Items.PURPLE_STAINED_GLASS_PANE,
            Items.BLUE_STAINED_GLASS_PANE,
            Items.BROWN_STAINED_GLASS_PANE,
            Items.GREEN_STAINED_GLASS_PANE,
            Items.RED_STAINED_GLASS_PANE,
            Items.BLACK_STAINED_GLASS_PANE)
        .forEach(this::tagAsGlass);
    Stream.of(
            Items.OBSIDIAN,
            Items.CRYING_OBSIDIAN)
        .forEach(this::tagAsObsidian);
  }

  private void tagAsMetalPowder(Item item) {
    tag(CosmereCraftItems.METAL_POWDERS_TAG).add(item);
  }

  private void tagAsMetalIngot(Item item) {
    tag(CosmereCraftItems.METAL_INGOTS_TAG).add(item);
  }

  private void tagAsObsidian(Item item) {
    tag(CosmereCraftItems.OBSIDIAN_ITEMS_TAG).add(item);
  }

  private void tagAsGlass(Item item) {
    tag(CosmereCraftItems.GLASS_ITEMS_TAG).add(item);
  }
}
