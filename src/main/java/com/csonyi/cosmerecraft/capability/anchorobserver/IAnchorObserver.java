package com.csonyi.cosmerecraft.capability.anchorobserver;

import com.csonyi.cosmerecraft.CosmereCraft;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAnchorObserver extends INBTSerializable<ListTag> {

  ResourceLocation CAPABILITY_ID = CosmereCraft.createResourceLocation("anchor_observer");

  Set<BlockPos> getAnchorsInRange(BlockPos playerPos);

  boolean hasAnchorInRange(BlockPos playerPos);

  void learnAnchor(BlockPos anchor);

  void forgetAnchor(BlockPos anchor);

  void copyFrom(IAnchorObserver other);

}
