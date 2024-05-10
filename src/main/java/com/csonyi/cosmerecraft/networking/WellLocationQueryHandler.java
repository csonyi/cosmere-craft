package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.csonyi.cosmerecraft.util.ScadrialTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class WellLocationQueryHandler extends NetworkHandler {

  public static void queryWellLocation(BlockPos playerPos) {
    sendToServer(new WellLocationQuery(playerPos));
  }

  // server side handler
  public static void handleQuery(WellLocationQuery packet, PlayPayloadContext context) {
    var level = context.level();
    if (level.isEmpty() || !(level.get() instanceof ServerLevel serverLevel)) {
      return;
    }
    var wellLocation = ScadrialTeleporter.getWellLocation(serverLevel, packet.playerPos);

    wellLocation
        .map(WellLocationResponse::new)
        .ifPresent(context.replyHandler()::send);
  }

  // client side handler
  public static void handleResponse(WellLocationResponse packet, PlayPayloadContext context) {
    context.player()
        .ifPresent(player -> player.setData(
            CosmereCraftAttachments.TRACKED_WELL.get(),
            packet.wellPos));
  }

  public record WellLocationQuery(BlockPos playerPos) implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceUtils.modLocation("well_location_query");


    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static WellLocationQuery read(FriendlyByteBuf buffer) {
      return new WellLocationQuery(buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
      buffer.writeBlockPos(playerPos);
    }
  }

  public record WellLocationResponse(BlockPos wellPos) implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceUtils.modLocation("well_location_response");

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static WellLocationResponse read(FriendlyByteBuf buffer) {
      return new WellLocationResponse(buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
      buffer.writeBlockPos(wellPos);
    }

  }


}
