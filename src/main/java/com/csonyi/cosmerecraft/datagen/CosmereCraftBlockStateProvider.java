package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.block.AshLayerBlock;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CosmereCraftBlockStateProvider extends BlockStateProvider {

  private final ResourceLocation ASH_BLOCK_LOCATION = modLoc("block/ash");

  public CosmereCraftBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, CosmereCraft.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    generateMetalStatesAndModels();
    generateAshStatesAndModels();
  }

  private void generateMetalStatesAndModels() {
    CosmereCraftBlocks.METAL_ORES.forEach(
        (metal, block) -> {
          var name = "%s_ore".formatted(metal.lowerCaseName());
          generateCubeAllStuff(block, name);
        });
    generateCubeAllStuff(CosmereCraftBlocks.LEAD_ORE, "lead_ore");
    generateCubeAllStuff(CosmereCraftBlocks.NICKEL_ORE, "nickel_ore");
    generateCubeAllStuff(CosmereCraftBlocks.SILVER_ORE, "silver_ore");
    generateCubeAllStuff(CosmereCraftBlocks.BISMUTH_ORE, "bismuth_ore");
    CosmereCraftBlocks.DEEPSLATE_METAL_ORES.forEach(
        (metal, block) -> {
          var name = "deepslate_%s_ore".formatted(metal.lowerCaseName());
          generateCubeAllStuff(block, name);
        });
    generateCubeAllStuff(CosmereCraftBlocks.DEEPSLATE_LEAD_ORE, "deepslate_lead_ore");
    generateCubeAllStuff(CosmereCraftBlocks.DEEPSLATE_NICKEL_ORE, "deepslate_nickel_ore");
    generateCubeAllStuff(CosmereCraftBlocks.DEEPSLATE_SILVER_ORE, "deepslate_silver_ore");
    generateCubeAllStuff(CosmereCraftBlocks.DEEPSLATE_BISMUTH_ORE, "deepslate_bismuth_ore");
    CosmereCraftBlocks.METAL_BLOCKS.forEach(
        (metal, block) -> {
          var name = "%s_block".formatted(metal.lowerCaseName());
          generateCubeAllStuff(block, name);
        });
    generateCubeAllStuff(CosmereCraftBlocks.LEAD_BLOCK, "lead_block");
    generateCubeAllStuff(CosmereCraftBlocks.NICKEL_BLOCK, "nickel_block");
    generateCubeAllStuff(CosmereCraftBlocks.SILVER_BLOCK, "silver_block");
    generateCubeAllStuff(CosmereCraftBlocks.BISMUTH_BLOCK, "bismuth_block");
    CosmereCraftBlocks.RAW_METAL_BLOCKS.forEach(
        (metal, block) -> {
          var name = "raw_%s_block".formatted(metal.lowerCaseName());
          generateCubeAllStuff(block, name);
        });
    generateCubeAllStuff(CosmereCraftBlocks.RAW_LEAD_BLOCK, "raw_lead_block");
    generateCubeAllStuff(CosmereCraftBlocks.RAW_NICKEL_BLOCK, "raw_nickel_block");
    generateCubeAllStuff(CosmereCraftBlocks.RAW_SILVER_BLOCK, "raw_silver_block");
    generateCubeAllStuff(CosmereCraftBlocks.RAW_BISMUTH_BLOCK, "raw_bismuth_block");
  }

  private void generateAshStatesAndModels() {
    AshLayerBlock.LAYERS.getPossibleValues()
        .forEach(this::buildLayerData);
    itemModels().withExistingParent("ash", modLoc("block/ash_height2"));

    getVariantBuilder(CosmereCraftBlocks.ASH_BLOCK.value()).partialState()
        .modelForState()
        .modelFile(cubeAllModel("ash_block", ASH_BLOCK_LOCATION))
        .addModel();
    itemModels().cubeAll("ash_block", ASH_BLOCK_LOCATION);

    // TODO: add ashy grass behaviour
    // getVariantBuilder(Blocks.GRASS_BLOCK).partialState()
    //     .with(AshyDirtBlock.ASHY, true)
    //     .modelForState()
    //     .modelFile(ashyGrassModel())
    //     .addModel();
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
      case 8 -> cubeAllModel("ash");
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

  private void generateCubeAllStuff(Holder<Block> blockHolder, String name) {
    getVariantBuilder(blockHolder.value()).partialState()
        .modelForState()
        .modelFile(cubeAllModel(name))
        .addModel();
    cubeAllBlockItemModel(name);
  }

  private BlockModelBuilder cubeAllModel(String name) {
    return cubeAllModel(name, modLoc("block/" + name));
  }

  private BlockModelBuilder cubeAllModel(String name, ResourceLocation texture) {
    return models().cubeAll(name, texture);
  }

  private void cubeAllBlockItemModel(String name) {
    itemModels().cubeAll(name, modLoc("block/" + name));
  }

  // private BlockModelBuilder ashyGrassModel() {
  //   return cubeAllModel("ashy_grass", modLoc("block/grass_block_ash"));
  // }
}
