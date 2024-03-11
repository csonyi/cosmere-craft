package com.csonyi.cosmerecraft.registry;

import static com.csonyi.cosmerecraft.CosmereCraft.createResourceLocation;

import com.csonyi.cosmerecraft.CosmereCraft;
import com.csonyi.cosmerecraft.block.AshLayerBlock;
import com.csonyi.cosmerecraft.block.InvestitureLiquidBlock;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CosmereCraftBlocks {

  public static final DeferredRegister.Blocks BLOCKS =
      DeferredRegister.createBlocks(CosmereCraft.MOD_ID);

  public static final TagKey<Block> ANCHOR_TAG = BlockTags.create(createResourceLocation("anchor"));
  public static final TagKey<Block> ASH_TAG = BlockTags.create(createResourceLocation("ash"));

  public static final Holder<Block> ASH = BLOCKS.registerBlock(
      "ash",
      AshLayerBlock::new,
      BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW)
          .mapColor(DyeColor.LIGHT_GRAY));
  public static final Holder<Block> ASH_BLOCK = BLOCKS.registerSimpleBlock(
      "ash_block",
      BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK)
          .mapColor(DyeColor.LIGHT_GRAY));

  public static final DeferredBlock<LiquidBlock> INVESTITURE_LIQUID = BLOCKS.register(
      "investiture_liquid",
      InvestitureLiquidBlock::new);

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}