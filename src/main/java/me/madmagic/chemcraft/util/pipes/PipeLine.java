package me.madmagic.chemcraft.util.pipes;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class PipeLine {

    public List<PipeConnectionSet> sets = new ArrayList<>();

    public void addSet(PipeConnectionSet set) {
        sets.add(set);
    }

    public boolean hasContainers() {
        return !sets.isEmpty();
    }

    public void sort(ToDoubleFunction<PipeConnectionSet> func) {
        sets.sort(Comparator.comparingDouble(func));
    }
}
