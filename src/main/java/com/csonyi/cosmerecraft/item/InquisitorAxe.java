package com.csonyi.cosmerecraft.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

public class InquisitorAxe extends TieredItem {

  private final float attackDamage;
  private final Multimap<Attribute, AttributeModifier> defaultModifiers;

  public InquisitorAxe(Tier tier, float attackDamageModifier, float attackSpeedModifier) {
    super(tier, new Item.Properties());
    attackDamage = attackDamageModifier + tier.getAttackDamageBonus();
    var modifierMapBuilder = ImmutableMultimap.<Attribute, AttributeModifier>builder();
    modifierMapBuilder.put(
        Attributes.ATTACK_DAMAGE.value(),
        new AttributeModifier(
            BASE_ATTACK_DAMAGE_UUID,
            "Weapon modifier",
            attackDamage,
            AttributeModifier.Operation.ADD_VALUE));
    modifierMapBuilder.put(
        Attributes.ATTACK_SPEED.value(),
        new AttributeModifier(
            BASE_ATTACK_SPEED_UUID,
            "Weapon modifier",
            attackSpeedModifier,
            AttributeModifier.Operation.ADD_VALUE));
    this.defaultModifiers = modifierMapBuilder.build();
  }

  @Override
  public float getDestroySpeed(@NotNull ItemStack inquisitorAxe, BlockState targetBlockState) {
    return targetBlockState.is(BlockTags.MINEABLE_WITH_AXE) ? getTier().getSpeed() : super.getDestroySpeed(inquisitorAxe, targetBlockState);
  }

  @Override
  public boolean canAttackBlock(@NotNull BlockState targetBlockState, @NotNull Level level, @NotNull BlockPos targetBlockPos,
      Player attackingPlayer) {
    return !attackingPlayer.isCreative();
  }

  @Override
  public boolean hurtEnemy(@NotNull ItemStack inquisitorAxe, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
    inquisitorAxe.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    return true;
  }

  @Override
  public boolean mineBlock(@NotNull ItemStack inquisitorAxe, @NotNull Level level, BlockState targetBlockState,
      @NotNull BlockPos targetBlockPos, @NotNull LivingEntity miner) {
    if (targetBlockState.getDestroySpeed(level, targetBlockPos) != 0.0F) {
      inquisitorAxe.hurtAndBreak(2, miner, EquipmentSlot.MAINHAND);
    }

    return true;
  }

  @Override
  public boolean isCorrectToolForDrops(@NotNull ItemStack inquisitorAxe, BlockState targetBlockState) {
    return targetBlockState.is(BlockTags.MINEABLE_WITH_AXE);
  }

  @Override
  public boolean canPerformAction(@NotNull ItemStack inquisitorAxe, @NotNull ToolAction toolAction) {
    return ToolActions.SWORD_SWEEP.equals(toolAction)
        || ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
  }
}
