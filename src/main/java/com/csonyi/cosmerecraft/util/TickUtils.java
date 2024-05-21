package com.csonyi.cosmerecraft.util;

import static java.util.function.Predicate.not;

import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;

public class TickUtils {

  private final Level level;

  public TickUtils(Level level) {
    this.level = level;
  }

  private static final int TICKS_PER_SECOND = 20;

  public static int secondsToTicks(double seconds) {
    return Mth.floor(seconds * TICKS_PER_SECOND);
  }

  public static int minutesToTicks(int minutes) {
    return secondsToTicks(minutes * 60);
  }

  public static Component toTimeFormat(long ticks) {
    long seconds = ticks / TICKS_PER_SECOND;
    long minutes = seconds / 60;
    long hours = minutes / 60;
    return Component.literal("%02d:%02d:%02d".formatted(hours, minutes % 60, seconds % 60));
  }

  public boolean everyNthSecond(double n) {
    return everyNthTick(secondsToTicks(n));
  }

  public boolean everyNthTick(int n) {
    return level.getGameTime() % n == 0;
  }

  public static void speedUpRandomTicks(ServerLevel level, int extraTicks, AABB range) {
    if (range == null || extraTicks == 0) {
      return;
    }

    // TODO: Implement deny list config
    BlockPos.betweenClosedStream(range)
        .filter(level::isLoaded)
        .map(pos -> Pair.of(pos.immutable(), level.getBlockState(pos)))
        .filter(pair -> pair.getSecond().isRandomlyTicking())
        .filter(not(pair -> pair.getSecond().getBlock() instanceof LiquidBlock))
        .forEach(pair -> {
          var pos = pair.getFirst();
          var state = pair.getSecond();
          IntStream.range(0, extraTicks)
              .forEach(n -> state.randomTick(level, pos, level.random));
        });
  }

  public static void speedUpBlockEntities(Level level, int extraTicks, AABB range) {
    if (range == null || extraTicks == 0) {
      return;
    }
    // TODO: Implement deny list config
    BlockPos.betweenClosedStream(range)
        .map(level::getBlockEntity)
        .filter(Objects::nonNull)
        .filter(not(BlockEntity::isRemoved))
        .filter(entity -> level.shouldTickBlocksAt(entity.getBlockPos()))
        .forEach(entity -> {
          var blockEntity = level.getBlockEntity(entity.getBlockPos());
          if (blockEntity != null) {
            tickBlockEntity(level, extraTicks, blockEntity);
          }
        });
  }

  public static void slowDownMobs(Level level, float slowDownFactor, AABB range, AABB exclusionRange) {
    if (range == null || slowDownFactor == 0) {
      return;
    }
    var entitiesInRange = level.getEntitiesOfClass(LivingEntity.class, range);
    if (range.intersects(exclusionRange)) {
      entitiesInRange.stream()
          .filter(livingEntity -> exclusionRange.contains(livingEntity.position()))
          .forEach(livingEntity -> slowEntity(livingEntity, slowDownFactor));
    } else {
      entitiesInRange.forEach(livingEntity -> slowEntity(livingEntity, slowDownFactor));
    }
  }

  private static void slowEntity(LivingEntity livingEntity, float slowDownFactor) {
    livingEntity.setDeltaMovement(
        livingEntity.getDeltaMovement()
            .multiply(slowDownFactor, 1, slowDownFactor));
  }

  @SuppressWarnings("unchecked")
  private static <T extends BlockEntity> void tickBlockEntity(Level level, int extraTicks, T blockEntity) {
    var blockPos = blockEntity.getBlockPos();
    var blockEntityTicker = level.getBlockState(blockPos)
        .getTicker(level, (BlockEntityType<T>) blockEntity.getType());
    if (blockEntityTicker != null) {
      var blockState = blockEntity.getBlockState();
      StreamUtils.repeat(extraTicks,
          () -> blockEntityTicker.tick(level, blockPos, blockState, blockEntity));
    }
  }
}
