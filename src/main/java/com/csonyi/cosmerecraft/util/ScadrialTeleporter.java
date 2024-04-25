package com.csonyi.cosmerecraft.util;

import java.util.Comparator;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.util.ITeleporter;

public class ScadrialTeleporter implements ITeleporter {

  private static final int MAX_PORTAL_DISTANCE = 16;
  public static final TagKey<Structure> ANCIENT_MEDALLION_LOCATED = TagKey.create(
      Registries.STRUCTURE,
      ResourceUtils.modLocation("ancient_medallion_located"));
  public static final ResourceKey<PoiType> WELL_OF_ASCENSION = ResourceKey.create(
      Registries.POINT_OF_INTEREST_TYPE,
      ResourceUtils.modLocation("well_of_ascension"));

  private final ServerLevel level;

  public ScadrialTeleporter(ServerLevel level) {
    this.level = level;
  }

  @Override
  public boolean isVanilla() {
    return false;
  }

  @Override
  public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
    return false;
  }

  private Optional<BlockPos> findWellOnTheOtherSide(
      BlockPos startPortalPos, boolean isNether, WorldBorder worldBorder) {
    var poiManager = level.getPoiManager();
    poiManager.ensureLoadedAndValid(level, startPortalPos, MAX_PORTAL_DISTANCE);

    return poiManager.getInSquare(
            poiTypeHolder -> poiTypeHolder.is(WELL_OF_ASCENSION),
            startPortalPos, MAX_PORTAL_DISTANCE, PoiManager.Occupancy.ANY)
        .filter(well -> worldBorder.isWithinBounds(well.getPos()))
        .min(
            compareWellsByDistanceSquared(startPortalPos)
                .thenComparing(compareWellsByHeight()))
        .map(PoiRecord::getPos);
  }

  private Comparator<PoiRecord> compareWellsByDistanceSquared(BlockPos startPortalPos) {
    return Comparator.comparingDouble(well -> well.getPos().distSqr(startPortalPos));
  }

  private Comparator<PoiRecord> compareWellsByHeight() {
    return Comparator.comparingInt(well -> well.getPos().getY());
  }

  public static Optional<BlockPos> getWellLocation(ServerLevel serverLevel, BlockPos playerPos) {
    return Optional.ofNullable(serverLevel.findNearestMapStructure(
        ScadrialTeleporter.ANCIENT_MEDALLION_LOCATED,
        playerPos,
        100,
        false));
  }

  // public static Optional<BlockPos> getWellLocation(ServerLevel serverLevel, BlockPos playerPos) throws Exception {
  //   var registry = serverLevel.registryAccess()
  //       .registryOrThrow(Registries.STRUCTURE);
  //   var key = ResourceKey.create(Registries.STRUCTURE, ResourceUtils.modLocation("well_of_ascension/well_of_ascension"));
  //   return registry.getHolder(key)
  //       .map(HolderSet::direct)
  //       .map(holderSet -> serverLevel.getChunkSource()
  //           .getGenerator()
  //           .findNearestMapStructure(
  //               serverLevel,
  //               holderSet,
  //               playerPos,
  //               100, false))
  //       .map(Pair::getFirst);
  // }

}
