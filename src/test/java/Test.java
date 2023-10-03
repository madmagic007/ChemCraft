import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.reactions.ChemicalReaction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        LinkedList<Fluid> a = new LinkedList<>();
        a.add(new Fluid("a", 70, 100));
        a.add(new Fluid("b", 30, 100));

        ChemicalReaction reaction = new ChemicalReaction(ChemicalReaction.ReactionType.ANY, 100, Integer.MAX_VALUE,
                Collections.emptyList(),
                List.of(new ChemicalReaction.ReactionProduct("a", 1), new ChemicalReaction.ReactionProduct("b", 2)),
                List.of(new ChemicalReaction.ReactionProduct("c", 2)));

        LinkedList<Fluid> output = reaction.tryReact(a);
        System.out.println(output);
    }
}
