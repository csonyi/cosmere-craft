package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class MetalReserveChangeHandler {

  private static final MetalReserveChangeHandler INSTANCE = new MetalReserveChangeHandler();

  public static MetalReserveChangeHandler getInstance() {
    return INSTANCE;
  }

  public void handleData(final MetalReserveChange message, PlayPayloadContext context) {
    context.player()
        .ifPresent(player -> message.metal.updateState(player, message));
  }

  public record MetalReserveChange(AllomanticMetal metal, int amount, boolean isInUse, boolean isAvailable) implements CustomPacketPayload {

    public static final ResourceLocation ID = CosmereCraft.createResourceLocation("metal_reserve_change");

    public MetalReserveChange(final FriendlyByteBuf friendlyByteBuf) {
      this(
          friendlyByteBuf.readEnum(AllomanticMetal.class),
          friendlyByteBuf.readInt(),
          friendlyByteBuf.readBoolean(),
          friendlyByteBuf.readBoolean());
    }

    @Override
    public void write(final @NotNull FriendlyByteBuf friendlyByteBuf) {
      friendlyByteBuf.writeEnum(metal);
      friendlyByteBuf.writeInt(amount);
      friendlyByteBuf.writeBoolean(isInUse);
      friendlyByteBuf.writeBoolean(isAvailable);
    }

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }
  }

}
