import me.madmagic.chemcraft.util.fluids.Fluid;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        DistillationColumn column = new DistillationColumn(5, 100, 78);

        double temp = 80;

        for (int i = 0; i < 50; i++) {
            List<Fluid> fluids = new LinkedList<>(Arrays.asList(
                    new Fluid("ethanol", 50, temp),
                    new Fluid("water", 50, temp)
            ));

            column.insert(2, fluids);
            column.tick();
        }

        System.out.println();
    }
}
