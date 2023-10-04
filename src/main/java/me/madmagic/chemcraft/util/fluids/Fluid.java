package me.madmagic.chemcraft.util.fluids;

import java.text.DecimalFormat;

public class Fluid {

    public final String name;
    public double amount, temperature;

    private static DecimalFormat decimalFormat = new DecimalFormat("#.######");
    private static DecimalFormat decimalFormatShort = new DecimalFormat("#.##");

    public Fluid(String name, double amount, double temperature) {
        this.name = name;
        this.amount = roundNumber(amount);
        this.temperature = roundNumber(temperature);
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

    public static double roundNumber(double number) {
        try {
            return Double.parseDouble(decimalFormat.format(number));
        } catch (Exception ignored) {
            return number; //THIS WILL NEVER HAPPEN BUT I STILL NEED IT
        }
    }

    @Override
    public String toString() {
        return String.format("%sl %s at %s°C", decimalFormatShort.format(amount), name, decimalFormatShort.format(temperature));
    }
}
