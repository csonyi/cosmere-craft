package com.csonyi.cosmerecraft.item;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

public class FeruchemicalMedallion extends Item {

  private final boolean isAncient;
  private final Set<AllomanticMetal> metals;

  public FeruchemicalMedallion(boolean isAncient, AllomanticMetal... metals) {
    super(new Properties()
        .stacksTo(1));
    this.isAncient = isAncient;
    this.metals = Set.of(metals);
  }

  @Override
  public void appendHoverText(
      @NotNull ItemStack itemStack,
      @NotNull TooltipContext tooltipContext,
      @NotNull List<Component> tooltipComponents,
      @NotNull TooltipFlag isAdvanced) {
    if (isAncient) {
      tooltipComponents.add(Component.translatable("item.cosmerecraft.feruchemical_medallion.ancient.tooltip"));
    }
  }

  public static FeruchemicalMedallion ancient() {
    return new FeruchemicalMedallion(true,
        AllomanticMetal.NICROSIL,
        AllomanticMetal.DURALUMIN);
  }

}
