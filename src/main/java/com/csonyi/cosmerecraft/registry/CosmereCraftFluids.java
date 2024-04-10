package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.fluid.InvestitureFluidType;
import com.csonyi.cosmerecraft.fluid.PreservationInvestitureFluid;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Contains the deferred registers and holders for the fluids of the mod, and registers them.
 */
public class CosmereCraftFluids {

  // Deferred registers
  public static final DeferredRegister<FluidType> FLUID_TYPES =
      DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, CosmereCraft.MOD_ID);
  public static final DeferredRegister<Fluid> FLUIDS =
      DeferredRegister.create(Registries.FLUID, CosmereCraft.MOD_ID);

  // FluidType and Fluid holders
  public static final Holder<FluidType> INVESTITURE_FLUID_TYPE = FLUID_TYPES.register(
      "investiture_type",
      InvestitureFluidType::new);
  public static final DeferredHolder<Fluid, FlowingFluid> INVESTITURE = FLUIDS.register(
      "investiture",
      PreservationInvestitureFluid.Source::new);
  public static final DeferredHolder<Fluid, FlowingFluid> INVESTITURE_PORTAL = FLUIDS.register(
      "portal_investiture",
      PreservationInvestitureFluid.PortalPoi::new);
  public static final Holder<Fluid> FLOWING_INVESTITURE = FLUIDS.register(
      "flowing_investiture",
      PreservationInvestitureFluid.Flowing::new);

  public static void register(IEventBus modEventBus) {
    FLUID_TYPES.register(modEventBus);
    FLUIDS.register(modEventBus);
  }
}
