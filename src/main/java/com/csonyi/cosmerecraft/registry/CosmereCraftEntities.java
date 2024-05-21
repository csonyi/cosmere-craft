package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.entity.Inquisitor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftEntities {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(Registries.ENTITY_TYPE, CosmereCraft.MOD_ID);

  public static final DeferredHolder<EntityType<?>, EntityType<Inquisitor>> INQUISITOR_ENTITY_TYPE =
      ENTITY_TYPES.register(
          "inquisitor",
          () -> EntityType.Builder.<Inquisitor>of(Inquisitor::new, MobCategory.MONSTER)
              .sized(0.6f, 2.1f)
              .build(new ResourceLocation(CosmereCraft.MOD_ID, "inquisitor").toString()));

  public static void register(IEventBus eventBus) {
    ENTITY_TYPES.register(eventBus);
  }

}
