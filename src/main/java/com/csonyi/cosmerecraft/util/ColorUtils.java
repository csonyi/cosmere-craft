package com.csonyi.cosmerecraft.util;

public class ColorUtils {

  public static final int WHITE = 0xffffff;
  // int GRAY = 0x808080;
  public static final int[] ALLOMANTIC_METALS = {
      0xAAAAAA, // STEEL
      0xC5C5C5, // IRON
      0xC8DEDC, // ZINC
      0xFFD971, // BRASS

      0xD3D3D1, // PEWTER
      0xE2F4FB, // TIN
      0xC15A36, // COPPER
      0xE4AB20, // BRONZE

      0xBFCACD, // DURALUMIN
      0xDCE4E6, // ALUMINUM
      0xE9B116, // GOLD
      0xF5F180, // ELECTRUM

      0xD8D8D8, // NICROSIL
      0xF7F7F7, // CHROMIUM
      0xFCD870, // CADMIUM
      0x9C4E31, // BENDALLOY

      0x8DACD6, // ATIUM
      0xBB905C // LERASIUM
  };
  public static final int[] METALS = {
      0xA3E0CC, // BISMUTH
      0xA3B7F4, // LEAD
      0xC7C0AE, // NICKEL
      0xD4DDE2, // SILVER
  };

  public static float[] getColor(int color) {
    var hexString = Integer.toHexString(color);
    return new float[]{
        Integer.valueOf(hexString.substring(0, 2), 16),
        Integer.valueOf(hexString.substring(2, 4), 16),
        Integer.valueOf(hexString.substring(4, 6), 16),
        255
    };
  }
}
