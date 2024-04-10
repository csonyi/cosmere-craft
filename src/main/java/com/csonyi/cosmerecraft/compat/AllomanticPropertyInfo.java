package com.csonyi.cosmerecraft.compat;

import com.csonyi.cosmerecraft.util.ResourceUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class AllomanticPropertyInfo implements IModPlugin {

  @Override
  public ResourceLocation getPluginUid() {
    return ResourceUtils.modResourceLocation("allomantic_property_info");
  }
}
