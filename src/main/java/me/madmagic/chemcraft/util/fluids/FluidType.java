package me.madmagic.chemcraft.util.fluids;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FluidType {

    public final String name;
    public final int boilingPoint;
    public final SolventType solventType;

    public Function<Fluid, List<String>> shouldDecompose = f -> new ArrayList<>();

    public FluidType(String name, int boilingPoint, SolventType solventType) {
        this.name = name;
        this.boilingPoint = boilingPoint;
        this.solventType = solventType;
    }

    public FluidType setDecomposition(Function<Fluid, List<String>> shouldDecompose) {
        this.shouldDecompose = shouldDecompose;
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
