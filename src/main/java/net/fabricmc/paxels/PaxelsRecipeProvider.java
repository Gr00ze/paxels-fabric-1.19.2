package net.fabricmc.paxels;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class PaxelsRecipeProvider extends FabricRecipeProvider {
    public PaxelsRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(PaxelsMod.PAXELS[0])
                .pattern("CCC,CSX,SCX")
                .input('C', Blocks.COBBLESTONE)
                .input('S', Items.STICK).offerTo(exporter);
    }
}
