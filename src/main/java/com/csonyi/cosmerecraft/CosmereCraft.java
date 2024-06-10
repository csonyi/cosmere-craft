package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.client.ClientConfig;
import com.csonyi.cosmerecraft.datagen.CosmereCraftArchaeologyLootModifierProvider;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import com.csonyi.cosmerecraft.registry.CosmereCraftFeatures;
import com.csonyi.cosmerecraft.registry.CosmereCraftFluids;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.csonyi.cosmerecraft.registry.CosmereCraftStructures;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredRegister;

// TODO:
//  * Make allomancy work
//  * Allomantic vibration system based on the skulk system
//  * Banner patterns for allomantic metal icons, created with a corresponding metal item
//  * remove anchors when spawning iron golem
//  * configurable medallion drop rate

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
      ResourceKey.create(Registries.DIMENSION, ResourceUtils.modLocation("scadrial"));

  public static final ResourceKey<Biome> SCADRIAL_PLAINS =
      ResourceKey.create(Registries.BIOME, ResourceUtils.modLocation("scadrial_plains"));

  /**
   * The constructor of the mod, registers and initializes all the features of the mod.
   *
   * @param modEventBus the event bus of the mod
   */
  public CosmereCraft(IEventBus modEventBus, ModContainer modContainer) {
    // NeoForge.EVENT_BUS.register(this); // only needed if you want to listen to NeoForge events

    CREATIVE_MODE_TABS.register(modEventBus);
    CREATIVE_MODE_TABS.register(MOD_ID, () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.%s".formatted(MOD_ID)))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(CosmereCraftItems.ANCIENT_MEDALLION.value()::getDefaultInstance)
        .displayItems(CosmereCraftItems::generateDisplayItems)
        .build());

    CosmereCraftFluids.register(modEventBus);
    CosmereCraftItems.register(modEventBus);
    CosmereCraftBlocks.register(modEventBus);
    CosmereCraftFeatures.register(modEventBus);
    CosmereCraftAttachments.register(modEventBus);
    CosmereCraftEntities.register(modEventBus);
    CosmereCraftStructures.register(modEventBus);
    CosmereCraftArchaeologyLootModifierProvider.register(modEventBus);

    modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
  }

}
