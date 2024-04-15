package com.csonyi.cosmerecraft.datagen;

import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.ALUMINUM;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.BENDALLOY;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.BRASS;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.BRONZE;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.CADMIUM;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.CHROMIUM;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.COPPER;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.DURALUMIN;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.ELECTRUM;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.GOLD;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.IRON;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.NICROSIL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.PEWTER;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.STEEL;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.TIN;
import static com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal.ZINC;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.INVESTITURE_LIQUID;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.INVESTITURE_PORTAL_BLOCK;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.METAL_BLOCKS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftBlocks.RAW_METAL_BLOCKS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_PILE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.BISMUTH_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.COAL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.DEEPSLATE_BISMUTH_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.DEEPSLATE_LEAD_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.DEEPSLATE_NICKEL_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.DEEPSLATE_SILVER_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LERASIUM_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_INGOTS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_POWDERS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_VIALS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.MORTAR_AND_PESTLE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.NICKEL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_BISMUTH;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_BISMUTH_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_LEAD;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_LEAD_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_NICKEL;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_NICKEL_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_SILVER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_SILVER_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.SILVER_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getDeepslateOreBlockItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getIngotItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getNuggetItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getOreBlockItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getPowderItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getRawMetalItemHolder;
import static net.minecraft.world.item.Items.COAL;
import static net.minecraft.world.item.Items.DIORITE_SLAB;
import static net.minecraft.world.item.Items.DIORITE_WALL;
import static net.minecraft.world.item.Items.POTION;
import static net.minecraft.world.item.Items.SAND;

