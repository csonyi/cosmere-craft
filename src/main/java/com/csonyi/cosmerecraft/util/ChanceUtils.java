package com.csonyi.cosmerecraft.util;

import net.minecraft.util.RandomSource;

public final class ChanceUtils {

  private final RandomSource random;

  private ChanceUtils(RandomSource random) {
    this.random = random;
  }

  public static ChanceUtils from(RandomSource random) {
    return new ChanceUtils(random);
  }

  public boolean oneIn(int chance) {
    return random.nextInt(chance) == 0;
  }
}
