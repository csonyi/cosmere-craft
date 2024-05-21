package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.capability.anchors.ChunkAnchors;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class AnchorUpdateHandler {

  public static void replaceClientAnchors(ServerPlayer player, ChunkPos chunkPos, List<BlockPos> anchors) {
    PacketDistributor.sendToPlayer(player, new Anchors(chunkPos, anchors));
  }

  public static void handleUpdate(Anchors packet, IPayloadContext context) {
    var chunkAnchors = ChunkAnchors.ofExisting(context.player().level().getChunk(packet.chunkPos.x, packet.chunkPos.z));
    chunkAnchors.setAnchors(packet.anchors);
    chunkAnchors.saveAnchors();
  }

  public record Anchors(ChunkPos chunkPos, List<BlockPos> anchors) implements
      CustomPacketPayload {

    public static final Type<Anchors> TYPE = new Type<>(ResourceUtils.modLocation("anchor_update"));
    public static final StreamCodec<FriendlyByteBuf, Anchors> CODEC = StreamCodec.composite(
        NeoForgeStreamCodecs.CHUNK_POS, Anchors::chunkPos,
        ByteBufCodecs.collection(ArrayList::new, BlockPos.STREAM_CODEC), Anchors::anchors,
        Anchors::new);

    @Override
    public @NotNull Type<Anchors> type() {
      return TYPE;
    }
  }
}
