package com.csonyi.cosmerecraft.capability.allomancy;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.gui.AllomancyGui;
import com.csonyi.cosmerecraft.registry.CosmereCraftKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

@Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyEventHandlers {

  @SubscribeEvent
  public static void handleAllomancyUpdates(final LivingEvent.LivingTickEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      Allomancy.of(player).tick();
    }
  }

  @SubscribeEvent
  public static void handleAllomancyGuiOpen(final TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      while (CosmereCraftKeyMappings.OPEN_ALLOMANCY_GUI.get().consumeClick()) {
        Minecraft.getInstance().setScreen(new AllomancyGui());
      }
    }
  }
}
