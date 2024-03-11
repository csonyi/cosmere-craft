package com.csonyi.cosmerecraft.item;

import com.csonyi.cosmerecraft.capability.allomancy.Allomancy;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.util.TickUtils;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MetalVial extends Item {

  private static final int[] MULTI_PORTION_AMOUNTS = {
      0,
      TickUtils.minutesToTicks(1) + TickUtils.secondsToTicks(30),
      TickUtils.minutesToTicks(3),
      TickUtils.minutesToTicks(8)
  };

  private static final int DRINK_DURATION = 10;

  protected AllomanticMetal metal1;
  protected AllomanticMetal metal2;
  protected AllomanticMetal metal3;

  public MetalVial() {
    super(new Properties().stacksTo(16));
  }

  public MetalVial(AllomanticMetal metal1, AllomanticMetal metal2, AllomanticMetal metal3) {
    this();
    this.metal1 = metal1;
    this.metal2 = metal2;
    this.metal3 = metal3;
  }

  @Override
  public @NotNull ItemStack finishUsingItem(
      @NotNull ItemStack itemStack,
      @NotNull Level level,
      @NotNull LivingEntity livingEntity) {
    super.finishUsingItem(itemStack, level, livingEntity);

    if (!level.isClientSide()) {
      if (livingEntity instanceof ServerPlayer player) {
        var allomancy = Allomancy.of(player);
        var metalAmounts = metals()
            .collect(Collectors.toMap(
                Function.identity(),
                metal -> MULTI_PORTION_AMOUNTS[getPortionCount(metal)]));
        var collectiveMetalAmount = metalAmounts.values().stream()
            .mapToInt(Integer::intValue)
            .sum();
        if (allomancy.canIngestMetal(collectiveMetalAmount)) {
          metalAmounts
              .forEach(allomancy::ingestMetal);
        }
      }
    }

    if (!itemStack.isEmpty()) {
      if (livingEntity instanceof Player player) {
        if (!player.getInventory().add(itemStack)) {
          player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
        }
      }
      return itemStack;
    }
    return new ItemStack(Items.GLASS_BOTTLE);
  }

  @Override
  public int getUseDuration(@NotNull ItemStack itemStack) {
    return DRINK_DURATION * getPortionCount();
  }

  @Override
  public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
    return UseAnim.DRINK;
  }

  @Override
  public @NotNull SoundEvent getDrinkingSound() {
    return SoundEvents.GENERIC_DRINK;
  }

  @Override
  public @NotNull SoundEvent getEatingSound() {
    return getDrinkingSound();
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(
      @NotNull Level level,
      @NotNull Player player,
      @NotNull InteractionHand interactionHand) {
    return ItemUtils.startUsingInstantly(level, player, interactionHand);
  }

  private Stream<AllomanticMetal> metals() {
    return Stream.of(metal1, metal2, metal3)
        .filter(Objects::nonNull);
  }

  private int getPortionCount(AllomanticMetal metal) {
    return (int) metals()
        .filter(metal::equals)
        .count();
  }

  private int getPortionCount() {
    return (int) metals().count();
  }
}
