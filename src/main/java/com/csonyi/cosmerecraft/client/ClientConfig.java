package com.csonyi.cosmerecraft.client;

import com.csonyi.cosmerecraft.CosmereCraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(value = Dist.CLIENT, modid = CosmereCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {

  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

  private static final ModConfigSpec.IntValue ALLOMANTIC_LINE_RENDER_DISTANCE = BUILDER
      .comment("Max distance of anchors to render lines to")
      .defineInRange("allomanticLineRenderDistance", 16, 0, 64);

  public static int allomanticLineRenderDistance;

  public static ModConfigSpec SPEC = BUILDER.build();

  @SubscribeEvent
  public static void onLoad(final ModConfigEvent event) {
    allomanticLineRenderDistance = ALLOMANTIC_LINE_RENDER_DISTANCE.get();
  }
}
