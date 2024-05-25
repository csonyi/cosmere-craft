package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.capability.allomancy.ExternalPhysicalMovement;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class SteelJumpHandler {

  public static void jump(ServerPlayer serverPlayer) {
    PacketDistributor.sendToPlayer(serverPlayer, SteelJump.get());
  }

  public static void handleOnClient(SteelJump packet, IPayloadContext context) {
    new ExternalPhysicalMovement(context.player()).applySteelPush();
  }

  public record SteelJump() implements CustomPacketPayload {

    public static final SteelJump INSTANCE = new SteelJump();
    public static final Type<SteelJump> TYPE = new Type<>(ResourceUtils.modLocation("steel_jump"));
    public static final StreamCodec<ByteBuf, SteelJump> CODEC = StreamCodec.unit(INSTANCE);

    public static SteelJump get() {
      return INSTANCE;
    }

    @Override
    public @NotNull Type<SteelJump> type() {
      return TYPE;
    }
  }
}
