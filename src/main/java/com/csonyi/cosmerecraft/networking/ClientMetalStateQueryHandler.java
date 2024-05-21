package com.csonyi.cosmerecraft.networking;

import static java.util.function.Predicate.not;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMetalStateQueryHandler {

  public static void initializeLocalMetalStates() {
    PacketDistributor.sendToServer(MetalStateQuery.all());
  }

  public static void queryMetalStatesFromServer(Collection<AllomanticMetal> metals) {
    if (!metals.isEmpty()) {
      PacketDistributor.sendToServer(new MetalStateQuery(metals));
    }
  }

  public static void handleResponse(MetalStatePacket packet, IPayloadContext context) {
    var metalStateManager = new MetalStateManager(context.player());
    metalStateManager.setStates(packet.metalStates);
  }

  public static void handleQuery(MetalStateQuery packet, IPayloadContext context) {
    var metalStateManager = new MetalStateManager(context.player());
    var states = packet.metals.stream()
        .map(metalStateManager::getState)
        .collect(Collectors.toSet());
    context.reply(new MetalStatePacket(states));
  }

  public record MetalStateQuery(Collection<AllomanticMetal> metals) implements CustomPacketPayload {

    public static final Type<MetalStateQuery> TYPE = new Type<>(ResourceUtils.modLocation("metal_state_query"));
    public static final StreamCodec<FriendlyByteBuf, MetalStateQuery> CODEC = StreamCodec.composite(
        ByteBufCodecs.collection(HashSet::new, AllomanticMetal.CODEC), MetalStateQuery::metals,
        MetalStateQuery::new);

    @Override
    public @NotNull Type<MetalStateQuery> type() {
      return TYPE;
    }

    public static MetalStateQuery all() {
      return new MetalStateQuery(AllomanticMetal.stream(not(AllomanticMetal::isGodMetal)).collect(Collectors.toSet()));
    }
  }

  public record MetalStatePacket(Set<AllomanticMetal.State> metalStates) implements CustomPacketPayload {

    public static final Type<MetalStatePacket> TYPE = new Type<>(ResourceUtils.modLocation("metal_state_packet"));
    public static final StreamCodec<FriendlyByteBuf, MetalStatePacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.collection(HashSet::new, AllomanticMetal.State.CODEC), MetalStatePacket::metalStates,
        MetalStatePacket::new);

    @Override
    public @NotNull Type<MetalStatePacket> type() {
      return TYPE;
    }
  }

}
