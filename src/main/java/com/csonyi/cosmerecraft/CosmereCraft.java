package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import com.csonyi.cosmerecraft.registry.CosmereCraftFeatures;
import com.csonyi.cosmerecraft.registry.CosmereCraftFluids;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.csonyi.cosmerecraft.registry.CosmereCraftPois;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredRegister;

// TODO:
//  * Make allomancy work
//  * Allomantic vibration system based on the skulk system
//    * Rudimentary version to find the well of ascension
//    * Unsealed nicrosilmind
//  * Banner patterns for allomantic metal icons, created with a corresponding metal item

/**
 * Main class of the mod, the entrypoint of the application.
 */
@Mod(CosmereCraft.MOD_ID)
public class CosmereCraft {

  // private static final Logger LOGGER = LogUtils.getLogger();

  /**
   * The mod id of the mod, this is referenced everywhere it is needed.
   */
  public static final String MOD_ID = "cosmerecraft";

  /**
   * The deferred register for creative mode tab of the mod.
   */
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

  /**
   * The resource key for the scadrial dimension. Dimension properties are defined in the data pack.
   */
  public static final ResourceKey<Level> SCADRIAL =
      ResourceKey.create(Registries.DIMENSION, ResourceUtils.modResourceLocation("scadrial"));

  /**
   * The constructor of the mod, registers and initializes all the features of the mod.
   *
   * @param modEventBus the event bus of the mod
   */
  public CosmereCraft(IEventBus modEventBus) {
    // NeoForge.EVENT_BUS.register(this); // only needed if you want to listen to NeoForge events

    CREATIVE_MODE_TABS.register(modEventBus);
    CREATIVE_MODE_TABS.register("cosmerecraft", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.%s".formatted(MOD_ID)))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .withTabsImage(AllomanticMetal.IRON.textureLocation())
        .displayItems(CosmereCraftItems::generateDisplayItems)
        .build());

    CosmereCraftFluids.register(modEventBus);
    CosmereCraftItems.register(modEventBus);
    CosmereCraftBlocks.register(modEventBus);
    CosmereCraftFeatures.register(modEventBus);
    CosmereCraftAttachments.register(modEventBus);
    CosmereCraftEntities.register(modEventBus);
    CosmereCraftPois.register(modEventBus);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);
  }

}
