package com.csonyi.cosmerecraft.util;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class TickUtils {

  private final Level level;

  public TickUtils(Level level) {
    this.level = level;
  }

  private static final int TICKS_PER_SECOND = 20;

  public static int secondsToTicks(double seconds) {
    return Mth.floor(seconds * TICKS_PER_SECOND);
  }

  public static int minutesToTicks(int minutes) {
    return secondsToTicks(minutes * 60);
  }

  public static Component toTimeFormat(long ticks) {
    long seconds = ticks / TICKS_PER_SECOND;
    long minutes = seconds / 60;
    long hours = minutes / 60;
    return Component.literal("%02d:%02d:%02d".formatted(hours, minutes % 60, seconds % 60));
  }

  public boolean everyNthSecond(double n) {
    return everyNthTick(secondsToTicks(n));
  }

  public boolean everyNthTick(int n) {
    return level.getGameTime() % n == 0;
  }

}
