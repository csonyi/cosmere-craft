package com.csonyi.cosmerecraft.block;

import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * This class is fully based on the vanilla {@link SnowyDirtBlock} class.
 */
public class AshyDirtBlock extends Block {

  public static final MapCodec<AshyDirtBlock> CODEC = simpleCodec(AshyDirtBlock::new);
  public static final BooleanProperty ASHY = BooleanProperty.create("ashy");

  @Override
  protected MapCodec<AshyDirtBlock> codec() {
    return CODEC;
  }

  public AshyDirtBlock(Properties properties) {
    super(properties);
    registerDefaultState(stateDefinition.any().setValue(ASHY, false));
  }

  @Override
  public BlockState updateShape(
      BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
    return pFacing == Direction.UP
        ? pState.setValue(ASHY, isAshy(pFacingState))
        : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
    var blockStateAboveClickedBlock = placeContext.getLevel()
        .getBlockState(placeContext.getClickedPos().above());
    return this.defaultBlockState().setValue(ASHY, isAshy(blockStateAboveClickedBlock));
  }

  private static boolean isAshy(BlockState pState) {
    return pState.is(CosmereCraftBlocks.ASH_TAG);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(ASHY);
  }

}
