package me.madmagic.chemcraft.util.fluids;

public record FluidType(String name, int boilingPoint,
                        me.madmagic.chemcraft.util.fluids.FluidType.SolventType solventType) {

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
