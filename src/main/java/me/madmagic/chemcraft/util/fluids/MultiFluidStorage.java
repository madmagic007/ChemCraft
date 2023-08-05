package me.madmagic.chemcraft.util.fluids;

import java.util.*;

public abstract class MultiFluidStorage {

    public double capacity;
    private double temperature = 25;
    public final List<Fluid> fluids = new ArrayList<>();

    public MultiFluidStorage(double capacity) {
        this.capacity = capacity;
    }

    public void add(List<Fluid> fluids, double desiredAmount) {
        double totalFluidsAmount = getStored(fluids);
        double amount = Math.min(getSpaceLeft(), Math.min(totalFluidsAmount, desiredAmount));
        fluids.sort(Comparator.comparingDouble(Fluid::getAmount).reversed());

        for (Fluid fluid : fluids) {
            double fluidFactor = fluid.amount / totalFluidsAmount;
            double fluidAmount = amount * fluidFactor;

            mergeFluid(fluid.name, fluidAmount, fluid.temperature);
        }
    }

    public double extract(double desiredAmount, List<Fluid> extractTo) {
        double totalExtracted = 0;

        List<Fluid> extractFrom = new ArrayList<>();
        double amount = Math.min(desiredAmount, calculateFluidAvailable(extractFrom));
        double remainingFluids = extractFrom.size();

        for (Fluid fluid : extractFrom) {
            double avg = amount / remainingFluids;

            double extracted = fluid.extract(avg);
            if (extracted == 0) continue;

            extractTo.add(new Fluid(fluid.name, extracted, fluid.temperature));

            amount -= extracted;
            totalExtracted += extracted;

            remainingFluids --;
        }
        return totalExtracted;
    }

    public double getStored() {
        return getStored(fluids);
    }

    public static double getStored(List<Fluid> fluids) {
        double count = 0;
        for (Fluid fluid : fluids) {
            count += fluid.amount;
        }
        return count;
    }

    public double getSpaceLeft() {
        return capacity - getStored();
    }

    private double calculateFluidAvailable(List<Fluid> emptyMap) {
        double fluidAvailable = 0;
        for (Fluid fluid : fluids) {

            double available = fluid.amount;
            if (available > 0) emptyMap.add(fluid);

            fluidAvailable += available;
        }

        emptyMap.sort(Comparator.comparingDouble(Fluid::getAmount));

        return fluidAvailable;
    }

    private void mergeFluid(String name, double amount, double temperature) {
        if (Double.isNaN(amount)) return;

        double newTemperature = FluidHandler.calculateTemperature(getStored(), this.temperature, amount, temperature);
        this.temperature = newTemperature;

        for (Fluid fluid : fluids) {
            if (fluid.name.equals(name)) {
                fluid.amount += amount;
                fluid.temperature = newTemperature;
                return;
            }
        }
        fluids.add(new Fluid(name, amount, newTemperature));
    }
}
