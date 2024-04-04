package com.csonyi.cosmerecraft.entity;

import com.csonyi.cosmerecraft.capability.allomancy.Allomancy;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class Inquisitor extends Monster {

  private static final Logger LOGGER = LogUtils.getLogger();

  public Inquisitor(EntityType<? extends Monster> pEntityType, Level pLevel) {
    super(pEntityType, pLevel);
    xpReward = 25;
  }

  @Override
  protected void registerGoals() {
    addGoals();
    addTargets();

  }

  private void addGoals() {
    goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5D, true));
  }

  private void addTargets() {
    targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false, Inquisitor::isActiveAllomancer));
  }

  private static boolean isActiveAllomancer(LivingEntity entity) {
    if (entity instanceof Player player) {
      return Allomancy.of(player).isBurningMetal();
    }
    return false;
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ZOMBIE_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
    return SoundEvents.ZOMBIE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ZOMBIE_DEATH;
  }

  protected SoundEvent getStepSound() {
    return SoundEvents.ZOMBIE_STEP;
  }

  @Override
  protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
    this.playSound(this.getStepSound(), 0.15F, 1.0F);
  }

  @Override
  public @NotNull MobType getMobType() {
    return CosmereCraftEntities.MobTypes.INQUISITOR;
  }

  @Override
  protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance difficultyInstance) {
    super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
    try (var level = level()) {
      if (randomSource.nextFloat() < (level.getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CosmereCraftItems.OBSIDIAN_AXE));
      }
    } catch (IOException e) {
      LOGGER.error("IOException while retrieving level in scan: {}", e.getMessage());
    }
  }
}
