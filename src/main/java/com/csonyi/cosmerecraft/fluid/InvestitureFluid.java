package com.csonyi.cosmerecraft.fluid;

import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftFluids;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class InvestitureFluid extends BaseFlowingFluid {

  public InvestitureFluid() {
    super(createProperties());
  }

  private static Properties createProperties() {
    return new Properties(
        CosmereCraftFluids.INVESTITURE_FLUID_TYPE::value,
        CosmereCraftFluids.INVESTITURE::value,
        CosmereCraftFluids.FLOWING_INVESTITURE::value)
        .bucket(CosmereCraftItems.INVESTITURE_BUCKET::value)
        .block(CosmereCraftBlocks.INVESTITURE_LIQUID::value);
  }

  public static class Source extends InvestitureFluid {

    @Override
    public int getAmount(FluidState fluidState) {
      return 8;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
      return true;
    }
  }

  public static class Flowing extends InvestitureFluid {

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
}
