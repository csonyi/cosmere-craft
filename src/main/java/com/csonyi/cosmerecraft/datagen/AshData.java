package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.block.AshLayerBlock;
import com.csonyi.cosmerecraft.block.AshyDirtBlock;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AshData extends BaseDataGenerator {

  private final ResourceLocation ASH_BLOCK_LOCATION = modLoc("block/ash");

  public AshData(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    super.registerStatesAndModels();
    AshLayerBlock.LAYERS.getPossibleValues()
        .forEach(this::buildLayerData);
    itemModels().withExistingParent("ash", modLoc("block/ash_height2"));

    getVariantBuilder(CosmereCraftBlocks.ASH_BLOCK.value()).partialState()
        .modelForState()
        .modelFile(cubeAllModel("ash_block", ASH_BLOCK_LOCATION))
        .addModel();
    itemModels().withExistingParent("ash_block", modLoc("block/ash_block"));

    getVariantBuilder(Blocks.GRASS_BLOCK).partialState()
        .with(AshyDirtBlock.ASHY, true)
        .modelForState()
        .modelFile(ashyGrassModel())
        .addModel();

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

  private BlockModelBuilder ashyGrassModel() {
    return models().cubeAll("ashy_grass", modLoc("block/grass_block_ash"));
  }
}
