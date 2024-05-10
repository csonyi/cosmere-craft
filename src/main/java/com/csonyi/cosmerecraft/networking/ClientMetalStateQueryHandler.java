package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMetalStateQueryHandler extends NetworkHandler {

  public static void queryMetalStatesFromServer() {
    sendToServer(new MetalStateQuery());
  }

  public static void handleResponse(MetalStatePacket packet, PlayPayloadContext context) {
    context.player()
        .map(MetalStateManager::new)
        .ifPresent(metalStateManager -> metalStateManager.setStates(packet.metalStates));
  }

  public static void handleQuery(MetalStateQuery packet, PlayPayloadContext context) {
    context.player()
        .map(MetalStateManager::new)
        .map(MetalStateManager::getStates)
        .map(MetalStatePacket::new)
        .ifPresent(context.replyHandler()::send);
  }

  public record MetalStateQuery() implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceUtils.modLocation("metal_state_query");

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static MetalStateQuery read(FriendlyByteBuf friendlyByteBuf) {
      return new MetalStateQuery();
    }

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }
  }

  public record MetalStatePacket(Set<AllomanticMetal.State> metalStates) implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceUtils.modLocation("metal_state_packet");

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static MetalStatePacket read(FriendlyByteBuf friendlyByteBuf) {
      return new MetalStatePacket(friendlyByteBuf.readCollection(
          HashSet::new,
          AllomanticMetal.State::read));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
      friendlyByteBuf.writeCollection(metalStates, (buffer, state) -> state.write(buffer));
    }
  }

}
