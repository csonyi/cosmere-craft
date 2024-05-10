package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.capability.anchors.ChunkAnchors;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class AnchorUpdateHandler extends NetworkHandler {

  public static void updateClient(ServerPlayer player, ChunkPos chunkPos, HashSet<BlockPos> anchors) {
    sendToPlayer(player, new Anchors(chunkPos, anchors));
  }

  public static void handleUpdate(Anchors packet, PlayPayloadContext context) {
    context.level()
        .ifPresent(level -> {
          ChunkAnchors.of(level.getChunk(packet.chunkPos.x, packet.chunkPos.z)).setAnchors(packet.anchors);
        });
  }

  public record Anchors(ChunkPos chunkPos, HashSet<BlockPos> anchors) implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceUtils.modLocation("anchor_update");

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static Anchors read(FriendlyByteBuf buffer) {
      return new Anchors(
          buffer.readChunkPos(),
          buffer.readCollection(HashSet::new, FriendlyByteBuf::readBlockPos));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
      buffer.writeChunkPos(chunkPos);
      buffer.writeCollection(anchors, FriendlyByteBuf::writeBlockPos);
    }
  }
}
