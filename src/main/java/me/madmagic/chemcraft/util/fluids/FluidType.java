package me.madmagic.chemcraft.util.fluids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FluidType {

    public final String name;
    public final int boilingPoint;
    public final SolventType solventType;

    public Predicate<Fluid> decomposePredicate;
    public Function<Fluid, List<Fluid>> decomposeFunc;

    public FluidType(String name, int boilingPoint, SolventType solventType) {
        this.name = name;
        this.boilingPoint = boilingPoint;
        this.solventType = solventType;
    }

    public FluidType decomposesInto(Predicate<Fluid> decomposePredicate, String... fluids) {
        this.decomposePredicate = decomposePredicate;
        decomposeFunc = fluid -> {
            double amount = fluid.amount / (double) fluids.length;
            List<Fluid> decomposed = new ArrayList<>();

            for (String fluidName : fluids) {
                decomposed.add(new Fluid(fluidName, amount, fluid.temperature));
            }

            return decomposed;
        };
        return this;
    }

    public FluidType decomposeInto(Predicate<Fluid> decomposePredicate, Function<Fluid, Fluid> decomposeFunc) {
        this.decomposePredicate = decomposePredicate;
        this.decomposeFunc = fluid -> Collections.singletonList(decomposeFunc.apply(fluid));
        return this;
    }

    public FluidType decomposesInto(Predicate<Fluid> decomposePredicate, Function<Fluid, List<Fluid>> decomposeFunc) {
        this.decomposePredicate = decomposePredicate;
        this.decomposeFunc = decomposeFunc;
        return this;
    }

    public void register() {
        FluidHandler.fluidTypes.put(name, this);
    }

    public enum SolventType {
        ORGANIC,
        WATER,
        BOTH,
        GAS
    }
}
