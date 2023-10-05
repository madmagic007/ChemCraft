import net.minecraft.core.Direction;

public class Test {

   /* public static void main(String[] args) {
        LinkedList<Fluid> a = new LinkedList<>();
        a.add(new Fluid("a", 70, 100));
        a.add(new Fluid("b", 30, 100));

        ChemicalReaction reaction = new ChemicalReaction(ChemicalReaction.ReactionType.ANY, 100, Integer.MAX_VALUE,
                Collections.emptyList(),
                List.of(new ChemicalReaction.ReactionProduct("a", 1), new ChemicalReaction.ReactionProduct("b", 2)),
                List.of(new ChemicalReaction.ReactionProduct("c", 2)));

        LinkedList<Fluid> output = reaction.tryReact(a);
        System.out.println(output);
    }*/

    public static void main(String[] args) {
        for (Direction facing : Direction.values()) {
            if (!facing.getAxis().isHorizontal()) continue;

            for (Direction abs : Direction.values()) {
                if (!abs.getAxis().isHorizontal()) continue;

                Direction relative = getRelativeDirFromAbsolute(facing, abs);
                Direction newAbs = getAbsoluteDirFromRelative(facing, relative);

                if (newAbs != abs) System.out.println(facing + " " + abs + " " + relative + " " + newAbs);
            }
        }
    }

    private static Direction getAbsoluteDirFromRelative(Direction facing, Direction relative) {
        int val = (facing.get2DDataValue() + relative.get2DDataValue() - 2);
        if (val < 0) val += 4;
        if (val > 3) val -= 4;
        return Direction.from2DDataValue(val);
    }

    private static Direction getRelativeDirFromAbsolute(Direction facing, Direction absolute) {
        int val = (absolute.get2DDataValue() - facing.get2DDataValue() + 2);
        if (val < 0) val += 4;
        return Direction.from2DDataValue(val);
    }
}
