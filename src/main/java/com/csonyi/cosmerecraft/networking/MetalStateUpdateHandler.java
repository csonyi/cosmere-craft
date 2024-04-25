package com.csonyi.cosmerecraft.networking;

import static java.util.function.Predicate.not;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class MetalStateUpdateHandler {

  public static void queryMetalStatesFromServer() {
    AllomanticMetal.stream()
        .filter(not(AllomanticMetal::isGodMetal))
        .forEach(
            metal -> PacketDistributor.SERVER.noArg()
                .send(new MetalStateQuery(metal)));
  }

  public static void pushBurnStrengthUpdatesToServer(Stream<AllomanticMetal.State> burnStrengths) {
    burnStrengths.forEach(
        state -> PacketDistributor.SERVER.noArg()
            .send(new MetalStatePacket(state)));
  }

  public static void handleResponse(MetalStatePacket packet, PlayPayloadContext context) {
    context.player()
        .map(MetalStateManager::new)
        .ifPresent(metalStateManager -> metalStateManager.setState(packet.metalState));
  }

  public static void handleQuery(MetalStateQuery packet, PlayPayloadContext context) {
    context.player()
        .filter(player -> player instanceof ServerPlayer)
        .map(ServerPlayer.class::cast)
        .map(MetalStateManager::new)
        .map(metalStateManager -> metalStateManager.getState(packet.metal))
        .map(MetalStatePacket::new)
        .ifPresent(context.replyHandler()::send);
  }

  public record MetalStateQuery(AllomanticMetal metal) implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceUtils.modLocation("metal_state_query");

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

    public static ResourceLocation ID = ResourceUtils.modLocation("metal_state");

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
