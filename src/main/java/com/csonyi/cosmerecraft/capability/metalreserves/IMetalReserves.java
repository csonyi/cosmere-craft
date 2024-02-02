package com.csonyi.cosmerecraft.capability.metalreserves;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.allomancy.AllomanticMetal;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IMetalReserves extends INBTSerializable<CompoundTag> {

  ResourceLocation CAPABILITY_ID = CosmereCraft.createResourceLocation("metal_reserves");

  void receiveMetal(AllomanticMetal metal);

  void expendMetal(AllomanticMetal metal, int amount);

  int getMetalAmount(AllomanticMetal metal);

  boolean isMetalInUse(AllomanticMetal metal);

  boolean canReceiveMetal();

  boolean canExpendMetal(AllomanticMetal metal);

  Set<AllomanticMetal> getMetalsWithReserves();

  void copyFrom(IMetalReserves other);

}
