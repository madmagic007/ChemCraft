package me.madmagic.chemcraft.util.fluids;

public class FluidType {

    public final String name;
    public final int boilingPoint;
    public final SolventType solventType;

    public FluidType(String name, int boilingPoint, SolventType solventType) {
        this.name = name;
        this.boilingPoint = boilingPoint;
        this.solventType = solventType;
    }

    public void register() {
        FluidHandler.fluidTypes.put(name, this);
    }

    public enum SolventType {
        ORGANIC,
        WATER,
        BOTH
    }
}
