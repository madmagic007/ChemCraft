package me.madmagic.chemcraft.util.fluids;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FluidHandler {

    public static Map<String, FluidType> fluidTypes = new HashMap<>();

    public static FluidType getFluidByName(String name) {
        return fluidTypes.get(name.toLowerCase());
    }

    public static double calculateTemperature(double amtA, double tempA, double amtB, double tempB) {
        double temp = (amtA * tempA + amtB * tempB) / (amtA + amtB);
        return !Double.isNaN(temp) ? temp : 25;
    }

    public static double getTemperature(LinkedList<Fluid> fluids) {
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
        clearEmptyFluids(source);

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

        clearEmptyFluids(destination);
        clearEmptyFluids(source);

        return actualTransferred;
    }

    public static void transferTo(Fluid fluid, LinkedList<Fluid> addTo) {
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

        clearEmptyFluids(addTo);
    }

    public static void removeFrom(String name, double amount, LinkedList<Fluid> fluids) {
        fluids.forEach(fluid -> {
            if (fluid.name.equals(name) && fluid.amount >= amount)
                fluid.split(amount);
        });
        clearEmptyFluids(fluids);
    }

    public static double getStored(List<Fluid> fluids) {
        double count = 0;
        for (Fluid fluid : fluids) {
            count += fluid.amount;
        }
        return count;
    }

    public static void clearEmptyFluids(LinkedList<Fluid> toClear) {
        toClear.removeIf(fluid -> fluid.amount < 0.00001);
    }
}
