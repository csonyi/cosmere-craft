package com.csonyi.cosmerecraft.datagen;

import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_PILE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.COAL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INVESTITURE_BUCKET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LERASIUM_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.MORTAR_AND_PESTLE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_LEAD;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CosmereCraftItemModelProvider extends ItemModelProvider {

  public CosmereCraftItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, CosmereCraft.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerModels() {
    Stream.of(
            // Holder collections
            Stream.of(
                    CosmereCraftItems.METAL_POWDERS,
                    CosmereCraftItems.METAL_INGOTS,
                    CosmereCraftItems.METAL_VIALS,
                    CosmereCraftItems.RAW_METALS)
                .map(Map::values)
                .flatMap(Collection::stream),
            Stream.of(
                ASH_PILE,
                INVESTITURE_BUCKET,
                LERASIUM_NUGGET,
                MORTAR_AND_PESTLE,
                COAL_POWDER,
                LEAD_POWDER,
                LEAD_INGOT,
                RAW_LEAD))
        .flatMap(Function.identity())
        .map(Holder::value)
        .forEach(this::basicItem);
  }
}
