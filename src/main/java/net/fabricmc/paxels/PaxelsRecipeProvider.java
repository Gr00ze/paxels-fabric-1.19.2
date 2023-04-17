package net.fabricmc.paxels;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.*;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.StonecuttingRecipe;

import java.util.function.Consumer;

public class PaxelsRecipeProvider extends FabricRecipeProvider {
    public PaxelsRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        for (int i = 0; i < 6; i++) {
            ShapedRecipeJsonBuilder
                    .create(PaxelsMod.PAXELS[i])
                    .input('A', PaxelsMod.AXES[i])
                    .input('P', PaxelsMod.PICKAXES[i])
                    .input('S', PaxelsMod.SHOVELS[i])
                    .input('s', Items.STICK)
                    .pattern("ASP")
                    .pattern(" s ")
                    .pattern(" s ")
                    .criterion("has_axe", conditionsFromItem(PaxelsMod.AXES[i]))
                    .criterion("has_pickaxe", conditionsFromItem(PaxelsMod.PICKAXES[i]))
                    .criterion("has_shovel", conditionsFromItem(PaxelsMod.SHOVELS[i]))
                    .offerTo(exporter);
        }
    }

}
