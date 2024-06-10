package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.capability.allomancy.ServerAllomancy;
import com.csonyi.cosmerecraft.client.datagen.CosmereCraftBlockStateProvider;
import com.csonyi.cosmerecraft.client.datagen.CosmereCraftItemModelProvider;
import com.csonyi.cosmerecraft.client.datagen.lang.EnUsLanguageProvider;
import com.csonyi.cosmerecraft.client.datagen.lang.HuHuLanguageProvider;
import com.csonyi.cosmerecraft.client.entity.InquisitorModel;
import com.csonyi.cosmerecraft.client.entity.InquisitorRenderer;
import com.csonyi.cosmerecraft.datagen.CosmereCraftArchaeologyLootModifierProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftBlockTagsProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftItemTagsProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftLootTableProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftRecipeProvider;
import com.csonyi.cosmerecraft.entity.Inquisitor;
import com.csonyi.cosmerecraft.networking.AnchorUpdateHandler;
import com.csonyi.cosmerecraft.networking.ClientMetalStateQueryHandler;
import com.csonyi.cosmerecraft.networking.ServerBurnStateUpdateHandler;
import com.csonyi.cosmerecraft.networking.SteelJumpHandler;
import com.csonyi.cosmerecraft.networking.WellLocationQueryHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryEventHandlers {

  @SubscribeEvent
  public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
    var registrar = event.registrar(MOD_ID);
    registrar.playToServer(
        ClientMetalStateQueryHandler.MetalStateQuery.TYPE,
        ClientMetalStateQueryHandler.MetalStateQuery.CODEC,
        ClientMetalStateQueryHandler::handleQuery);
    registrar.playToClient(
        ClientMetalStateQueryHandler.MetalStatePacket.TYPE,
        ClientMetalStateQueryHandler.MetalStatePacket.CODEC,
        ClientMetalStateQueryHandler::handleResponse);
    registrar.playToServer(
        ServerBurnStateUpdateHandler.BurnStateUpdate.TYPE,
        ServerBurnStateUpdateHandler.BurnStateUpdate.CODEC,
        ServerBurnStateUpdateHandler::updateBurnStateOnServer);
    registrar.playToServer(
        WellLocationQueryHandler.WellLocationQuery.TYPE,
        WellLocationQueryHandler.WellLocationQuery.CODEC,
        WellLocationQueryHandler::handleQuery);
    registrar.playToClient(
        WellLocationQueryHandler.WellLocationResponse.TYPE,
        WellLocationQueryHandler.WellLocationResponse.CODEC,
        WellLocationQueryHandler::handleResponse);
    registrar.playToClient(
        AnchorUpdateHandler.Anchors.TYPE,
        AnchorUpdateHandler.Anchors.CODEC,
        AnchorUpdateHandler::handleUpdate);
    registrar.playToClient(
        SteelJumpHandler.SteelJump.TYPE,
        SteelJumpHandler.SteelJump.CODEC,
        SteelJumpHandler::handleOnClient);
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
    registerLanguageProviders(
        generator,
        event.includeClient(),
        packOutput);

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
        new CosmereCraftRecipeProvider(packOutput, lookupProvider));
    generator.addProvider(
        event.includeServer(),
        new CosmereCraftLootTableProvider(packOutput, lookupProvider));
    generator.addProvider(
        event.includeServer(),
        new CosmereCraftArchaeologyLootModifierProvider(packOutput, lookupProvider));
  }

  private static void registerLanguageProviders(DataGenerator generator, boolean includeClient, PackOutput packOutput) {
    generator.addProvider(
        includeClient,
        new EnUsLanguageProvider(packOutput));
    generator.addProvider(
        includeClient,
        new HuHuLanguageProvider(packOutput));
  }

  @SubscribeEvent
  public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(
        CosmereCraftEntities.INQUISITOR_ENTITY_TYPE.get(),
        InquisitorRenderer::new);
  }

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(
        InquisitorModel.LAYER_LOCATION,
        InquisitorModel::createBodyLayer);
  }

  @SubscribeEvent
  public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
    event.put(
        CosmereCraftEntities.INQUISITOR_ENTITY_TYPE.get(),
        Inquisitor.createAttributes().build());
  }

  @SubscribeEvent
  public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
    event.register(
        CosmereCraftEntities.INQUISITOR_ENTITY_TYPE.get(),
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.WORLD_SURFACE,
        Inquisitor::canSpawn,
        SpawnPlacementRegisterEvent.Operation.OR);
  }

  @SubscribeEvent
  public static void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerEntity(
        CosmereCraftCapabilities.ALLOMANCY,
        EntityType.PLAYER,
        ServerAllomancy::register);
  }
}
