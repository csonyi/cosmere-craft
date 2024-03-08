package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.CosmereCraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyEventHandlers {

  @SubscribeEvent
  public static void handleAllomancyUpdates(LivingEvent.LivingTickEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      Allomancy.of(player).tick();
    }
  }
}
