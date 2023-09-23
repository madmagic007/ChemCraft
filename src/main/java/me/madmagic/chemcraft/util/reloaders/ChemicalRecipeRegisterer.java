package me.madmagic.chemcraft.util.reloaders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.madmagic.chemcraft.util.recipes.ChemicalRecipe;
import me.madmagic.chemcraft.util.recipes.ChemicalRecipeHandler;

public class ChemicalRecipeRegisterer extends BasePreparableReloadListener<ChemicalRecipe> {

    private static final Codec<ChemicalRecipe> recipeCodec = RecordCodecBuilder.create(instance -> instance.group(
        ChemicalRecipe.ReactionType.codec,
            Codec.INT.optionalFieldOf("requiredTemperature", Integer.MIN_VALUE).forGetter(ChemicalRecipe::minTemp),
            Codec.INT.optionalFieldOf("maxTemperature", Integer.MAX_VALUE).forGetter(ChemicalRecipe::maxTemp),
            ChemicalRecipe.ReactionCatalyst.codec,
            ChemicalRecipe.ReactionProduct.codec.fieldOf("input").forGetter(ChemicalRecipe::inputs),
            ChemicalRecipe.ReactionProduct.codec.fieldOf("output").forGetter(ChemicalRecipe::outputs)
    ).apply(instance, ChemicalRecipe::new));

    public ChemicalRecipeRegisterer() {
        super("chemistry/reactions", recipeCodec);
    }

    @Override
    protected void removeAllElements() {
        ChemicalRecipeHandler.recipes.clear();
    }

    @Override
    protected void registerElement(ChemicalRecipe element) {
        ChemicalRecipeHandler.recipes.add(element);
    }
}
