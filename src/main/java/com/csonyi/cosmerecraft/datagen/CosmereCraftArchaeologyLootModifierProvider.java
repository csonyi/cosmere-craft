package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.registry.CosmereCraftAttachments;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CosmereCraftArchaeologyLootModifierProvider extends GlobalLootModifierProvider {

  private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
      DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CosmereCraft.MOD_ID);

  private static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ArchaeologyLootModifier>> ARCHAEOLOGY_LOOT_MODIFIER =
      GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("archaeology_loot_modifier", ArchaeologyLootModifier.CODEC);

  private static final LootItemCondition[] CONDITIONS = new LootItemCondition[]{
      LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS).build(),
      AnyOfCondition.anyOf(
          LootTableIdCondition.builder(ResourceUtils.minecraftLocation("archaeology/desert_pyramid")),
          LootTableIdCondition.builder(ResourceUtils.minecraftLocation("archaeology/desert_well")),
          LootTableIdCondition.builder(ResourceUtils.minecraftLocation("archaeology/ocean_ruin_cold")),
          LootTableIdCondition.builder(ResourceUtils.minecraftLocation("archaeology/ocean_ruin_warm")),
          LootTableIdCondition.builder(ResourceUtils.minecraftLocation("archaeology/trail_ruins_common")),
          LootTableIdCondition.builder(ResourceUtils.minecraftLocation("archaeology/trail_ruins_rare"))
      ).build()
  };

  public CosmereCraftArchaeologyLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, CosmereCraft.MOD_ID);
  }

  public static void register(IEventBus eventBus) {
    GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(eventBus);
  }

  @Override
  protected void start() {
    add("archaeology_loot_modifiers", new ArchaeologyLootModifier(CONDITIONS));
  }

  public static class ArchaeologyLootModifier extends LootModifier {

    public static final Supplier<MapCodec<ArchaeologyLootModifier>> CODEC =
        Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(
            inst -> codecStart(inst)
                .apply(inst, ArchaeologyLootModifier::new)));

    public ArchaeologyLootModifier(LootItemCondition[] conditionsIn) {
      super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
      var thisEntity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
      if (thisEntity instanceof Player player) {
        var now = Instant.now();
        if (playerLastMedallionFindWasLongAgoOrNever(player, now) && oneInTenChance(context)) {
          player.setData(CosmereCraftAttachments.FOUND_MEDALLION.get(), now);
          return new ObjectArrayList<>(new ItemStack[]{CosmereCraftItems.ANCIENT_MEDALLION.value().getDefaultInstance()});
        }
      }
      return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
      return CODEC.get();
    }

    private static boolean playerLastMedallionFindWasLongAgoOrNever(Player player, Instant now) {
      return player.getExistingData(CosmereCraftAttachments.FOUND_MEDALLION.get())
          .map(lastFoundMedallion -> lastFoundMedallion.plus(1, ChronoUnit.HOURS).isBefore(now))
          .orElse(true);
    }

    private static boolean oneInTenChance(LootContext context) {
      return context.getRandom().nextIntBetweenInclusive(0, 9) == 0;
    }
  }
}
