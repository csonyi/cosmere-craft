package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.entity.Inquisitor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftEntities {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(Registries.ENTITY_TYPE, CosmereCraft.MOD_ID);

  public static final Holder<EntityType<?>> INQUISITOR_ENTITY_TYPE =
      ENTITY_TYPES.register(
          "inquisitor",
          () -> EntityType.Builder.of(Inquisitor::new, MobCategory.MONSTER)
              .sized(0.6f, 1.95f)
              .clientTrackingRange(8)
              .build("inquisitor"));

  public static void register(IEventBus eventBus) {
    ENTITY_TYPES.register(eventBus);
  }

  public static final class MobTypes {

    public static final MobType INQUISITOR = new MobType();
  }
}
