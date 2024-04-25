package com.csonyi.cosmerecraft.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MathUtils {

  public static double vectorAngleInDegrees(Vec3 a, Vec3 b) {
    return Math.acos(a.dot(b) / (a.length() * b.length())) * Mth.RAD_TO_DEG;
  }

  public static double lorp(double d, double x1, double x2) {
    return Math.pow(x2, d) * Math.pow(x1, 1 - d);
  }

}
