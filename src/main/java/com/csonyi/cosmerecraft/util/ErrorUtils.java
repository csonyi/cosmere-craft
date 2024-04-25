package com.csonyi.cosmerecraft.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class ErrorUtils {

  private static final Logger LOGGER = LogUtils.getLogger();


  public static void handleError(String methodName, Exception e) {
    LOGGER.error("An error occurred in {}: {}", methodName, e.getMessage());
  }
}
