package com.csonyi.cosmerecraft.datagen;

import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INVESTITURE_BUCKET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LERASIUM_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.MORTAR_AND_PESTLE;

import com.csonyi.cosmerecraft.CosmereCraft;
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
            INVESTITURE_BUCKET,
            LERASIUM_NUGGET,
            MORTAR_AND_PESTLE)
        .map(Holder::value)
        .forEach(this::basicItem);
  }
}
