package com.csonyi.cosmerecraft.item;

import com.csonyi.cosmerecraft.capability.allomancy.Allomancy;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EdibleMetalNugget extends Item {

  private final AllomanticMetal metal;

  public EdibleMetalNugget(AllomanticMetal metal) {
    super(
        new Properties()
            .rarity(Rarity.EPIC)
            .food(
                new FoodProperties.Builder()
                    .alwaysEat()
                    .fast()
                    .build()));
    this.metal = metal;
  }

  @Override
  public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, Level level, @NotNull LivingEntity livingEntity) {
    if (!level.isClientSide()) {
      if (livingEntity instanceof ServerPlayer player) {
        CriteriaTriggers.CONSUME_ITEM.trigger(player, itemStack);
        Allomancy.of(player).ingestMetal(metal, AllomanticMetal.MetalAmount.NUGGET);
      }
    }

    return super.finishUsingItem(itemStack, level, livingEntity);
  }
}
