package com.csonyi.cosmerecraft.entity;

import com.csonyi.cosmerecraft.capability.allomancy.Allomancy;
import com.csonyi.cosmerecraft.registry.CosmereCraftEntities;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

  /**
   * Implement the "AI" of the Inquisitor. This is done by registering the goals and targets.
   */
  @Override
  protected void registerGoals() {
    addGoals();
    addTargets();
  }

  /**
   * Add the goals for the Inquisitor. Lower number means higher priority.<br> Inquisitors have the following goals:
   * <ul>
   *   <li>Look at the player if there is one nearby</li>
   *   <li>Randomly look around</li>
   *   <li>Randomly walk around while avoiding water</li>
   *   <li>Melee attack the current target</li>
   *   <li>Float in water</li>
   * </ul>
   */
  private void addGoals() {
    goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
    goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
    goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5D, true));
    goalSelector.addGoal(0, new FloatGoal(this));
  }

  /**
   * Add the targets for the Inquisitor. Lower number means higher priority.<br> Inquisitors have the following goals:
   * <ul>
   *   <li>Set entity as target if they hurt the Inquisitor</li>
   *   <li>Set entity as target if they are actively using allomancy</li>
   * </ul>
   */
  private void addTargets() {
    targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false, Inquisitor::isActiveAllomancer));
  }

  private static boolean isActiveAllomancer(LivingEntity entity) {
    if (entity instanceof Player player) {
      return Allomancy.of(player).isBurningMetal();
    }
    return false;
  }

  /**
   * For now, Inquisitors use the same sound set as zombies. This could be improved later.
   *
   * @return
   */
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

  /**
   * Inquisitors have a chance to spawn with obsidian axes in their main and off hands. The chance is based on the difficulty level.
   *
   * @param randomSource       the random source to use.
   * @param difficultyInstance the difficulty instance of the current level.
   */
  @Override
  protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance difficultyInstance) {
    super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
    try (var level = level()) {
      var mainHandAxeChance = switch (level.getDifficulty()) {
        case HARD -> 1F;
        case NORMAL -> 0.5F;
        case EASY -> 0.25F;
        default -> 0.0F;
      };
      var offHandAxeChance = switch (level.getDifficulty()) {
        case HARD -> 0.5F;
        case NORMAL -> 0.25F;
        case EASY -> 0F;
        default -> 0.0F;
      };
      if (randomSource.nextFloat() < mainHandAxeChance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CosmereCraftItems.OBSIDIAN_AXE));
      }
      if (randomSource.nextFloat() < offHandAxeChance) {
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(CosmereCraftItems.OBSIDIAN_AXE));
      }
    } catch (IOException e) {
      LOGGER.error("IOException while retrieving level in scan: {}", e.getMessage());
    }
  }

  /**
   * Finalizes spawning by populating the equipment slots and setting the spawn type. Not actually deprecated, it's just a warning from the
   * mod loader to be careful when calling this, since the coremod changes some of the superclass's behaviours.
   *
   * @param serverLevelAccessor the server-side level accessor of the current level.
   * @param difficulty          the difficulty of the current level.
   * @param spawnReason         the reason for the spawn.
   * @param spawnGroupData      the spawn group data (determines how many mobs will spawn together).
   * @param compoundTag         an nbt tag for the mob.
   * @return the superclass's method's return value after setting up the default equipment.
   */
  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(
      @NotNull ServerLevelAccessor serverLevelAccessor, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason,
      @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
    populateDefaultEquipmentSlots(serverLevelAccessor.getRandom(), difficulty);
    return super.finalizeSpawn(serverLevelAccessor, difficulty, spawnReason, spawnGroupData, compoundTag);
  }

  /**
   * Inquisitors get the default Monster attributes, and then add their own attributes on top of that. Health and speed are scaled slightly
   * above the Piglin Brute, attack damage is the same, but the axes will raise that significantly
   *
   * @return the builder for the attributes.
   */
  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 75.0)
        .add(Attributes.MOVEMENT_SPEED, 0.5F)
        .add(Attributes.ATTACK_DAMAGE, 7.0);
  }

  /**
   * Check if the Inquisitor can spawn at the given location. Inquisitors get the default monster spawning rules for now, but this could be
   * an improvement point later.
   *
   * @param inquisitorEntityType   the entity type of the inquisitor.
   * @param serverLevelAccessor    the server-side level accessor of the current level.
   * @param inquisitorMobSpawnType the mob spawn type of the inquisitor.
   * @param spawnPos               block position to check the spawning conditions against.
   * @param randomSource           the random source to use.
   * @return true if the inquisitor can spawn at the given location, false otherwise.
   */
  public static boolean canSpawn(
      EntityType<Inquisitor> inquisitorEntityType, ServerLevelAccessor serverLevelAccessor,
      MobSpawnType inquisitorMobSpawnType, BlockPos spawnPos, RandomSource randomSource) {
    return Monster.checkMonsterSpawnRules(inquisitorEntityType, serverLevelAccessor, inquisitorMobSpawnType, spawnPos, randomSource);
  }
}
