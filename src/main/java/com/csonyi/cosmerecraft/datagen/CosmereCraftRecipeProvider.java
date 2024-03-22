package com.csonyi.cosmerecraft.datagen;

import com.csonyi.cosmerecraft.capability.allomancy.AllomanticMetal;
import com.csonyi.cosmerecraft.registry.CosmereCraftBlocks;
import com.csonyi.cosmerecraft.registry.CosmereCraftItems;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class CosmereCraftRecipeProvider extends RecipeProvider {

  public CosmereCraftRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
    // TODO: sort this crap out...
    AllomanticMetal.stream()
        .filter(metal -> !metal.isGodMetal())
        .forEach(metal -> {
          shapelessCraftMetalVialRecipe(metal, recipeOutput);
          smeltMetalIngotFromInputRecipe(metal, recipeOutput, CosmereCraftItems.METAL_POWDERS.get(metal));
          shapelessCraftMetalPowderRecipe(metal, recipeOutput, CosmereCraftItems.METAL_INGOTS.get(metal));
          Stream.of(
                  CosmereCraftItems.RAW_METALS,
                  CosmereCraftItems.METAL_ORE_BLOCK_ITEMS,
                  CosmereCraftItems.DEEPSLATE_METAL_ORE_BLOCK_ITEMS)
              .map(map -> map.get(metal))
              .forEach(itemHolder -> {
                smeltMetalIngotFromInputRecipe(metal, recipeOutput, itemHolder);
                shapelessCraftMetalPowderRecipe(metal, recipeOutput, itemHolder);
              });
        });
  }

  private static void smeltMetalIngotFromInputRecipe(AllomanticMetal metal, RecipeOutput recipeOutput, Holder<Item> input) {
    var ingredient = Ingredient.of(input.value());
    var resultItem = metal.getIngotItemHolder().value();
    SimpleCookingRecipeBuilder.smelting(
            ingredient,
            RecipeCategory.MISC,
            resultItem,
            0.7f, 200)
        .unlockedBy("has_lerasium", has(CosmereCraftItems.LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(CosmereCraftBlocks.INVESTITURE_LIQUID.value()))
        .save(recipeOutput); // same as vanilla iron ore TODO: move to AllomanticMetal and balance
  }

  private static void shapelessCraftMetalPowderRecipe(AllomanticMetal metal, RecipeOutput recipeOutput, Holder<Item> inputItem) {
    if (metal.isGodMetal()) {
      return;
    }
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CosmereCraftItems.METAL_POWDERS.get(metal).value())
        .requires(inputItem.value())
        .requires(CosmereCraftItems.MORTAR_AND_PESTLE.value())
        .unlockedBy("has_lerasium", has(CosmereCraftItems.LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(CosmereCraftBlocks.INVESTITURE_LIQUID.value()))
        .save(recipeOutput);
  }

  private static void shapelessCraftMetalVialRecipe(AllomanticMetal metal, RecipeOutput recipeOutput) {
    if (metal.isGodMetal()) {
      return;
    }
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CosmereCraftItems.METAL_VIALS.get(metal).value())
        .requires(Items.POTION)
        .requires(CosmereCraftItems.METAL_POWDERS.get(metal).value())
        .unlockedBy("has_lerasium", has(CosmereCraftItems.LERASIUM_NUGGET.value()))
        .unlockedBy("inside_of_investiture", insideOf(CosmereCraftBlocks.INVESTITURE_LIQUID.value()))
        .save(recipeOutput);
  }
}
