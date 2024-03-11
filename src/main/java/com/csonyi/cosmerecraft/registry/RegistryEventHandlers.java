package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.datagen.AshData;
import com.csonyi.cosmerecraft.networking.MetalStateUpdateHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEventHandlers {

  @SubscribeEvent
  public static void registerPayloads(final RegisterPayloadHandlerEvent event) {
    final IPayloadRegistrar registrar = event.registrar(MOD_ID);
    registrar.play(
        MetalStateUpdateHandler.MetalStateQuery.ID,
        MetalStateUpdateHandler.MetalStateQuery::read,
        handler -> handler.server(MetalStateUpdateHandler::handleQueryOnServer));
    registrar.play(
        MetalStateUpdateHandler.MetalStatePacket.ID,
        MetalStateUpdateHandler.MetalStatePacket::read,
        MetalStateUpdateHandler::handleState);
  }

  @SubscribeEvent
  public static void registerKeyBindings(final RegisterKeyMappingsEvent event) {
    // event.register(SCAN_FOR_ANCHORS.get());
  }

  @SubscribeEvent
  public static void registerDataGenerators(final GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var existingFileHelper = event.getExistingFileHelper();
    generator.addProvider(
        event.includeClient(),
        new AshData(packOutput, existingFileHelper));
  }
}
