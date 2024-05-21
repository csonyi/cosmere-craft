package com.csonyi.cosmerecraft.util;

import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.INVESTITURE_LIQUID_TAG;

import com.csonyi.cosmerecraft.CosmereCraft;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.neoforged.neoforge.common.util.ITeleporter;

public class ScadrialTeleporter implements ITeleporter {

  private static final int MAX_PORTAL_DISTANCE = 16;
  private static final BlockPos WELL_PLACEMENT_OFFSET = new BlockPos(-4, -1, -4);
  private static final TagKey<Structure> ANCIENT_MEDALLION_LOCATED = TagKey.create(
      Registries.STRUCTURE,
      ResourceUtils.modLocation("ancient_medallion_located"));
  private static final ResourceKey<PoiType> WELL_OF_ASCENSION = ResourceKey.create(
      Registries.POINT_OF_INTEREST_TYPE,
      ResourceUtils.modLocation("well_of_ascension"));
  private static final ResourceLocation WELL_OF_ASCENSION_STRUCTURE = ResourceUtils.modLocation(
      "well_of_ascension/well_of_ascension");

  private final ServerLevel level;
  private final boolean isScadrial;

  public ScadrialTeleporter(ServerLevel level) {
    this.level = level;
    this.isScadrial = level.dimension().equals(CosmereCraft.SCADRIAL);
  }

  @Override
  public boolean isVanilla() {
    return false;
  }

  @Override
  public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
    return false;
  }

  @Override
  public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw,
      Function<Boolean, Entity> repositionEntity) {
    var foundPos = findFirstNonLiquidBlockAround(destWorld, entity.blockPosition())
        .map(BlockPos::above)
        .orElse(entity.blockPosition());
    entity.setPos(foundPos.getCenter());
    return ITeleporter.super.placeEntity(entity, currentWorld, destWorld, yaw, repositionEntity);
  }

  public void generateWellIfNeeded(BlockPos startPortalPos) {
    if (isScadrial) {
      return;
    }
    var wellPos = findWellOnTheOtherSide(startPortalPos, level.getWorldBorder());
    if (wellPos.isEmpty()) {
      generateWell(startPortalPos);
    }
  }

  private Optional<BlockPos> findWellOnTheOtherSide(BlockPos startPortalPos, WorldBorder worldBorder) {
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

  private void generateWell(BlockPos startPortalPos) {
    level.getStructureManager().get(WELL_OF_ASCENSION_STRUCTURE)
        .ifPresent(structure -> structure.placeInWorld(
            level, WELL_PLACEMENT_OFFSET, startPortalPos,
            new StructurePlaceSettings(), level.random, 2));
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

  public static Optional<BlockPos> findFirstNonLiquidBlockAround(ServerLevel serverLevel, BlockPos destinationPos) {
    return BlockPos.betweenClosedStream(
            destinationPos.offset(-3, 0, -3),
            destinationPos.offset(3, 0, 3))
        .filter(blockPos -> !serverLevel.getBlockState(blockPos).is(INVESTITURE_LIQUID_TAG))
        .findFirst();
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
