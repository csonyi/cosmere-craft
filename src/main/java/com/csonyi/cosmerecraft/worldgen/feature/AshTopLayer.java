package com.csonyi.cosmerecraft.worldgen.feature;

import com.csonyi.cosmerecraft.block.AshyDirtBlock;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Loosely based on the vanilla class {@link SnowAndFreezeFeature}.
 */
public class AshTopLayer extends Feature<NoneFeatureConfiguration> {

  public AshTopLayer() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
    var worldgenlevel = featurePlaceContext.level();
    var placementOrigin = featurePlaceContext.origin();
    var currentBlock = new BlockPos.MutableBlockPos();
    var blockUnderCurrent = new BlockPos.MutableBlockPos();

    for (int dX = 0; dX < 16; ++dX) {
      for (int dZ = 0; dZ < 16; ++dZ) {
        int x = placementOrigin.getX() + dX;
        int z = placementOrigin.getZ() + dZ;
        int motionBlockingHeight = worldgenlevel.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        currentBlock.set(x, motionBlockingHeight, z);
        blockUnderCurrent.set(currentBlock).move(Direction.DOWN);

        var blockStateUnderCurrent = worldgenlevel.getBlockState(blockUnderCurrent);
        if (!isFluid(blockStateUnderCurrent)) {
          worldgenlevel.setBlock(currentBlock, CosmereCraftBlocks.ASH.value().defaultBlockState(), 2);
          if (blockStateUnderCurrent.hasProperty(AshyDirtBlock.ASHY)) {
            worldgenlevel.setBlock(blockUnderCurrent, blockStateUnderCurrent.setValue(AshyDirtBlock.ASHY, true), 2);
          }
        }
      }
    }

    return true;
  }

  private static boolean isFluid(BlockState blockState) {
    return Stream.of(Blocks.WATER, Blocks.LAVA)
        .anyMatch(blockState::is);
  }
}
