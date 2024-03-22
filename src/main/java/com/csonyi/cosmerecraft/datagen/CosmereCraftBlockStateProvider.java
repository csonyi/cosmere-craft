package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.block.AshLayerBlock;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CosmereCraftBlockStateProvider extends BlockStateProvider {

  private final ResourceLocation ASH_BLOCK_LOCATION = modLoc("block/ash");

  public CosmereCraftBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, CosmereCraft.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    generateAllomanticMetalStuff();
    generateAshStuff();
  }

  private void generateAllomanticMetalStuff() {
    CosmereCraftBlocks.METAL_ORES.forEach(
        (metal, block) -> getVariantBuilder(block.value()).partialState()
            .modelForState()
            .modelFile(metalOreModel(metal))
            .addModel());
    CosmereCraftBlocks.DEEPSLATE_METAL_ORES.forEach(
        (metal, block) -> getVariantBuilder(block.value()).partialState()
            .modelForState()
            .modelFile(deepslateMetalOreModel(metal))
            .addModel());

    Stream.of(
            CosmereCraftItems.METAL_POWDERS,
            CosmereCraftItems.METAL_INGOTS,
            CosmereCraftItems.METAL_VIALS,
            CosmereCraftItems.RAW_METALS)
        .map(Map::values)
        .flatMap(Collection::stream)
        .map(Holder::value)
        .forEach(itemModels()::basicItem);
  }

  private void generateAshStuff() {
    AshLayerBlock.LAYERS.getPossibleValues()
        .forEach(this::buildLayerData);
    itemModels().withExistingParent("ash", modLoc("block/ash_height2"));

    getVariantBuilder(CosmereCraftBlocks.ASH_BLOCK.value()).partialState()
        .modelForState()
        .modelFile(cubeAllModel("ash_block", ASH_BLOCK_LOCATION))
        .addModel();
    itemModels().withExistingParent("ash_block", modLoc("block/ash_block"));

    // getVariantBuilder(Blocks.GRASS_BLOCK).partialState()
    //     .with(AshyDirtBlock.ASHY, true)
    //     .modelForState()
    //     .modelFile(ashyGrassModel())
    //     .addModel();

    itemModels().basicItem(CosmereCraftItems.ASH_PILE.value());
  }


  private void buildLayerData(int layer) {
    getVariantBuilder(CosmereCraftBlocks.ASH.value()).partialState()
        .with(AshLayerBlock.LAYERS, layer)
        .modelForState()
        .modelFile(getLayerModel(layer))
        .addModel();
  }

  private BlockModelBuilder getLayerModel(int layer) {
    return switch (layer) {
      case 1 -> models().withExistingParent("ash_height2", mcLoc("block/thin_block"))
          .texture("particle", ASH_BLOCK_LOCATION)
          .texture("texture", ASH_BLOCK_LOCATION)
          .element()
          .from(0, 0, 0)
          .to(16, 2, 16)
          .allFaces((direction, faceBuilder) -> buildFaces(layer, direction, faceBuilder))
          .end();
      case 8 -> cubeAllModel("ash", ASH_BLOCK_LOCATION);
      default -> models().getBuilder("ash_height" + layer * 2)
          .texture("particle", ASH_BLOCK_LOCATION)
          .texture("texture", ASH_BLOCK_LOCATION)
          .element()
          .from(0, 0, 0)
          .to(16, layer * 2, 16)
          .allFaces((direction, faceBuilder) -> buildFaces(layer, direction, faceBuilder))
          .end();
    };
  }

  private void buildFaces(int layer, Direction direction, ModelBuilder.ElementBuilder.FaceBuilder faceBuilder) {
    switch (direction) {
      case UP -> faceBuilder
          .uvs(0, 0, 16, 16)
          .texture("#texture")
          .end();
      case DOWN -> faceBuilder
          .uvs(0, 0, 16, 16)
          .texture("#texture")
          .cullface(Direction.DOWN)
          .end();
      default -> faceBuilder
          .uvs(0, 16 - layer * 2, 16, 16)
          .texture("#texture")
          .cullface(direction)
          .end();
    }
  }

  private BlockModelBuilder cubeAllModel(String name, ResourceLocation texture) {
    return models().cubeAll(name, texture);
  }

  private ModelFile metalOreModel(AllomanticMetal metal) {
    var name = "%s_ore".formatted(metal.lowerCaseName());
    return cubeAllModel(name, modLoc("block/" + name));
  }

  private ModelFile deepslateMetalOreModel(AllomanticMetal metal) {
    var name = "deepslate_%s_ore".formatted(metal.lowerCaseName());
    return cubeAllModel(name, modLoc("block/" + name));
  }

  private BlockModelBuilder ashyGrassModel() {
    return cubeAllModel("ashy_grass", modLoc("block/grass_block_ash"));
  }
}
