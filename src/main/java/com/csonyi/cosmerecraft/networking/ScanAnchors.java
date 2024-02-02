package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.allomancy.Allomancy;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ScanAnchors {

  public ScanAnchors() {
  }

  public ScanAnchors(FriendlyByteBuf buffer) {
  }

  public void toBytes(FriendlyByteBuf buffer) {
  }

  public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
    NetworkEvent.Context context = contextSupplier.get();

    context.enqueueWork(() -> {
      var player = context.getSender();
      if (player != null) {
        Allomancy.scanForAnchors(player);
      }
    });
    return true;
  }

}
