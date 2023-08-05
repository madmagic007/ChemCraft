package me.madmagic.chemcraft.util.fluids;

public class Fluid {

    public final String name;
    public double amount, temperature;

    public Fluid(String name, double amount, double temperature) {
        this.name = name;
        this.amount = amount;
        this.temperature = temperature;
    }

    public double extract(double amount) {
        double extractAble = Math.min(amount, this.amount);
        this.amount -= extractAble;
        return extractAble;
    }

    public void mergeWith(Fluid other) {
        if (!other.name.equals(name)) return;

        amount += other.amount;
        temperature = FluidHandler.calculateTemperature(amount, temperature, other.amount, other.temperature);
    }

    public double getAmount() {
        return amount;
    }
}
