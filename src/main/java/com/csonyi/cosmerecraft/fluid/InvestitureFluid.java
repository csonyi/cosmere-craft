package com.csonyi.cosmerecraft.fluid;

import static com.csonyi.cosmerecraft.CosmereCraftFluids.INVESTITURE_FLUID_FLOWING;
import static com.csonyi.cosmerecraft.CosmereCraftFluids.INVESTITURE_FLUID_SOURCE;
import static com.csonyi.cosmerecraft.CosmereCraftFluids.INVESTITURE_FLUID_TYPE;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class InvestitureFluid extends BaseFlowingFluid {

  public InvestitureFluid() {
    super(new Properties(
        INVESTITURE_FLUID_TYPE::value,
        INVESTITURE_FLUID_SOURCE::value,
        INVESTITURE_FLUID_FLOWING::value));
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
