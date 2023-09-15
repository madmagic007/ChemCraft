import me.madmagic.chemcraft.util.fluids.Fluid;

import java.util.LinkedList;

public class Test {

    public static void main(String[] args) {
        LinkedList<Fluid> a = new LinkedList<>();
        a.add(new Fluid("water", 100, 25));
        a.add(new Fluid("ethanol", 100, 25));

        LinkedList<Fluid> b = new LinkedList<>();
        b.add(new Fluid("water", 50, 25));
        b.add(new Fluid("ethanol", 75, 25));
    }
}
