package com.csonyi.cosmerecraft.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class NetworkHandler {

  public static void sendToServer(CustomPacketPayload payload) {
    if (payload == null) {
      return;
    }
    PacketDistributor.SERVER.noArg().send(payload);
  }

  public static void sendToPlayer(ServerPlayer serverPlayer, CustomPacketPayload payload) {
    if (payload == null) {
      return;
    }
    PacketDistributor.PLAYER.with(serverPlayer).send(payload);
  }
}
