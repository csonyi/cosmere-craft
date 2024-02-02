package com.csonyi.cosmerecraft.networking;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MetalReserveChange {

  public MetalReserveChange() {
  }

  public MetalReserveChange(FriendlyByteBuf buffer) {
  }

  public void toBytes(FriendlyByteBuf buffer) {
  }

  public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
    NetworkEvent.Context context = contextSupplier.get();

    context.enqueueWork(() -> {
    });
    return true;
  }

}
