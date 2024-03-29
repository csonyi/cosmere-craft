package com.csonyi.cosmerecraft.item;

import com.csonyi.cosmerecraft.capability.allomancy.Allomancy;
import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.util.TickUtils;
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

  private static final int DRINK_DURATION = 40;

  protected AllomanticMetal metal;

  public MetalVial(AllomanticMetal metal) {
    super(new Properties().stacksTo(16));
    this.metal = metal;
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
        var metalAmount = TickUtils.minutesToTicks(16);
        if (allomancy.canIngestMetal(metalAmount)) {
          allomancy.ingestMetal(metal, metalAmount);
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
    return DRINK_DURATION;
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

}
