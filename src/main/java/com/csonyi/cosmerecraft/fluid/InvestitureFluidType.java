package com.csonyi.cosmerecraft.fluid;

import com.csonyi.cosmerecraft.CosmereCraft;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

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
        return CosmereCraft.createResourceLocation("block/investiture_still");
      }

      @Override
      public @NotNull ResourceLocation getFlowingTexture() {
        return CosmereCraft.createResourceLocation("block/investiture_flowing");
      }
    });
  }
}
