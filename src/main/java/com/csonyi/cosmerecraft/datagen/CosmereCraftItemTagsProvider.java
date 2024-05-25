package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CosmereCraftItemTagsProvider extends ItemTagsProvider {

  // TODO: is this needed? If yes, update
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
  }

  private void tagAsMetalPowder(Item item) {
    tag(Tags.Items.DUSTS).add(item);
  }

  private void tagAsMetalIngot(Item item) {
    tag(Tags.Items.INGOTS).add(item);
  }
}
