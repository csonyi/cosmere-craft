package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.google.common.collect.Streams;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class AllomancyEventHandlers {


  @SubscribeEvent
  public static void handlePlayerCloneEvent(PlayerEvent.Clone event) {
    var originalPlayer = event.getOriginal();
    var newPlayer = event.getEntity();
    IAllomancy.of(newPlayer).copyFrom(originalPlayer, event.isWasDeath());
    if (event.isWasDeath() && !hadAncientMedal(originalPlayer)) {
      CosmereCraftAttachments.copyAttachments(originalPlayer, newPlayer, CosmereCraftAttachments.FOUND_MEDALLION);
    }
  }

  @SubscribeEvent
  public static void handleAllomancyUpdates(final PlayerTickEvent.Pre event) {
    IAllomancy.of(event.getEntity()).tick();
  }


  private static boolean hadAncientMedal(Player player) {
    return Streams.stream(player.getAllSlots())
        .anyMatch(CosmereCraftItems.ANCIENT_MEDALLION.value().getDefaultInstance()::equals);
  }

}
