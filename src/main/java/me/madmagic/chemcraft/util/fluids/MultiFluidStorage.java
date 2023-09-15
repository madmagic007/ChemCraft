package me.madmagic.chemcraft.util.fluids;

import net.minecraft.nbt.CompoundTag;

import java.util.LinkedList;
import java.util.List;

public class MultiFluidStorage {

    public double capacity;
    public double temperature = Double.NaN;
    public final LinkedList<Fluid> fluids = new LinkedList<>();

    public MultiFluidStorage(double capacity) {
        this.capacity = capacity;
    }

    public double add(LinkedList<Fluid> fluids) {
        return add(fluids, FluidHandler.getStored(fluids));
    }

    public double add(LinkedList<Fluid> fluids, double desiredAmount) {
        double maxAmount = Math.min(getSpaceLeft(), desiredAmount);
        double transferred = FluidHandler.transferTo(fluids, this.fluids, maxAmount);

        temperature = FluidHandler.getTemperature(this.fluids);
        return transferred;
    }

    public double extract(double desiredAmount, LinkedList<Fluid> extractTo) {
        double maxAmount = Math.min(getStored(), desiredAmount);
        return FluidHandler.transferTo(this.fluids, extractTo, maxAmount);
    }

    public double getSpaceLeft() {
        return capacity - getStored();
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

    public void setTemperature(double temperature) {
        this.temperature = temperature;
        fluids.forEach(fluid -> fluid.temperature = temperature);
        FluidHandler.checkFluids(fluids);
    }

    public void saveToNBT(CompoundTag nbt) {
        CompoundTag storageTag = new CompoundTag();
        storageTag.putInt("capacity", (int) capacity);

        CompoundTag fluidsTag = new CompoundTag();
        fluids.forEach(fluid -> {
            CompoundTag fluidTag = new CompoundTag();
            fluidTag.putDouble("amount", (int) Math.round(fluid.amount));
            fluidTag.putDouble("temperature", (int) Math.round(fluid.temperature));

            fluidsTag.put(fluid.name, fluidTag);
        });
        storageTag.put("fluids", fluidsTag);

        nbt.put("chemcraft.fluidstorage", storageTag);
    }

    public void loadFromNBT(CompoundTag nbt) {
        CompoundTag storageTag = (CompoundTag) nbt.get("chemcraft.fluidstorage");
        capacity = storageTag.getInt("capacity");

        CompoundTag fluidsTag = storageTag.getCompound("fluids");
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

    @Override
    public String toString() {
        return String.format("%sl stored, %s types", getStored(), fluids.size());
    }
}
