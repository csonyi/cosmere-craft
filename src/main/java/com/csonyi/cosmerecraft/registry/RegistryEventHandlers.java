package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.datagen.CosmereCraftBlockStateProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftBlockTagsProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftItemModelProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftItemTagsProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftLootTableProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftRecipeProvider;
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
    event.register(CosmereCraftKeyMappings.OPEN_ALLOMANCY_GUI.get());
  }

  @SubscribeEvent
  public static void registerDataGenerators(final GatherDataEvent event) {
    var generator = event.getGenerator();
    var existingFileHelper = event.getExistingFileHelper();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    generator.addProvider(
        event.includeClient(),
        new CosmereCraftItemModelProvider(packOutput, existingFileHelper));
    generator.addProvider(
        event.includeClient(),
        new CosmereCraftBlockStateProvider(packOutput, existingFileHelper));

    generator.addProvider(
        event.includeServer(),
        new CosmereCraftBlockTagsProvider(
            packOutput,
            lookupProvider,
            existingFileHelper));
    generator.addProvider(
        event.includeServer(),
        new CosmereCraftItemTagsProvider(
            packOutput,
            lookupProvider,
            existingFileHelper));
    generator.addProvider(
        event.includeServer(),
        new CosmereCraftRecipeProvider(packOutput));
    generator.addProvider(
        event.includeServer(),
        new CosmereCraftLootTableProvider(packOutput));
  }
}
