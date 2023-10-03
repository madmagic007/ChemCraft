package me.madmagic.chemcraft.util.reactions;

import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.FluidHandler;

import java.util.HashSet;
import java.util.LinkedList;

public class ChemicalReactionHandler {

    public static final HashSet<ChemicalReaction> anyReaction = new HashSet<>();
    public static final HashSet<ChemicalReaction> reactorReaction = new HashSet<>();

    public static void tryReactFluids(LinkedList<Fluid> fluids) {
        anyReaction.forEach(reaction -> {
            LinkedList<Fluid> output = reaction.tryReact(fluids);
            FluidHandler.transferTo(output, fluids);
        });
    }
}
