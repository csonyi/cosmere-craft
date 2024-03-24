package com.csonyi.cosmerecraft.block;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftFluids;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class InvestitureLiquidBlock extends LiquidBlock {

  public InvestitureLiquidBlock() {
    super(
        CosmereCraftFluids.INVESTITURE,
        BlockBehaviour.Properties.of()
            .noLootTable()
            .liquid()
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .lightLevel(state -> 15)
            .explosionResistance(100.0F));
  }

  @Override
  public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
    if (pLevel instanceof ServerLevel serverLevel) {
      var destinationLevel = getDestination(serverLevel);
      if (destinationLevel != null) {
        pEntity.teleportTo(destinationLevel,
            pEntity.getX(), pEntity.getY(), pEntity.getZ(),
            Set.of(), pEntity.getYRot(), pEntity.getXRot());
      }
    }
  }

  private static ServerLevel getDestination(ServerLevel level) {
    var destinationResourceKey = CosmereCraft.SCADRIAL.equals(level.dimension())
        ? Level.OVERWORLD
        : CosmereCraft.SCADRIAL;
    return level.getServer().getLevel(destinationResourceKey);
  }
}
