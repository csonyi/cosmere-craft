package com.csonyi.cosmerecraft.networking;

import com.csonyi.cosmerecraft.CosmereCraft;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {

  private static final ResourceLocation CHANNEL_ID = new ResourceLocation(CosmereCraft.MOD_ID, "network_channel");
  private static final String PROTOCOL_VERSION = "1.0";
  private static final List<Class<?>> packetTypes = List.of(
      MetalReserveChange.class,
      ScanAnchors.class
  );
  private static SimpleChannel INSTANCE;

  private static int packetId = 0;

  private static int nextId() {
    return packetId++;
  }

  public static void registerChannel() {
    INSTANCE = NetworkRegistry.ChannelBuilder
        .named(CHANNEL_ID)
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .clientAcceptedVersions(version -> true)
        .serverAcceptedVersions(version -> true)
        .simpleChannel();

    INSTANCE.messageBuilder(MetalReserveChange.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
        .decoder(MetalReserveChange::new)
        .encoder(MetalReserveChange::toBytes)
        .consumerNetworkThread(MetalReserveChange::handle)
        .add();

    INSTANCE.messageBuilder(ScanAnchors.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
        .decoder(ScanAnchors::new)
        .encoder(ScanAnchors::toBytes)
        .consumerNetworkThread(ScanAnchors::handle)
        .add();
  }

  public static <MESSAGE> void sendToServer(MESSAGE message) {
    INSTANCE.sendToServer(message);
  }

}
