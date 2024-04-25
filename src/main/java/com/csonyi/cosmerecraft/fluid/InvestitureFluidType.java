package com.csonyi.cosmerecraft.fluid;

import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

/**
 * The fluid type for the investiture fluid. This class is responsible for defining the properties of the fluid type, and the client side
 * rendering properties.
 */
public class InvestitureFluidType extends FluidType {

  public InvestitureFluidType() {
    super(Properties.create());
  }

  @Override
  public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
    consumer.accept(new IClientFluidTypeExtensions() {
      @Override
      public int getTintColor() {
        return 15590363;
      }

      @Override
      public @NotNull ResourceLocation getStillTexture() {
        return ResourceUtils.modLocation("block/investiture_still");
      }

      @Override
      public @NotNull ResourceLocation getFlowingTexture() {
        return ResourceUtils.modLocation("block/investiture_flowing");
      }
    });
  }
}
