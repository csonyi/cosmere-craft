package com.csonyi.cosmerecraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AshLayerBlock extends Block {

  public static final MapCodec<AshLayerBlock> CODEC = simpleCodec(AshLayerBlock::new);
  protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{
      Shapes.empty(),
      Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
  };
  public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

  public AshLayerBlock(Properties pProperties) {
    super(pProperties);
    registerDefaultState(stateDefinition.any().setValue(LAYERS, 1));
  }

  @Override
  public @NotNull MapCodec<AshLayerBlock> codec() {
    return CODEC;
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return SHAPE_BY_LAYER[pState.getValue(LAYERS)];
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    var targetBlockState = pContext.getLevel().getBlockState(pContext.getClickedPos());
    if (targetBlockState.is(this)) {
      return addLayer(targetBlockState);
    }
    return super.getStateForPlacement(pContext);
  }

  public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
    int layers = pState.getValue(LAYERS);
    if (pUseContext.getItemInHand().is(this.asItem()) && layers < 8) {
      if (pUseContext.replacingClickedOnBlock()) {
        return pUseContext.getClickedFace() == Direction.UP;
      } else {
        return true;
      }
    } else {
      return layers == 1;
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
    stateBuilder.add(LAYERS);
  }

  private static BlockState addLayer(BlockState targetBlockState) {
    var currentLayers = targetBlockState.getValue(LAYERS);
    return targetBlockState.setValue(LAYERS, currentLayers + 1);
  }
}