import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class CosmereCraftRecipeProvider extends RecipeProvider {

  public CosmereCraftRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
    metalIngotRecipes(recipeOutput);
    metalBlockRecipes(recipeOutput);
    rawMetalBlockRecipes(recipeOutput);
    metalPowderRecipes(recipeOutput);
    ashRecipes(recipeOutput);
    shaped(MORTAR_AND_PESTLE)
        .pattern("  |")
        .pattern("# #")
        .pattern(" # ")
        .define('#', DIORITE_SLAB)
        .define('|', DIORITE_WALL);
    METAL_VIALS.forEach(
        (metal, vialItemHolder) -> shapeless(vialItemHolder)
            .requires(POTION)
            .requires(getPowderItemHolder(metal).value())
            .save(recipeOutput));
  }

  private static void rawMetalBlockRecipes(@NotNull RecipeOutput recipeOutput) {
    RAW_METAL_BLOCKS.forEach(
        (metal, blockItemHolder) -> {
          nineBlockStorageRecipes(
              recipeOutput,
              RecipeCategory.MISC,
              getRawMetalItemHolder(metal).value(),
              RecipeCategory.MISC,
              blockItemHolder.value());
        });
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        RAW_LEAD.value(),
        RecipeCategory.MISC,
        RAW_LEAD_BLOCK_ITEM.value());
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        RAW_NICKEL.value(),
        RecipeCategory.MISC,
        RAW_NICKEL_BLOCK_ITEM.value());
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        RAW_SILVER.value(),
        RecipeCategory.MISC,
        RAW_SILVER_BLOCK_ITEM.value());
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        RAW_BISMUTH.value(),
        RecipeCategory.MISC,
        RAW_BISMUTH_BLOCK_ITEM.value());
  }

  private static void metalBlockRecipes(@NotNull RecipeOutput recipeOutput) {
    METAL_BLOCKS.forEach(
        (metal, blockItemHolder) -> {
          nineBlockStorageRecipes(
              recipeOutput,
              RecipeCategory.MISC,
              getIngotItemHolder(metal).value(),
              RecipeCategory.MISC,
              blockItemHolder.value());
        });
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        LEAD_INGOT.value(),
        RecipeCategory.MISC,
        LEAD_BLOCK_ITEM.value());
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        NICKEL_INGOT.value(),
        RecipeCategory.MISC,
        NICKEL_BLOCK_ITEM.value());
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        SILVER_INGOT.value(),
        RecipeCategory.MISC,
        SILVER_BLOCK_ITEM.value());
    nineBlockStorageRecipes(
        recipeOutput,
        RecipeCategory.MISC,
        BISMUTH_INGOT.value(),
        RecipeCategory.MISC,
        BISMUTH_BLOCK_ITEM.value());
  }

  private static void metalIngotRecipes(RecipeOutput recipeOutput) {
    METAL_INGOTS.forEach(
        (metal, ingotItemHolder) -> {
          var ingredients = !metal.isAlloy
              ? Ingredient.of(
              getPowderItemHolder(metal).value(),
              getRawMetalItemHolder(metal).value(),
              getOreBlockItemHolder(metal).value(),
              getDeepslateOreBlockItemHolder(metal).value())
              : Ingredient.of(getPowderItemHolder(metal).value());
          smeltingRecipe(recipeOutput, ingredients, ingotItemHolder);
          nuggetRecipes(recipeOutput, getNuggetItemHolder(metal), ingotItemHolder, metal.lowerCaseName());
        });
    nuggetRecipes(
        recipeOutput,
        getNuggetItemHolder(COPPER),
        getIngotItemHolder(COPPER),
        "copper");
    nuggetRecipes(
        recipeOutput,
        LEAD_NUGGET,
        LEAD_INGOT,
        "lead");
    nuggetRecipes(
        recipeOutput,
        NICKEL_NUGGET,
        NICKEL_INGOT,
        "nickel");
    nuggetRecipes(
        recipeOutput,
        SILVER_NUGGET,
        SILVER_INGOT,
        "silver");
    nuggetRecipes(
        recipeOutput,
        BISMUTH_NUGGET,
        BISMUTH_INGOT,
        "bismuth");
    smeltingRecipe(
        recipeOutput,
        Ingredient.of(
            LEAD_POWDER.value(),
            RAW_LEAD.value(),
            LEAD_BLOCK_ITEM.value(),
            DEEPSLATE_LEAD_ORE_BLOCK_ITEM.value()),
        LEAD_INGOT);
    smeltingRecipe(
        recipeOutput,
        Ingredient.of(
            NICKEL_POWDER.value(),
            RAW_NICKEL.value(),
            NICKEL_BLOCK_ITEM.value(),
            DEEPSLATE_NICKEL_ORE_BLOCK_ITEM.value()),
        NICKEL_INGOT);
    smeltingRecipe(
        recipeOutput,
        Ingredient.of(
            SILVER_POWDER.value(),
            RAW_SILVER.value(),
            SILVER_BLOCK_ITEM.value(),
            DEEPSLATE_SILVER_ORE_BLOCK_ITEM.value()),
        SILVER_INGOT);
    smeltingRecipe(
        recipeOutput,
        Ingredient.of(
            BISMUTH_POWDER.value(),
            RAW_BISMUTH.value(),
            BISMUTH_BLOCK_ITEM.value(),
            DEEPSLATE_BISMUTH_ORE_BLOCK_ITEM.value()),
        BISMUTH_INGOT);
  }

  private static void metalPowderRecipes(RecipeOutput recipeOutput) {
    METAL_POWDERS.forEach(
        (metal, powderItemHolder) -> {
          var singlePowderIngredients = metal.isAlloy
              ? Ingredient.of(getIngotItemHolder(metal).value())
              : Ingredient.of(
                  getIngotItemHolder(metal).value(),
                  getRawMetalItemHolder(metal).value());
          shapeless(powderItemHolder)
              .requires(singlePowderIngredients)
              .requires(MORTAR_AND_PESTLE.value())
              .save(recipeOutput, "single_%s_powder_from_mortar_and_pestle".formatted(metal.lowerCaseName()));
          if (!metal.isAlloy) {
            shapeless(powderItemHolder, 2)
                .requires(Ingredient.of(
                    getOreBlockItemHolder(metal).value(),
                    getDeepslateOreBlockItemHolder(metal).value()))
                .requires(MORTAR_AND_PESTLE.value())
                .save(recipeOutput, "double_%s_powder_from_mortar_and_pestle".formatted(metal.lowerCaseName()));
          }
        });
    shapeless(COAL_POWDER)
        .requires(COAL)
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput);
    shapeless(LEAD_POWDER)
        .requires(Ingredient.of(
            LEAD_INGOT.value(),
            RAW_LEAD.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "single_lead_powder_from_mortar_and_pestle");
    shapeless(LEAD_POWDER, 2)
        .requires(Ingredient.of(
            LEAD_ORE_BLOCK_ITEM.value(),
            DEEPSLATE_LEAD_ORE_BLOCK_ITEM.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "double_lead_powder_from_mortar_and_pestle");
    shapeless(NICKEL_POWDER)
        .requires(Ingredient.of(
            NICKEL_INGOT.value(),
            RAW_NICKEL.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "single_nickel_powder_from_mortar_and_pestle");
    shapeless(NICKEL_POWDER, 2)
        .requires(Ingredient.of(
            NICKEL_ORE_BLOCK_ITEM.value(),
            DEEPSLATE_NICKEL_ORE_BLOCK_ITEM.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "double_nickel_powder_from_mortar_and_pestle");
    shapeless(SILVER_POWDER)
        .requires(Ingredient.of(
            SILVER_INGOT.value(),
            RAW_SILVER.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "single_silver_powder_from_mortar_and_pestle");
    shapeless(SILVER_POWDER, 2)
        .requires(Ingredient.of(
            SILVER_ORE_BLOCK_ITEM.value(),
            DEEPSLATE_SILVER_ORE_BLOCK_ITEM.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "double_silver_powder_from_mortar_and_pestle");
    shapeless(BISMUTH_POWDER)
        .requires(Ingredient.of(
            BISMUTH_INGOT.value(),
            RAW_BISMUTH.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "single_bismuth_powder_from_mortar_and_pestle");
    shapeless(BISMUTH_POWDER, 2)
        .requires(Ingredient.of(
            BISMUTH_ORE_BLOCK_ITEM.value(),
            DEEPSLATE_BISMUTH_ORE_BLOCK_ITEM.value()))
        .requires(MORTAR_AND_PESTLE.value())
        .save(recipeOutput, "double_bismuth_powder_from_mortar_and_pestle");
    // Alloy powders
    shapeless(getPowderItemHolder(STEEL))
        .requires(getPowderItemHolder(IRON).value())
        .requires(COAL_POWDER.value(), 2)
        .save(recipeOutput);
    shapeless(getPowderItemHolder(BRASS), 2)
        .requires(getPowderItemHolder(COPPER).value())
        .requires(getPowderItemHolder(ZINC).value())
        .save(recipeOutput);
    shapeless(getPowderItemHolder(PEWTER), 9)
        .requires(getPowderItemHolder(TIN).value(), 8)
        .requires(LEAD_POWDER.value())
        .save(recipeOutput);
    shapeless(getPowderItemHolder(BRONZE), 4)
        .requires(getPowderItemHolder(COPPER).value(), 3)
        .requires(getPowderItemHolder(TIN).value())
        .save(recipeOutput);
    shapeless(getPowderItemHolder(DURALUMIN), 9)
        .requires(getPowderItemHolder(ALUMINUM).value(), 8)
        .requires(getPowderItemHolder(COPPER).value())
        .save(recipeOutput);
    shapeless(getPowderItemHolder(NICROSIL), 9)
        .requires(NICKEL_POWDER.value(), 7)
        .requires(getPowderItemHolder(CHROMIUM).value())
        .requires(SAND)
        .save(recipeOutput);
    shapeless(getPowderItemHolder(ELECTRUM), 2)
        .requires(getPowderItemHolder(GOLD).value())
        .requires(SILVER_POWDER.value())
        .save(recipeOutput);
    shapeless(getPowderItemHolder(BENDALLOY), 2)
        .requires(getPowderItemHolder(CADMIUM).value())
        .requires(BISMUTH_POWDER.value())
        .save(recipeOutput);
  }

  private static void ashRecipes(RecipeOutput recipeOutput) {
    twoByTwoPacker(recipeOutput, RecipeCategory.MISC, ASH_BLOCK_ITEM.value(), ASH_PILE.value());
    shaped(ASH_ITEM, 6)
        .define('#', ASH_BLOCK_ITEM.value())
        .pattern("###")
        .save(recipeOutput);
  }

  private static void nuggetRecipes(
      RecipeOutput recipeOutput, Holder<Item> nuggetItemHolder, Holder<Item> ingotItemHolder, String metalName) {
    nineBlockStorageRecipesWithCustomPacking(
        recipeOutput,
        RecipeCategory.MISC,
        nuggetItemHolder.value(),
        RecipeCategory.MISC,
        ingotItemHolder.value(),
        "%s_ingot_from_nuggets".formatted(metalName),
        "%s_ingot".formatted(metalName));
  }


  private static void smeltingRecipe(
      RecipeOutput recipeOutput, Ingredient ingredients, Holder<Item> outputItemHolder) {
    SimpleCookingRecipeBuilder.smelting(
            ingredients,
            RecipeCategory.MISC,
            outputItemHolder.value(),
            0.7f, 200)
        .unlockedBy("has_lerasium", has(LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(INVESTITURE_LIQUID.value()))
        .unlockedBy("inside_of_investiture_portal", insideOf(INVESTITURE_PORTAL_BLOCK.value()))
        .save(recipeOutput); // same as vanilla iron ore TODO: move to AllomanticMetal and balance
  }

  private static ShapelessRecipeBuilder shapeless(Holder<Item> outputItemHolder) {
    return shapeless(outputItemHolder, 1);
  }

  private static ShapelessRecipeBuilder shapeless(Holder<Item> outputItemHolder, int outputAmount) {
    return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItemHolder.value(), outputAmount)
        .unlockedBy("has_lerasium", has(LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(INVESTITURE_LIQUID.value()))
        .unlockedBy("inside_of_investiture_portal", insideOf(INVESTITURE_PORTAL_BLOCK.value()));
  }

  private static ShapedRecipeBuilder shaped(Holder<Item> outputItemHolder) {
    return shaped(outputItemHolder, 1);
  }

  private static ShapedRecipeBuilder shaped(Holder<Item> outputItemHolder, int outputAmount) {
    return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputItemHolder.value(), outputAmount)
        .unlockedBy("has_lerasium", has(LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(INVESTITURE_LIQUID.value()))
        .unlockedBy("inside_of_investiture_portal", insideOf(INVESTITURE_PORTAL_BLOCK.value()));
  }
}
