package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.Allomancy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class AnchorScanHandler {

  private static final AnchorScanHandler INSTANCE = new AnchorScanHandler();

  public static AnchorScanHandler getInstance() {
    return INSTANCE;
  }

  public void handleData(final AnchorScan anchorScan, PlayPayloadContext context) {
    context.player()
        .ifPresent(Allomancy::scanForAnchors);
  }

  public record AnchorScan() implements CustomPacketPayload {

    public static final ResourceLocation ID = CosmereCraft.createResourceLocation("anchor_scan");

    public AnchorScan(final FriendlyByteBuf friendlyByteBuf) {
      this();
    }

    @Override
    public void write(final @NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }
  }

}
