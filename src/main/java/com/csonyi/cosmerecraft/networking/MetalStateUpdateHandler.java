package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class MetalStateUpdateHandler {

  public static void handleState(MetalStatePacket packet, PlayPayloadContext context) {
    context.player()
        .map(MetalStateManager::new)
        .ifPresent(metalStateManager -> metalStateManager.setState(packet.metalState));
  }

  public static void handleQueryOnServer(MetalStateQuery packet, PlayPayloadContext context) {
    context.player()
        .filter(player -> player instanceof ServerPlayer)
        .map(player -> (ServerPlayer) player)
        .map(MetalStateManager::new)
        .map(metalStateManager -> metalStateManager.getState(packet.metal))
        .map(MetalStatePacket::new)
        .ifPresent(context.replyHandler()::send);
  }

  public record MetalStateQuery(AllomanticMetal metal) implements CustomPacketPayload {

    public static ResourceLocation ID = CosmereCraft.createResourceLocation("metal_state_query");

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static MetalStateQuery read(FriendlyByteBuf friendlyByteBuf) {
      return new MetalStateQuery(friendlyByteBuf.readEnum(AllomanticMetal.class));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
      friendlyByteBuf.writeEnum(metal);
    }
  }

  public record MetalStatePacket(AllomanticMetal.State metalState) implements CustomPacketPayload {

    public static ResourceLocation ID = CosmereCraft.createResourceLocation("metal_state");

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static MetalStatePacket read(FriendlyByteBuf friendlyByteBuf) {
      return new MetalStatePacket(AllomanticMetal.State.read(friendlyByteBuf));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
      metalState.write(friendlyByteBuf);
    }
  }

}
