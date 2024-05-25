package com.csonyi.cosmerecraft.block;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.entity.Inquisitor;
import com.csonyi.cosmerecraft.util.ChanceUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
    super(pProperties
        .randomTicks()
        .isValidSpawn(Inquisitor::isValidSpawn));
    registerDefaultState(stateDefinition.any().setValue(LAYERS, 1));
  }

  @Override
  public @NotNull MapCodec<AshLayerBlock> codec() {
    return CODEC;
  }

  @Override
  public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext collisionContext) {
    return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
  }

  @Override
  protected @NotNull VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter pReader, BlockPos blockPos) {
    return getShape(blockState, pReader, blockPos, CollisionContext.empty());
  }

  @Override
  protected @NotNull VoxelShape getVisualShape(BlockState blockState, BlockGetter pReader, BlockPos blockPos,
      CollisionContext collisionContext) {
    return getShape(blockState, pReader, blockPos, collisionContext);
  }

  @Override
  protected @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos blockPos,
      CollisionContext collisionContext) {
    return SHAPE_BY_LAYER[blockState.getValue(LAYERS) / 2];
  }

  @Override
  protected boolean useShapeForLightOcclusion(BlockState blockState) {
    return true;
  }

  @Override
  protected boolean canSurvive(@NotNull BlockState blockState, LevelReader level, BlockPos blockPos) {
    var blockBelow = level.getBlockState(blockPos.below());
    if (blockBelow.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
      return false;
    }
    return blockBelow.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON)
        || isLayingOnFullBlockFace(level, blockPos, blockBelow)
        || isLayingOnFullAshBlock(blockBelow);
  }

  @Override
  protected void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
    var chance = ChanceUtils.from(random);
    if (level.getBiome(blockPos).is(CosmereCraft.SCADRIAL_PLAINS)) {
      if (level.getBlockState(blockPos.above()).isAir()) {
        if (canSurvive(blockState, level, blockPos)) {
          if (!isAshLayerThreeBlocksThick(blockState, blockPos, level)) {
            if (blockState.getValue(LAYERS) < 8) {
              if (chance.oneIn(100)) {
                level.setBlockAndUpdate(blockPos, addLayer(blockState));
              }
            }
          } else { // ash layer is 3 blocks thick
            if (chance.oneIn(10)) {
              level.setBlockAndUpdate(blockPos, removeLayer(blockState));
            }
          }
        } else { // can't survive
          level.destroyBlock(blockPos, true);
        }
      }
    }
  }

  private boolean isAshLayerThreeBlocksThick(BlockState topBlock, BlockPos topBlockPos, ServerLevel level) {
    return topBlock.is(this)
        && topBlock.getValue(LAYERS) == 8
        && level.getBlockState(topBlockPos.below()).is(this)
        && level.getBlockState(topBlockPos.below(2)).is(this);
  }

  @Override
  protected @NotNull BlockState updateShape(
      BlockState blockState, @NotNull Direction direction, @NotNull BlockState neighbourState,
      @NotNull LevelAccessor level, @NotNull BlockPos blockPos, @NotNull BlockPos neighbourPos) {
    return !blockState.canSurvive(level, blockPos)
        ? Blocks.AIR.defaultBlockState()
        : super.updateShape(blockState, direction, neighbourState, level, blockPos, neighbourPos);
  }

  private boolean isLayingOnFullAshBlock(BlockState blockBelow) {
    return blockBelow.is(this) && blockBelow.getValue(LAYERS) == 8;
  }

  private static boolean isLayingOnFullBlockFace(LevelReader level, BlockPos blockPos, BlockState blockBelow) {
    return Block.isFaceFull(blockBelow.getCollisionShape(level, blockPos.below()), Direction.UP);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext collisionContext) {
    var targetBlockState = collisionContext.getLevel().getBlockState(collisionContext.getClickedPos());
    if (targetBlockState.is(this)) {
      return addLayer(targetBlockState);
    }
    return super.getStateForPlacement(collisionContext);
  }

  public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
    int layers = blockState.getValue(LAYERS);
    if (blockPlaceContext.getItemInHand().is(this.asItem()) && layers < 8) {
      if (blockPlaceContext.replacingClickedOnBlock()) {
        return blockPlaceContext.getClickedFace() == Direction.UP;
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
    return targetBlockState.setValue(LAYERS, targetBlockState.getValue(LAYERS) + 1);
  }

  private static BlockState removeLayer(BlockState targetBlockState) {
    return targetBlockState.setValue(LAYERS, targetBlockState.getValue(LAYERS) - 1);
  }
}
