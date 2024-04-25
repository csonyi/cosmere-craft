package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.datagen.CosmereCraftBlockTagsProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftItemTagsProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftLootTableProvider;
import com.csonyi.cosmerecraft.datagen.CosmereCraftRecipeProvider;
import com.csonyi.cosmerecraft.datagen.client.CosmereCraftBlockStateProvider;
import com.csonyi.cosmerecraft.datagen.client.CosmereCraftItemModelProvider;
import com.csonyi.cosmerecraft.datagen.client.lang.EnUsLanguageProvider;
import com.csonyi.cosmerecraft.datagen.client.lang.HuHuLanguageProvider;
import com.csonyi.cosmerecraft.entity.Inquisitor;
import com.csonyi.cosmerecraft.entity.InquisitorModel;
import com.csonyi.cosmerecraft.entity.InquisitorRenderer;
import com.csonyi.cosmerecraft.networking.MetalStateUpdateHandler;
import com.csonyi.cosmerecraft.networking.WellLocationQueryHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
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
        handler -> handler
            .server(MetalStateUpdateHandler::handleQuery));
    registrar.play(
        MetalStateUpdateHandler.MetalStatePacket.ID,
        MetalStateUpdateHandler.MetalStatePacket::read,
        handler -> handler
            .server(MetalStateUpdateHandler::handleResponse)
            .client(MetalStateUpdateHandler::handleResponse));
    registrar.play(
        WellLocationQueryHandler.WellLocationQuery.ID,
        WellLocationQueryHandler.WellLocationQuery::read,
        handler -> handler
            .server(WellLocationQueryHandler::handleQuery));
    registrar.play(
        WellLocationQueryHandler.WellLocationResponse.ID,
        WellLocationQueryHandler.WellLocationResponse::read,
        handler -> handler
            .client(WellLocationQueryHandler::handleResponse));
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
        new CosmereCraftRecipeProvider(packOutput));
    generator.addProvider(
        event.includeServer(),
        new CosmereCraftLootTableProvider(packOutput));
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
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.WORLD_SURFACE,
        Inquisitor::canSpawn,
        SpawnPlacementRegisterEvent.Operation.OR);
  }
}
