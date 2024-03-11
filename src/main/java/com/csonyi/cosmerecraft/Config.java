package com.csonyi.cosmerecraft;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

  @Mod.EventBusSubscriber(modid = CosmereCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class Server {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.BooleanValue ALLOMANCY_NEEDS_METAL_NEARBY = BUILDER
        .comment("Whether allomancy needs metal nearby to work")
        .define("allomancyNeedsMetalNearby", true);
    private static final ModConfigSpec.IntValue COLLECTIVE_ALLOMANTIC_CAPACITY = BUILDER
        .comment("The collective amount of metals that can be stored in a player's metal reserves")
        .defineInRange("collectiveAllomanticCapacity", 60 * 60 * 20, 0, Integer.MAX_VALUE); // 1 hour in ticks (72 000)
    private static final ModConfigSpec.IntValue METAL_PORTION_BURN_TICKS = BUILDER
        .comment("How many \"ticks\" of burning a single portion of metal lasts")
        .defineInRange("metalPortionBurnTicks", 10 * 60 * 20, 0, Integer.MAX_VALUE); // 10 minutes in ticks (12 000)

    private static final ModConfigSpec.IntValue MAX_STEEL_PUSH_DISTANCE = BUILDER
        .comment("Maximum distance a steel push can affect things (in blocks)")
        .defineInRange("steelPushMaxDistance", 30, 0, Integer.MAX_VALUE);
    public static boolean allomancyNeedsMetalNearby;
    public static int collectiveAllomanticCapacity;
    public static int metalPortionBurnTicks;
    public static int maxSteelPushDistance;
    public static ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
      allomancyNeedsMetalNearby = ALLOMANCY_NEEDS_METAL_NEARBY.get();
      collectiveAllomanticCapacity = COLLECTIVE_ALLOMANTIC_CAPACITY.get();
      metalPortionBurnTicks = METAL_PORTION_BURN_TICKS.get();
      maxSteelPushDistance = MAX_STEEL_PUSH_DISTANCE.get();
    }
  }
}
