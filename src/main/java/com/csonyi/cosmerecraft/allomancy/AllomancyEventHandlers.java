package com.csonyi.cosmerecraft.allomancy;

import static com.csonyi.cosmerecraft.client.KeyMappings.SCAN_FOR_ANCHORS;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.capability.metalreserves.IMetalReserves;
import com.csonyi.cosmerecraft.capability.metalreserves.MetalReserveProvider;
import com.csonyi.cosmerecraft.networking.Messages;
import com.csonyi.cosmerecraft.networking.ScanAnchors;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

public class AllomancyEventHandlers {

  private static final Logger LOGGER = LogUtils.getLogger();

  public static void attachCapabilities(RegisterCapabilitiesEvent event) {
    event.registerEntity(

    );
    if (event.getObject() instanceof Player player) {
      if (!MetalReserveProvider.hasCapability(player)) {
        event.addCapability(IMetalReserves.CAPABILITY_ID, new MetalReserveProvider());
      }
    }
  }

  @Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
  public static class ForgeEventHandlers {

    @SubscribeEvent
    public static void handleAllomancyUpdates(LivingEvent.LivingTickEvent event) {
      if (!(event.getEntity() instanceof Player player)) {
        return;
      }

      MetalReserveProvider.ifPlayerHasCapability(player, metalReserves -> {
        if (player.jumping) {
          Allomancy.applySteelPush(player);
        }
      });
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
      if (event.isWasDeath()) {
        MetalReserveProvider.ifPlayerHasCapability(event.getOriginal(), oldCapability -> {
          MetalReserveProvider.ifPlayerHasCapability(event.getEntity(), newCapability -> {
            try (var level = event.getEntity().level()) {
              if (level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                newCapability.copyFrom(oldCapability);
              }
            } catch (IOException e) {
              LOGGER.error("Error while retrieving level in onPlayerCloned during metalReserve cloning: {}", e.getMessage());
            }
          });
        });
      }
    }

    @SubscribeEvent
    public void handleAnchorScan(TickEvent.ClientTickEvent event) {
      if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
        while (SCAN_FOR_ANCHORS.get().consumeClick()) {
          Messages.sendToServer(new ScanAnchors());
        }
      }
    }
  }

  @Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ModEventHandlers {

    @SubscribeEvent
    public void registerCapability(RegisterCapabilitiesEvent event) {
      event.register(IMetalReserves.class);
    }
  }
}
