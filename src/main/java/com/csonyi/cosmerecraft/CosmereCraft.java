package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.allomancy.AllomancyEventHandlers;
import com.csonyi.cosmerecraft.networking.Messages;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
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
  private static final Logger LOGGER = LogUtils.getLogger();

  public CosmereCraft(IEventBus modEventBus) {

    modEventBus.addListener(this::commonSetup);
    NeoForge.EVENT_BUS.addListener(AllomancyEventHandlers::attachCapabilities);

    CosmereCraftItems.register(modEventBus);
    CREATIVE_MODE_TABS.register(modEventBus);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);
  }

  public static ResourceLocation createResourceLocation(String path) {
    return new ResourceLocation(MOD_ID, path);
  }

  private void commonSetup(FMLCommonSetupEvent event) {
    LOGGER.info("The mists are gathering in the overworld...");
    Messages.registerChannel();
  }
}
