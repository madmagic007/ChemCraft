package me.madmagic.chemcraft.util.fluids;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MultiFluidStorage {

    public double capacity;
    public double temperature = 25;
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

    public void saveToNBT(CompoundTag nbt) {
        CompoundTag fluidsTag = new CompoundTag();
        fluidsTag.putInt("capacity", (int) capacity);

        fluids.forEach(fluid -> {
            CompoundTag fluidTag = new CompoundTag();
            fluidTag.putDouble("amount", (int) Math.round(fluid.amount));
            fluidTag.putDouble("temperature", (int) Math.round(fluid.temperature));

            fluidsTag.put(fluid.name, fluidTag);
        });

        nbt.put("chemcraft.fluids", fluidsTag);
    }

    public void loadFromNBT(CompoundTag nbt) {
        CompoundTag fluidsTag = (CompoundTag) nbt.get("chemcraft.fluids");
        capacity = fluidsTag.getInt("capacity");
        fluidsTag.getAllKeys().forEach(fluidName -> {
            CompoundTag fluidTag = fluidsTag.getCompound(fluidName);

            Fluid fluid = new Fluid(
                    fluidName,
                    fluidTag.getDouble("amount"),
                    fluidTag.getDouble("temperature")
            );

            fluids.add(fluid);
        });
    }
}
