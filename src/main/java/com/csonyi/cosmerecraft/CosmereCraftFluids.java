package com.csonyi.cosmerecraft;

import static com.csonyi.cosmerecraft.Registry.fluidTypes;
import static com.csonyi.cosmerecraft.Registry.fluids;

import com.csonyi.cosmerecraft.fluid.InvestitureFluid;
import com.csonyi.cosmerecraft.fluid.InvestitureFluidType;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class CosmereCraftFluids {


  public static final Holder<FluidType> INVESTITURE_FLUID_TYPE = fluidTypes().register(
      "investiture_type",
      InvestitureFluidType::new);
  public static final Holder<Fluid> INVESTITURE_FLUID_SOURCE = fluids().register(
      "investiture",
      InvestitureFluid.Source::new);

  public static final Holder<Fluid> INVESTITURE_FLUID_FLOWING = fluids().register(
      "investiture_flowing",
      InvestitureFluid.Flowing::new);

  private static final Map<Fluid, Item> BUCKETS = Map.of(
      INVESTITURE_FLUID_SOURCE.value(), CosmereCraftItems.INVESTITURE_BUCKET.value());

  public static Item getBucketItem(Fluid fluid) {
    return BUCKETS.get(fluid);
  }
}
