package com.csonyi.cosmerecraft;

import com.csonyi.cosmerecraft.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.capability.metalreserves.MetalReserveProvider;
import java.util.stream.Stream;
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
      if (livingEntity instanceof Player player) {
        player.getCapability(MetalReserveProvider.METAL_RESERVES_CAPABILITY)
            .ifPresent(metalReserve -> {
              if (metalReserve.canReceiveMetal()) {
                metalReserve.receiveMetal(metal1);
                metalReserve.receiveMetal(metal2);
                metalReserve.receiveMetal(metal3);
              }
            });
      }
    }

    if (!itemStack.isEmpty()) {
      if (livingEntity instanceof Player player) {
        var emptyBottleItemStack = new ItemStack(Items.GLASS_BOTTLE);
        if (!player.getInventory().add(itemStack)) {
          player.drop(emptyBottleItemStack, false);
        }
      }
      return itemStack;
    }
    return new ItemStack(Items.GLASS_BOTTLE);
  }

  @Override
  public int getUseDuration(@NotNull ItemStack itemStack) {
    return DRINK_DURATION * getPortions();
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

  private int getPortions() {
    return Stream.of(metal1, metal2, metal3)
        .mapToInt(metal -> metal == null ? 0 : 1)
        .sum();
  }
}
