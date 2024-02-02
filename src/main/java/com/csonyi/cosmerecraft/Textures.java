package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import java.util.EnumMap;
import java.util.Map;

public class Textures {

  public static final String METAL_PREFIX = "textures/icon/metal/";
  public static final Map<AllomanticMetal, String> METAL_TEXTURES = new EnumMap<>(AllomanticMetal.class) {{
    put(AllomanticMetal.STEEL, METAL_PREFIX + "steel_icon.png");
    put(AllomanticMetal.IRON, METAL_PREFIX + "iron_icon.png");
    put(AllomanticMetal.PEWTER, METAL_PREFIX + "pewter_icon.png");
    put(AllomanticMetal.TIN, METAL_PREFIX + "tin_icon.png");

    put(AllomanticMetal.ZINC, METAL_PREFIX + "zinc_icon.png");
    put(AllomanticMetal.BRASS, METAL_PREFIX + "brass_icon.png");
    put(AllomanticMetal.COPPER, METAL_PREFIX + "copper_icon.png");
    put(AllomanticMetal.BRONZE, METAL_PREFIX + "bronze_icon.png");

    put(AllomanticMetal.DURALUMIN, METAL_PREFIX + "duralumin_icon.png");
    put(AllomanticMetal.ALUMINUM, METAL_PREFIX + "aluminum_icon.png");
    put(AllomanticMetal.NICROSIL, METAL_PREFIX + "nicrosil_icon.png");
    put(AllomanticMetal.CHROMIUM, METAL_PREFIX + "chromium_icon.png");

    put(AllomanticMetal.GOLD, METAL_PREFIX + "gold_icon.png");
    put(AllomanticMetal.CADMIUM, METAL_PREFIX + "cadmium_icon.png");
    put(AllomanticMetal.ELECTRUM, METAL_PREFIX + "electrum_icon.png");
    put(AllomanticMetal.BENDALLOY, METAL_PREFIX + "bendalloy_icon.png");
  }};
  public static final String LERASIUM_TEXTURE = METAL_PREFIX + "lerasium_icon.png";
}
