package net.fabricmc.simplepaxels;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingRecipeJsonBuilder;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;

import java.util.function.Consumer;

import static net.fabricmc.simplepaxels.SPItems.NETHERITE_UPGRADE;
import static net.fabricmc.simplepaxels.SPItems.PAXELS;

public class SPRecipeProvider extends FabricRecipeProvider {
    public SPRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        //paxels
        for (int i = 0; i < 6; i++) {
            ShapedRecipeJsonBuilder
                    .create(PAXELS[i])
                    .input('A', SPMod.AXES[i])
                    .input('P', SPMod.PICKAXES[i])
                    .input('S', SPMod.SHOVELS[i])
                    .input('s', Items.STICK)
                    .pattern("ASP")
                    .pattern(" s ")
                    .pattern(" s ")
                    .criterion("has_axe", conditionsFromItem(SPMod.AXES[i]))
                    .criterion("has_pickaxe", conditionsFromItem(SPMod.PICKAXES[i]))
                    .criterion("has_shovel", conditionsFromItem(SPMod.SHOVELS[i]))
                    .offerTo(exporter);
        }
        //upgrade
        ShapelessRecipeJsonBuilder
                .create(NETHERITE_UPGRADE)
                .input(Items.NETHERITE_INGOT)
                .input(Items.NETHERITE_INGOT)
                .input(Items.NETHERITE_INGOT)
                .criterion("has_netherite_ingot",conditionsFromItem(Items.NETHERITE_INGOT))
                .offerTo(exporter);

        //smiting
        Ingredient addition = Ingredient.ofStacks(new ItemStack(NETHERITE_UPGRADE));
        Ingredient base = Ingredient.ofStacks(new ItemStack(PAXELS[4],1));

        SmithingRecipeJsonBuilder.create(base,addition, PAXELS[5])
                .criterion("has_diamond_paxel", conditionsFromItem(PAXELS[4]))
                .offerTo(exporter,"smithing_table_netherite_paxel");
    }

}
