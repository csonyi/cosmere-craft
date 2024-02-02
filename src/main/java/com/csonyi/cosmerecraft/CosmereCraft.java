package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.networking.AnchorScanHandler;
import com.csonyi.cosmerecraft.networking.MetalReserveChangeHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

@Mod(CosmereCraft.MOD_ID)
public class CosmereCraft {

  public static final String MOD_ID = "cosmerecraft";
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
  public static final Holder<CreativeModeTab> CREATIVE_TAB =
      CREATIVE_MODE_TABS.register("cosmerecraft", () -> CreativeModeTab.builder()
          .withTabsBefore(CreativeModeTabs.COMBAT)
          .icon(Items.IRON_NUGGET::getDefaultInstance)
          .displayItems(CosmereCraftItems::generateDisplayItems)
          .build());

  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES,
      MOD_ID);

  private static final Logger LOGGER = LogUtils.getLogger();

  public CosmereCraft(IEventBus modEventBus) {
    NeoForge.EVENT_BUS.register(this);
    CosmereCraftItems.register(modEventBus);
    ATTACHMENT_TYPES.register(modEventBus);
    CREATIVE_MODE_TABS.register(modEventBus);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);
  }

  public static ResourceLocation createResourceLocation(String path) {
    return new ResourceLocation(MOD_ID, path);
  }

  @SubscribeEvent
  public static void registerMessages(final RegisterPayloadHandlerEvent event) {
    final IPayloadRegistrar registrar = event.registrar(CosmereCraft.MOD_ID);
    registrar.play(
        MetalReserveChangeHandler.MetalReserveChange.ID,
        MetalReserveChangeHandler.MetalReserveChange::new,
        handler -> handler
            .client(MetalReserveChangeHandler.getInstance()::handleData));
    registrar.play(
        AnchorScanHandler.AnchorScan.ID,
        AnchorScanHandler.AnchorScan::new,
        handler -> handler
            .server(AnchorScanHandler.getInstance()::handleData));
  }
}
