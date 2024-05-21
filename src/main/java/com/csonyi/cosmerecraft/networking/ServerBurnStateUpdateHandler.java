package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ServerBurnStateUpdateHandler {

  public static void sendBurnStateUpdatesToServer(AllomanticMetal metal, boolean state) {
    PacketDistributor.sendToServer(new BurnStateUpdate(metal, state));
  }

  public static void updateBurnStateOnServer(BurnStateUpdate packet, IPayloadContext context) {
    var metalStateManager = new MetalStateManager(context.player());
    metalStateManager.setBurnState(packet.metal, packet.state);
  }

  public record BurnStateUpdate(AllomanticMetal metal, boolean state) implements CustomPacketPayload {

    public static final Type<BurnStateUpdate> TYPE = new Type<>(ResourceUtils.modLocation("burn_state_update"));
    public static final StreamCodec<FriendlyByteBuf, BurnStateUpdate> CODEC = StreamCodec.composite(
        AllomanticMetal.CODEC,
        BurnStateUpdate::metal,
        ByteBufCodecs.BOOL,
        BurnStateUpdate::state,
        BurnStateUpdate::new);

    @Override
    public @NotNull Type<BurnStateUpdate> type() {
      return TYPE;
    }
  }
}
