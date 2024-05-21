package com.csonyi.cosmerecraft.item;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.util.TickUtils;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class MetalVial extends Item {

  private static final int DRINK_DURATION = 40;
  private static final int METAL_AMOUNT = TickUtils.minutesToTicks(16);

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
    if (!(livingEntity instanceof Player player)) {
      return super.finishUsingItem(itemStack, level, livingEntity);
    }
    if (player instanceof ServerPlayer serverPlayer) {
      CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
    }
    var allomancy = IAllomancy.of(player);
    if (allomancy.canIngestMetalAmount(METAL_AMOUNT)) {
      allomancy.ingestMetal(metal, METAL_AMOUNT);
    }

    if (!player.getAbilities().instabuild) {
      itemStack.shrink(1);
      if (itemStack.isEmpty()) {
        return new ItemStack(Items.GLASS_BOTTLE);
      } else {
        player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
      }
    }

    livingEntity.gameEvent(GameEvent.DRINK);
    return super.finishUsingItem(itemStack, level, livingEntity);
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
