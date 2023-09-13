package me.madmagic.chemcraft.util.fluids;

import java.util.LinkedList;
import java.util.List;

public class Fluid {

    public final String name;
    public double amount, temperature;

    public Fluid(String name, double amount, double temperature) {
        this.name = name;
        this.amount = amount;
        this.temperature = temperature;
    }

    public Fluid split(double amount) {
        double extractAble = Math.min(amount, this.amount);
        this.amount -= extractAble;
        return new Fluid(name, extractAble, temperature);
    }

    public boolean mergeWith(Fluid other) {
        if (!other.name.equals(name)) return false;

        //temperature = FluidHandler.calculateTemperature(amount, temperature, other.amount, other.temperature);
        amount += other.amount;
        return true;
    }

    public double getAmount() {
        return amount;
    }

    public FluidType getFluidType() {
        return FluidHandler.getFluidByName(name);
    }

    public double getBoilingPoint() {
        return getFluidType().boilingPoint;
    }

    public List<Fluid> checkDecompose() {
        List<Fluid> fluids = new LinkedList<>();

        List<String> names = getFluidType().shouldDecompose.apply(this);
        double amountPer = amount / names.size();

        for (String fluidName : names) {
            fluids.add(new Fluid(fluidName, amountPer, temperature));
        }
        return fluids;
    }

    @Override
    public String toString() {
        return String.format("%sl %s at %s°C", amount, name, temperature);
    }
}
