package com.csonyi.cosmerecraft.capability.allomancy;

import static com.csonyi.cosmerecraft.client.KeyMappings.SCAN_FOR_ANCHORS;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.networking.AnchorScanHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyEventHandlers {

  private static final Logger LOGGER = LogUtils.getLogger();

  @SubscribeEvent
  public static void handleAllomancyUpdates(LivingEvent.LivingTickEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }

    if (AllomanticMetal.STEEL.isAvailable(player)
        && AllomanticMetal.STEEL.isInUse(player)
        && AllomanticMetal.STEEL.canExpend(player)
        && player.jumping) {
      Allomancy.applySteelPush(player);
    }
  }

  @SubscribeEvent
  public void handleAnchorScan(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
      while (SCAN_FOR_ANCHORS.get().consumeClick()) {
        PacketDistributor.SERVER.noArg()
            .send(new AnchorScanHandler.AnchorScan());
      }
    }
  }
}
