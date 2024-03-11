package com.csonyi.cosmerecraft.datagen;

import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.DEBUG_VIAL;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.INVESTITURE_BUCKET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LERASIUM_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_VIAL;

import com.csonyi.cosmerecraft.CosmereCraft;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BaseDataGenerator extends BlockStateProvider {

  public BaseDataGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, CosmereCraft.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    Stream.of(
            LERASIUM_NUGGET,
            METAL_VIAL,
            DEBUG_VIAL,
            INVESTITURE_BUCKET)
        .map(Holder::value)
        .forEach(itemModels()::basicItem);
  }
}
