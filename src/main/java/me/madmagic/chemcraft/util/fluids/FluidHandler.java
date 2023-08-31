package me.madmagic.chemcraft.util.fluids;

import java.util.HashMap;
import java.util.Map;
import static me.madmagic.chemcraft.util.fluids.FluidType.SolventType;

public class FluidHandler {

    public static Map<String, FluidType> fluidTypes = new HashMap<>();

    static {
        new FluidType("water", 100, SolventType.WATER).register();
        new FluidType("ethanol", 78, SolventType.WATER).register();
        new FluidType("methanol", 65, SolventType.WATER).register();

        new FluidType("crude_oil", Integer.MAX_VALUE, SolventType.ORGANIC).register();
        new FluidType("naphta", 120, SolventType.ORGANIC).register();
        new FluidType("fuel_oil", 370, SolventType.ORGANIC).register();

        new FluidType("benzene", 80, SolventType.ORGANIC).register();
        new FluidType("toluene", 111, SolventType.ORGANIC).register();

        new FluidType("acetone", 56, SolventType.BOTH).register();

        new FluidType("waste_gas", -50, SolventType.GAS).register();
    }

    public static FluidType getFluidByName(String name) {
        return fluidTypes.get(name);
    }

    public static double calculateTemperature(double amtA, double tempA, double amtB, double tempB) {
        if (amtA == 0) return tempB;
       return (amtA * tempA + amtB * tempB) / (amtA + amtB);
    }
}
