package com.csonyi.cosmerecraft.client.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.MOD_ID;

import com.csonyi.cosmerecraft.client.capability.allomancy.LocalAllomancy;
import com.csonyi.cosmerecraft.registry.CosmereCraftCapabilities;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class LocalRegistryEventHandlers {

  @SubscribeEvent
  public static void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerEntity(
        CosmereCraftCapabilities.ALLOMANCY,
        EntityType.PLAYER,
        LocalAllomancy::register);
  }
}
