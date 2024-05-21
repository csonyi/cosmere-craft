package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.csonyi.cosmerecraft.util.ScadrialTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class WellLocationQueryHandler {

  public static void queryWellLocation(BlockPos playerPos) {
    PacketDistributor.sendToServer(new WellLocationQuery(playerPos));
  }

  // server side handler
  public static void handleQuery(WellLocationQuery packet, IPayloadContext context) {
    var serverLevel = (ServerLevel) context.player().level();
    ScadrialTeleporter.getWellLocation(serverLevel, packet.playerPos)
        .map(WellLocationResponse::new)
        .ifPresent(context::reply);
  }

  // client side handler
  public static void handleResponse(WellLocationResponse packet, IPayloadContext context) {
    context.player()
        .setData(
            CosmereCraftAttachments.TRACKED_WELL.get(),
            packet.wellPos);
  }

  public record WellLocationQuery(BlockPos playerPos) implements CustomPacketPayload {

    public static final Type<WellLocationQuery> TYPE = new Type<>(ResourceUtils.modLocation("well_location_query"));
    public static final StreamCodec<FriendlyByteBuf, WellLocationQuery> CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC,
        WellLocationQuery::playerPos,
        WellLocationQuery::new);

    @Override
    public @NotNull Type<WellLocationQuery> type() {
      return TYPE;
    }
  }

  public record WellLocationResponse(BlockPos wellPos) implements CustomPacketPayload {

    public static final Type<WellLocationResponse> TYPE = new Type<>(ResourceUtils.modLocation("well_location_response"));
    public static final StreamCodec<FriendlyByteBuf, WellLocationResponse> CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC,
        WellLocationResponse::wellPos,
        WellLocationResponse::new);

    @Override
    public @NotNull Type<WellLocationResponse> type() {
      return TYPE;
    }
  }


}
