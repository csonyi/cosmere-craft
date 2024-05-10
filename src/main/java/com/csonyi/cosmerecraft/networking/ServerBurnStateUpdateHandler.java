package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.MetalStateManager;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ServerBurnStateUpdateHandler extends NetworkHandler {

  public static void sendBurnStateUpdatesToServer(AllomanticMetal metal, boolean state) {
    sendToServer(new BurnStateUpdate(metal, state));
  }

  public static void updateBurnStateOnServer(BurnStateUpdate packet, PlayPayloadContext context) {
    context.player()
        .map(MetalStateManager::new)
        .ifPresent(metalStateManager -> metalStateManager.setBurnState(packet.metal, packet.state));
  }

  public record BurnStateUpdate(AllomanticMetal metal, boolean state) implements CustomPacketPayload {

    public static ResourceLocation ID = ResourceUtils.modLocation("burn_state_update");

    @Override
    public @NotNull ResourceLocation id() {
      return ID;
    }

    public static BurnStateUpdate read(FriendlyByteBuf friendlyByteBuf) {
      return new BurnStateUpdate(
          friendlyByteBuf.readEnum(AllomanticMetal.class),
          friendlyByteBuf.readBoolean());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {
      friendlyByteBuf.writeEnum(metal);
      friendlyByteBuf.writeBoolean(state);
    }
  }
}
