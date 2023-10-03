package me.madmagic.chemcraft.util.reloaders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.madmagic.chemcraft.util.reactions.ChemicalReaction;
import me.madmagic.chemcraft.util.reactions.ChemicalReactionHandler;

public class ChemicalReactionRegisterer extends BasePreparableReloadListener<ChemicalReaction> {

    private static final Codec<ChemicalReaction> recipeCodec = RecordCodecBuilder.create(instance -> instance.group(
        ChemicalReaction.ReactionType.codec,
            Codec.INT.optionalFieldOf("requiredTemperature", Integer.MIN_VALUE).forGetter(ChemicalReaction::minTemp),
            Codec.INT.optionalFieldOf("maxTemperature", Integer.MAX_VALUE).forGetter(ChemicalReaction::maxTemp),
            ChemicalReaction.ReactionCatalyst.codec,
            ChemicalReaction.ReactionProduct.codec.fieldOf("input").forGetter(ChemicalReaction::inputs),
            ChemicalReaction.ReactionProduct.codec.fieldOf("output").forGetter(ChemicalReaction::outputs)
    ).apply(instance, ChemicalReaction::new));

    public ChemicalReactionRegisterer() {
        super("chemistry/reactions", recipeCodec);
    }

    @Override
    protected void removeAllElements() {
        ChemicalReactionHandler.reactorReaction.clear();
        ChemicalReactionHandler.anyReaction.clear();
    }

    @Override
    protected void registerElement(ChemicalReaction element) {
        if (element.type().equals(ChemicalReaction.ReactionType.ANY))
            ChemicalReactionHandler.anyReaction.add(element);
        else
            ChemicalReactionHandler.reactorReaction.add(element);
    }
}
