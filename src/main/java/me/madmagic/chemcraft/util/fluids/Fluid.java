package me.madmagic.chemcraft.util.fluids;

import java.util.LinkedList;

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
        other.amount = 0;
        return true;
    }

    public double getAmount() {
        return amount;
    }

    public FluidType getFluidType() {
        return FluidHandler.getFluidByName(name);
    }

    public double getBoilingPoint() {
        return getFluidType().boilingPoint();
    }

    public LinkedList<Fluid> checkDecompose() {
        LinkedList<Fluid> fluids = new LinkedList<>();
        /*FluidType type = getFluidType();

        if (type != null && type.decomposePredicate != null  && type.decomposePredicate.test(this)) {
            List<Fluid> fromFunc = getFluidType().decomposeFunc.apply(this);
            if (fromFunc != null) fluids.addAll(fromFunc);
        }*/

        return fluids;
    }

    @Override
    public String toString() {
        return String.format("%sl %s at %s°C", amount, name, temperature);
    }
}
