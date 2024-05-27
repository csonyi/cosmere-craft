package com.csonyi.cosmerecraft.networking;

import static java.util.function.Predicate.not;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ClientMetalStateQueryHandler {

  private static final MetalStatePacket AFTER_ALUMINUM_STATE_PACKET = new MetalStatePacket(
      AllomanticMetal.stream(not(AllomanticMetal::isGodMetal))
          .map(metal -> new AllomanticMetal.State(metal, 0, false, null))
          .collect(Collectors.toSet()));

  public static void initializeLocalMetalStates() {
    PacketDistributor.sendToServer(MetalStateQuery.all());
  }

  public static void queryMetalStatesFromServer(Collection<AllomanticMetal> metals) {
    if (!metals.isEmpty()) {
      PacketDistributor.sendToServer(new MetalStateQuery(metals));
    }
  }

  public static void turnOffMetalOnClient(ServerPlayer player, AllomanticMetal metal) {
    PacketDistributor.sendToPlayer(player, turnOffMetalPacket(metal));
  }

  public static void wipeReservesOnClient(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, AFTER_ALUMINUM_STATE_PACKET);
  }

  public static void handleResponse(MetalStatePacket packet, IPayloadContext context) {
    IAllomancy.of(context.player()).setStates(packet.metalStates);
  }

  public static void handleQuery(MetalStateQuery packet, IPayloadContext context) {
    context.reply(new MetalStatePacket(IAllomancy.of(context.player()).getStates()));
  }

  private static MetalStatePacket turnOffMetalPacket(AllomanticMetal metal) {
    return new MetalStatePacket(Set.of(new AllomanticMetal.State(metal, null, false, null)));
  }

  // TODO: rework if needed
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
