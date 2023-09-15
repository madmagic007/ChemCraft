package me.madmagic.chemcraft.util.fluids;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static me.madmagic.chemcraft.util.fluids.FluidType.SolventType;

public class FluidHandler {

    public static Map<String, FluidType> fluidTypes = new HashMap<>();

    static {
        new FluidType("water", 100, SolventType.WATER)
                .decomposesInto(fluid -> fluid.temperature > 100, "steam")
                .register();
        new FluidType("ethanol", 78, SolventType.WATER).register();
        new FluidType("methanol", 65, SolventType.WATER).register();

        new FluidType("crude_oil", Integer.MAX_VALUE, SolventType.ORGANIC)
                .decomposesInto(fluid -> fluid.temperature > 400, "naptha", "naptha", "fuel_oil")
                .register();
        new FluidType("naphta", 120, SolventType.ORGANIC).register();
        new FluidType("fuel_oil", 370, SolventType.ORGANIC).register();

        new FluidType("benzene", 80, SolventType.ORGANIC).register();
        new FluidType("toluene", 111, SolventType.ORGANIC).register();

        new FluidType("acetone", 56, SolventType.BOTH).register();

        new FluidType("steam", Integer.MAX_VALUE, SolventType.GAS)
                .decomposesInto(fluid -> fluid.temperature < 100, "water")
                .register();
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

    public static double transferTo(LinkedList<Fluid> source, LinkedList<Fluid> destination) {
        return transferTo(source, destination, getStored(source));
    }

    public static double transferTo(LinkedList<Fluid> source, LinkedList<Fluid> destination, double amount) {
        checkFluids(source);

        double amountInSource = getStored(source);
        amount = Math.min(amount, amountInSource);
        if (amount <= 0) return 0;

        double actualTransferred = 0;

        for (Fluid fluid : source) {
            double ratio = fluid.getAmount() / amountInSource;
            double amountToTransfer = ratio * amount;

            transferTo(fluid.split(amountToTransfer), destination);
            actualTransferred += amountToTransfer;
        }

        clearEmptyFluids(source);
        clearEmptyFluids(destination);
        checkFluids(destination);

        return actualTransferred;
    }

    public static double getStored(List<Fluid> fluids) {
        double count = 0;
        for (Fluid fluid : fluids) {
            count += fluid.amount;
        }
        return count;
    }

    private static void transferTo(Fluid fluid, List<Fluid> addTo) {
        if (fluid.amount <= 0) return;

        double storedAmount = FluidHandler.getStored(addTo);
        double storedTemp = FluidHandler.getTemperature(addTo);
        double newTemp = FluidHandler.calculateTemperature(storedAmount, storedTemp, fluid.amount, fluid.temperature);

        boolean foundFluid = false;
        for (Fluid listFluid : addTo)
            if (listFluid.mergeWith(fluid)) {
                foundFluid = true;
                break;
            }

        if (!foundFluid) addTo.add(fluid.split(fluid.amount));

        addTo.forEach(f -> f.temperature = newTemp);
    }

    public static void clearEmptyFluids(LinkedList<Fluid> toClear) {
        toClear.removeIf(fluid -> fluid.amount < 0.01);
    }

    public static void checkFluids(LinkedList<Fluid> fluids) {
        LinkedList<Fluid> decomposeProducts = new LinkedList<>();

        fluids.removeIf(fluid -> {
            FluidType type = fluid.getFluidType();

            LinkedList<Fluid> currentDecompose = fluid.checkDecompose();
            if (!currentDecompose.isEmpty()) {
                decomposeProducts.addAll(currentDecompose);
                return true;
            }

            if (fluid.temperature > type.boilingPoint) fluid.temperature = type.boilingPoint;
            return false;
        });

        if (!decomposeProducts.isEmpty())
            transferTo(decomposeProducts, fluids);
    }
}
