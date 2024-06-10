package com.csonyi.cosmerecraft.client.datagen;

import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ANCIENT_MEDALLION;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_PILE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.COAL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INVESTITURE_BUCKET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INVESTITURE_PORTAL_BUCKET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LERASIUM_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.MORTAR_AND_PESTLE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_BISMUTH;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_LEAD;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_NICKEL;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_SILVER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_POWDER;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CosmereCraftItemModelProvider extends ItemModelProvider {

  public CosmereCraftItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, CosmereCraft.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerModels() {
    Stream.concat(
            // Holder collections
            Stream.of(
                    CosmereCraftItems.METAL_POWDERS,
                    CosmereCraftItems.METAL_INGOTS,
                    CosmereCraftItems.METAL_NUGGETS,
                    CosmereCraftItems.METAL_VIALS,
                    CosmereCraftItems.RAW_METALS)
                .map(Map::values)
                .flatMap(Collection::stream),
            Stream.of(
                ASH_PILE,
                INVESTITURE_BUCKET,
                INVESTITURE_PORTAL_BUCKET,
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
                ANCIENT_MEDALLION))
        .map(Holder::value)
        .forEach(this::basicItem);
    handheldItem(CosmereCraftItems.OBSIDIAN_INQUISITOR_AXE);
  }

  public ItemModelBuilder handheldItem(Holder<Item> item) {
    var itemLocation = BuiltInRegistries.ITEM.getKey(item.value());
    return getBuilder(itemLocation.toString())
        .parent(new ModelFile.UncheckedModelFile("item/handheld"))
        .texture("layer0", modLoc("item/%s".formatted(itemLocation.getPath())));
  }
}
