package com.csonyi.cosmerecraft.util;

import net.minecraft.world.level.Level;

public class TickUtils {

  private final Level level;

  public TickUtils(Level level) {
    this.level = level;
  }

  public static TickUtils forLevel(Level level) {
    return new TickUtils(level);
  }

  private static final int TICKS_PER_SECOND = 20;

  public static int secondsToTicks(int seconds) {
    return seconds * TICKS_PER_SECOND;
  }

  public static int minutesToTicks(int minutes) {
    return secondsToTicks(minutes * 60);
  }

  public boolean everyNthSecond(int n) {
    return level.getGameTime() % secondsToTicks(n) == 0;
  }

}
