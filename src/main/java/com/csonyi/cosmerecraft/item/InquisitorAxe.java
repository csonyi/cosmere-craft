package com.csonyi.cosmerecraft.item;

import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class InquisitorAxe extends AxeItem {

  public InquisitorAxe(Tier tier, Properties properties) {
    super(tier, properties);
  }

  @Override
  public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
    itemStack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    return true;
  }

  public static InquisitorAxe obsidian() {
    return new InquisitorAxe(
        CosmereCraftItems.Tiers.OBSIDIAN,
        new Properties()
            .attributes(
                AxeItem.createAttributes(
                    CosmereCraftItems.Tiers.OBSIDIAN,
                    5.0F, -3.3F)));
  }
}
