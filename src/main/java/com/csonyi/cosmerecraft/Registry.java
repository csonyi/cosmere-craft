package com.csonyi.cosmerecraft;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.networking.MetalStateUpdateHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);
  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.createItems(CosmereCraft.MOD_ID);
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.createBlocks(CosmereCraft.MOD_ID);
  public static final DeferredRegister<Fluid> FLUIDS =
      DeferredRegister.create(Registries.FLUID, CosmereCraft.MOD_ID);
  public static final DeferredRegister<FluidType> FLUID_TYPES =
      DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, CosmereCraft.MOD_ID);

  public static void register(IEventBus modEventBus) {
    // NeoForge.EVENT_BUS.register(this);
    CREATIVE_MODE_TABS.register(modEventBus);
    CREATIVE_MODE_TABS.register("cosmerecraft", () -> CreativeModeTab.builder()
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(Items.IRON_NUGGET::getDefaultInstance)
        .displayItems(CosmereCraftItems::generateDisplayItems)
        .build());

    ATTACHMENT_TYPES.register(modEventBus);
    ITEMS.register(modEventBus);
    BLOCKS.register(modEventBus);
    FLUIDS.register(modEventBus);
    FLUID_TYPES.register(modEventBus);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);
  }

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

  public static DeferredRegister<AttachmentType<?>> attachmentTypes() {
    return ATTACHMENT_TYPES;
  }

  public static DeferredRegister<Item> items() {
    return ITEMS;
  }

  public static DeferredRegister<Block> blocks() {
    return BLOCKS;
  }

  public static DeferredRegister<Fluid> fluids() {
    return FLUIDS;
  }

  public static DeferredRegister<FluidType> fluidTypes() {
    return FLUID_TYPES;
  }
}
