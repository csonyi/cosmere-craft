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
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.ASH_PILE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.COAL_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.DEEPSLATE_LEAD_ORE_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_BLOCK_ITEM;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_INGOT;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_POWDER;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LEAD_VIAL;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.LERASIUM_NUGGET;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_INGOTS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_POWDERS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.METAL_VIALS;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.MORTAR_AND_PESTLE;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.RAW_LEAD;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getDeepslateOreBlockItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getIngotItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getOreBlockItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getPowderItemHolder;
import static com.csonyi.cosmerecraft.registry.CosmereCraftItems.getRawMetalItemHolder;
import static net.minecraft.world.item.Items.COAL;
import static net.minecraft.world.item.Items.DIORITE_SLAB;
import static net.minecraft.world.item.Items.DIORITE_WALL;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class CosmereCraftRecipeProvider extends RecipeProvider {

  public CosmereCraftRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
    shapedRecipeBuilder(MORTAR_AND_PESTLE)
        .pattern("  |")
        .pattern("# #")
        .pattern(" # ")
        .define('#', DIORITE_SLAB)
        .define('|', DIORITE_WALL);
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
        });
    smeltingRecipe(
        recipeOutput,
        Ingredient.of(
            LEAD_POWDER.value(),
            RAW_LEAD.value(),
            LEAD_BLOCK_ITEM.value(),
            DEEPSLATE_LEAD_ORE_BLOCK_ITEM.value()),
        LEAD_INGOT);
    METAL_VIALS.forEach(
        (metal, vialItemHolder) -> shapelessRecipe(
            recipeOutput,
            vialItemHolder,
            Items.POTION,
            getPowderItemHolder(metal).value()));
    shapelessRecipe(
        recipeOutput,
        LEAD_VIAL,
        Items.POTION,
        LEAD_POWDER.value());
    ashRecipes(recipeOutput);
    powderRecipes(recipeOutput);
  }

  private static void powderRecipes(RecipeOutput recipeOutput) {
    METAL_POWDERS.forEach(
        (metal, powderItemHolder) -> {
          shapelessRecipe(
              recipeOutput,
              powderItemHolder,
              getIngotItemHolder(metal).value(),
              MORTAR_AND_PESTLE.value());
        });
    shapelessRecipe(
        recipeOutput,
        COAL_POWDER,
        COAL,
        MORTAR_AND_PESTLE.value());
    shapelessRecipe(
        recipeOutput,
        LEAD_POWDER,
        LEAD_INGOT.value(),
        MORTAR_AND_PESTLE.value());
    // Alloy powders
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(STEEL), 1,
        getPowderItemHolder(IRON).value(),
        COAL_POWDER.value(),
        COAL_POWDER.value());
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(BRASS),
        getPowderItemHolder(COPPER).value(),
        getPowderItemHolder(ZINC).value());
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(PEWTER),
        Map.of(
            getPowderItemHolder(TIN).value(), 8,
            LEAD_POWDER.value(), 1));
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(BRONZE),
        Map.of(
            getPowderItemHolder(COPPER).value(), 3,
            getPowderItemHolder(TIN).value(), 1));
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(DURALUMIN),
        Map.of(
            getPowderItemHolder(ALUMINUM).value(), 8,
            getPowderItemHolder(COPPER).value(), 1));
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(NICROSIL),
        Map.of(
            // NICKEL_DUST, 7, // TODO: add nickel
            getPowderItemHolder(CHROMIUM).value(), 1,
            Items.SAND, 1));
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(ELECTRUM),
        getPowderItemHolder(GOLD).value()
        // SILVER_DUST // TODO: add silver
    );
    shapelessRecipe(
        recipeOutput,
        getPowderItemHolder(BENDALLOY),
        getPowderItemHolder(CADMIUM).value()
        // BISMUTH_DUST // TODO: add bismuth
    );
  }

  private static void ashRecipes(RecipeOutput recipeOutput) {
    shapelessRecipe(
        recipeOutput,
        ASH_PILE, 4,
        ASH_BLOCK_ITEM.value());
    shapedRecipeBuilder(ASH_BLOCK_ITEM)
        .pattern("##")
        .pattern("##")
        .define('#', ASH_PILE.value())
        .save(recipeOutput);
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
        .save(recipeOutput); // same as vanilla iron ore TODO: move to AllomanticMetal and balance
  }


  private static void shapelessRecipe(
      RecipeOutput recipeOutput, Holder<Item> outputItemHolder, ItemLike... inputItemLikes) {
    var inputItemLikeMap = Arrays.stream(inputItemLikes)
        .collect(Collectors.toMap(
            Function.identity(),
            itemLike -> 1));
    shapelessRecipe(recipeOutput, outputItemHolder, inputItemLikes.length, inputItemLikeMap);
  }

  private static void shapelessRecipe(
      RecipeOutput recipeOutput, Holder<Item> outputItemHolder, int outputAmount, ItemLike... inputItemLikes) {
    var inputItemLikeMap = Arrays.stream(inputItemLikes)
        .collect(Collectors.toMap(
            Function.identity(),
            itemLike -> 1));
    shapelessRecipe(recipeOutput, outputItemHolder, outputAmount, inputItemLikeMap);
  }

  private static <I extends ItemLike> void shapelessRecipe(
      RecipeOutput recipeOutput, Holder<Item> outputItemHolder, Map<I, Integer> inputItemLikes) {
    var outputAmount = inputItemLikes.values().stream()
        .reduce(0, Integer::sum);
    shapelessRecipe(recipeOutput, outputItemHolder, outputAmount, inputItemLikes);
  }

  private static <I extends ItemLike> void shapelessRecipe(
      RecipeOutput recipeOutput, Holder<Item> outputItemHolder, int outputAmount, Map<I, Integer> inputItemLikes) {
    var recipeBuilder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItemHolder.value(), outputAmount);
    inputItemLikes
        .forEach(recipeBuilder::requires);
    recipeBuilder
        .unlockedBy("has_lerasium", has(LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(INVESTITURE_LIQUID.value()))
        .save(recipeOutput);
  }

  private static ShapedRecipeBuilder shapedRecipeBuilder(Holder<Item> outputItemHolder) {
    return ShapedRecipeBuilder.shaped(
            RecipeCategory.MISC,
            outputItemHolder.value())
        .unlockedBy("has_lerasium", has(LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(INVESTITURE_LIQUID.value()));
  }
}
