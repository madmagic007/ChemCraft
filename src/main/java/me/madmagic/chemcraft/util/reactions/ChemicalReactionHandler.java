package me.madmagic.chemcraft.util.reactions;

import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.FluidHandler;

import java.util.HashSet;
import java.util.LinkedList;

public class ChemicalReactionHandler {

    public static final HashSet<ChemicalReaction> anyReactions = new HashSet<>();
    public static final HashSet<ChemicalReaction> reactorReactions = new HashSet<>();

    public static void tryReact(LinkedList<Fluid> fluids) {
        anyReactions.forEach(reaction -> {
            LinkedList<Fluid> output = reaction.tryReact(fluids);
            FluidHandler.transferTo(output, fluids);
        });
    }

    public static void tryReact(LinkedList<Fluid> fluids, HashSet<ChemicalReaction.ReactionCatalyst> catalyst) {
        reactorReactions.forEach(reaction -> {
            LinkedList<Fluid> output = reaction.tryReact(fluids, catalyst);
            FluidHandler.transferTo(output, fluids);
        });
    }
}
