package me.madmagic.chemcraft.util.fluids;

import java.util.*;

import static me.madmagic.chemcraft.util.fluids.FluidType.SolventType;

public class FluidHandler {

    public static Map<String, FluidType> fluidTypes = new HashMap<>();

    static {
        new FluidType("water", 100, SolventType.WATER)
                .setDecomposition(fluid -> {
                    if (fluid.temperature >= 100) return Arrays.asList("steam");
                    return new ArrayList<>();
                })
                .register();
        new FluidType("ethanol", 78, SolventType.WATER).register();
        new FluidType("methanol", 65, SolventType.WATER).register();

        new FluidType("crude_oil", Integer.MAX_VALUE, SolventType.ORGANIC)
                .setDecomposition(fluid -> {
                    if (fluid.temperature >= 400)
                        return Arrays.asList("naptha", "fuel_oil");
                    return new ArrayList<>();
                })
                .register();
        new FluidType("naphta", 120, SolventType.ORGANIC).register();
        new FluidType("fuel_oil", 370, SolventType.ORGANIC).register();

        new FluidType("benzene", 80, SolventType.ORGANIC).register();
        new FluidType("toluene", 111, SolventType.ORGANIC).register();

        new FluidType("acetone", 56, SolventType.BOTH).register();

        new FluidType("steam", Integer.MAX_VALUE, SolventType.GAS).register();
    }

    public static FluidType getFluidByName(String name) {
        return fluidTypes.get(name.toLowerCase());
    }

    public static double calculateTemperature(double amtA, double tempA, double amtB, double tempB) {
        if (amtA == 0) return tempB;
       return (amtA * tempA + amtB * tempB) / (amtA + amtB);
    }

    public static double getTemperature(List<Fluid> fluids) {
        if (fluids.isEmpty()) return 25;

        Fluid a = fluids.get(0);
        double temp = a.temperature;
        double amount = a.amount;

        for (Fluid fluid : fluids) {
            temp = calculateTemperature(amount, temp, fluid.amount, fluid.temperature);
        }
        return temp;
    }

    public static double transferTo(List<Fluid> source, List<Fluid> destination, double amount) {
        double amountInSource = getStored(source);
        amount = Math.min(amount, amountInSource);
        if (amount <= 0) return 0;

        double actualTransferred = 0;

        for (Fluid fluid : source) {
            double ratio = fluid.getAmount() / amountInSource;
            double amountToTransfer = ratio * amount;

            addFluid(fluid.split(amountToTransfer), destination);
        }

        clearEmptyFluids(source);
        clearEmptyFluids(destination);

        return actualTransferred;
    }

    public static double getStored(List<Fluid> fluids) {
        double count = 0;
        for (Fluid fluid : fluids) {
            count += fluid.amount;
        }
        return count;
    }

    public static void addFluid(Fluid fluid, List<Fluid> addTo) {
        if (fluid.amount <= 0) return;

        boolean foundFluid = false;
        for (Fluid listFluid : addTo)
            if (listFluid.mergeWith(fluid)) {
                foundFluid = true;
                break;
            }

        double stored = FluidHandler.getStored(addTo);
        double soredTemp = FluidHandler.getTemperature(addTo);
        double newTemp = FluidHandler.calculateTemperature(stored, soredTemp, fluid.amount, fluid.temperature);

        if (!foundFluid) addTo.add(fluid);

        addTo.forEach(f -> f.temperature = newTemp);
    }

    public static void clearEmptyFluids(List<Fluid> toClear) {
        toClear.removeIf(fluid -> fluid.amount < 0.01);
    }

    public static void checkFluids(LinkedList<Fluid> fluids) {
        fluids.removeIf(fluid -> {
            FluidType type = fluid.getFluidType();

            if (fluid.temperature > type.boilingPoint) fluid.temperature = type.boilingPoint;

            List<Fluid> decompose = fluid.checkDecompose();
            if (decompose.isEmpty()) return false;

            fluids.addAll(decompose);
            return true;
        });
    }
}
