package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import com.csonyi.cosmerecraft.registry.CosmereCraftFeatures;
import com.csonyi.cosmerecraft.registry.CosmereCraftFluids;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(CosmereCraft.MOD_ID)
public class CosmereCraft {

  // private static final Logger LOGGER = LogUtils.getLogger();
  public static final String MOD_ID = "cosmerecraft";

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

  public static final ResourceKey<Level> SCADRIAL =
      ResourceKey.create(Registries.DIMENSION, createResourceLocation("scadrial"));

  public CosmereCraft(IEventBus modEventBus) {
    // NeoForge.EVENT_BUS.register(this);
    CREATIVE_MODE_TABS.register(modEventBus);
    CREATIVE_MODE_TABS.register("cosmerecraft", () -> CreativeModeTab.builder()
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(Items.IRON_NUGGET::getDefaultInstance)
        .displayItems(CosmereCraftItems::generateDisplayItems)
        .build());

    CosmereCraftFluids.register(modEventBus);
    CosmereCraftItems.register(modEventBus);
    CosmereCraftBlocks.register(modEventBus);
    CosmereCraftFeatures.register(modEventBus);
    CosmereCraftAttachments.register(modEventBus);
    CosmereCraftEntities.register(modEventBus);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);
  }

  public static ResourceLocation createResourceLocation(String path) {
    return new ResourceLocation(MOD_ID, path);
  }

}
