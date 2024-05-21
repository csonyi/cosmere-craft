package com.csonyi.cosmerecraft.registry;

import com.csonyi.cosmerecraft.capability.ChunkCapability;
import com.csonyi.cosmerecraft.capability.allomancy.IAllomancy;
import com.csonyi.cosmerecraft.capability.anchors.ChunkAnchors;
import com.csonyi.cosmerecraft.util.ResourceUtils;
import net.neoforged.neoforge.capabilities.EntityCapability;

public class CosmereCraftCapabilities {

  public static final EntityCapability<IAllomancy, Void> ALLOMANCY = EntityCapability.createVoid(
      ResourceUtils.modLocation("allomancy"),
      IAllomancy.class);

  public static final ChunkCapability<ChunkAnchors, Boolean> CHUNK_ANCHORS = ChunkCapability.createBoolean(
      ResourceUtils.modLocation("chunk_anchors"),
      ChunkAnchors.class);
}
