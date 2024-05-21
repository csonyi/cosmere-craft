package com.csonyi.cosmerecraft.item;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.util.TickUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EdibleMetalNugget extends Item {

  private static final int METAL_AMOUNT = TickUtils.minutesToTicks(16) / 9;

  private final AllomanticMetal metal;

  public EdibleMetalNugget(AllomanticMetal metal) {
    super(
        new Properties()
            .rarity(Rarity.EPIC)
            .food(
                new FoodProperties.Builder()
                    .alwaysEdible()
                    .fast()
                    .build()));
    this.metal = metal;
  }

  @Override
  public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, Level level, @NotNull LivingEntity livingEntity) {
    if (!(livingEntity instanceof Player player)) {
      return super.finishUsingItem(itemStack, level, livingEntity);
    }
    if (player instanceof ServerPlayer serverPlayer) {
      CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
    }

    IAllomancy.of(player).ingestMetal(metal, METAL_AMOUNT);
    return super.finishUsingItem(itemStack, level, livingEntity);
  }
}
