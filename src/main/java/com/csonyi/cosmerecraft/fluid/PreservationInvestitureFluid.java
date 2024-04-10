package com.csonyi.cosmerecraft.fluid;

import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftFluids;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * The fluid that represents Preservation Investiture in the game.
 * <p>
 * Spawns in the "Well of Ascension" structure, and used to teleport between the overworld and Scadrial. Not infinite, non-replaceable by
 * blocks, but can be collected in a bucket. Will be given some useful applications in the future.
 * </p>
 */
public abstract class PreservationInvestitureFluid extends BaseFlowingFluid {

  public PreservationInvestitureFluid() {
    this(
        CosmereCraftFluids.INVESTITURE,
        CosmereCraftItems.INVESTITURE_BUCKET,
        CosmereCraftBlocks.INVESTITURE_LIQUID);
  }

  protected PreservationInvestitureFluid(
      Holder<Fluid> source, Holder<Item> bucket, DeferredBlock<LiquidBlock> block) {
    super(new Properties(
        CosmereCraftFluids.INVESTITURE_FLUID_TYPE::value,
        source::value,
        CosmereCraftFluids.FLOWING_INVESTITURE::value)
        .bucket(bucket::value)
        .block(block::value));
  }

  /**
   * The source block of the fluid. Always has an "amount" of 8.
   */
  public static class Source extends PreservationInvestitureFluid {

    public Source() {
      super();
    }

    protected Source(
        Holder<Fluid> source, Holder<Item> bucket, DeferredBlock<LiquidBlock> block) {
      super(source, bucket, block);
    }

    @Override
    public int getAmount(FluidState fluidState) {
      return 8;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
      return true;
    }
  }

  /**
   * The flowing block of the fluid. Has an "amount" between 1 and 7.
   */
  public static class Flowing extends PreservationInvestitureFluid {

    public Flowing() {
      super();
      this.registerDefaultState(
          getStateDefinition()
              .any()
              .setValue(LEVEL, 7));
    }

    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
      super.createFluidStateDefinition(builder);
      builder.add(LEVEL);
    }

    @Override
    public int getAmount(FluidState fluidState) {
      return fluidState.getValue(LEVEL);
    }

    @Override
    public boolean isSource(FluidState fluidState) {
      return false;
    }
  }

  public static class PortalPoi extends Source {

    public PortalPoi() {
      super(
          CosmereCraftFluids.INVESTITURE_PORTAL,
          CosmereCraftItems.INVESTITURE_PORTAL_BUCKET,
          CosmereCraftBlocks.INVESTITURE_PORTAL_BLOCK);
    }
  }
}
