package com.csonyi.cosmerecraft.block;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftFluids;
import com.csonyi.cosmerecraft.util.ScadrialTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class InvestiturePortalBlock extends LiquidBlock {


  public InvestiturePortalBlock() {
    super(
        CosmereCraftFluids.INVESTITURE_PORTAL,
        BlockBehaviour.Properties.of()
            .noLootTable()
            .liquid()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .lightLevel(state -> 15)
            .explosionResistance(100.0F));
  }

  @Override
  public void entityInside(
      @NotNull BlockState currentBlockState, @NotNull Level currentLevel,
      @NotNull BlockPos currentBlockPos, @NotNull Entity entityInBlock) {
    if (currentLevel instanceof ServerLevel serverLevel) {
      if (!getFluidState(currentBlockState).is(CosmereCraftFluids.INVESTITURE_PORTAL.value())) {
        return;
      }
      var destinationLevel = getDestination(serverLevel);
      if (destinationLevel != null && entityInBlock.canChangeDimensions()) {
        entityInBlock.changeDimension(destinationLevel, new ScadrialTeleporter(serverLevel));
      }
    }
  }

  private static ServerLevel getDestination(ServerLevel level) {
    var destinationResourceKey = CosmereCraft.SCADRIAL.equals(level.dimension())
        ? Level.OVERWORLD
        : CosmereCraft.SCADRIAL;
    return level.getServer().getLevel(destinationResourceKey);
  }

  private static BlockPos getDestinationPosition(ServerLevel destinationLevel, BlockPos startPosition) {
    var destinationPosition = startPosition;
    while (isDestinationPositionInvestitureLiquidBlock(destinationLevel, destinationPosition)) {
      destinationPosition = destinationPosition.north();
    }

    return destinationPosition.above();
  }

  private static boolean isDestinationPositionInvestitureLiquidBlock(ServerLevel destinationLevel, BlockPos destinationPosition) {
    return destinationLevel.getBlockState(destinationPosition).is(CosmereCraftBlocks.INVESTITURE_LIQUID);
  }


}
