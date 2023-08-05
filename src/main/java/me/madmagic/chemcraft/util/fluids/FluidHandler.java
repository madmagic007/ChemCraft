package me.madmagic.chemcraft.util.fluids;

import java.util.HashMap;
import java.util.Map;

public class FluidHandler {

    public static Map<String, FluidType> fluidTypes = new HashMap<>();

    static {
        new FluidType("Water", 100, FluidType.SolventType.WATER).register();
        new FluidType("Ethanol", 78, FluidType.SolventType.WATER).register();
        new FluidType("Methanol", 65, FluidType.SolventType.WATER).register();

        new FluidType("Benzene", 80, FluidType.SolventType.ORGANIC).register();
        new FluidType("Toluene", 111, FluidType.SolventType.ORGANIC).register();

        new FluidType("Acetone", 56, FluidType.SolventType.BOTH).register();
    }

    public static FluidType getFluidByName(String name) {
        return fluidTypes.get(name);
    }

    public static double calculateTemperature(double amtA, double tempA, double amtB, double tempB) {
       return (amtA * tempA + amtB * tempB) / (amtA + amtB);
    }
}
