package me.madmagic.chemcraft.util.fluids;

import java.util.Comparator;
import java.util.LinkedList;

public class VaporHelper {

    private final LinkedList<Fluid> fluids;
    public Fluid mostVolatile;
    public Fluid leastVolatile;

    public VaporHelper(LinkedList<Fluid> fluids) {
        FluidHandler.clearEmptyFluids(fluids);
        this.fluids = fluids;
        fluids.sort(Comparator.comparingDouble(Fluid::getBoilingPoint));

        if (fluids.isEmpty()) return;
        mostVolatile = fluids.getFirst();
        leastVolatile = fluids.getLast();
    }

    private double bpA() {
        return leastVolatile.getBoilingPoint();
    }

    public double bpB() {
        return mostVolatile.getBoilingPoint();
    }

    private double getFluidAFraction() {
        double sum = fluids.getFirst().amount + fluids.getLast().amount;
        return 100 * fluids.getFirst().amount / sum;
    }

    private double getMixtureBP() {
        double bpA = bpA();
        double bpB = bpB();

        return (bpA - bpB) * Math.pow((getFluidAFraction() - 100) / 100, 2) + bpB;
    }

    private double getVaporAFraction(double mixtureTemp) {
        double bpA = bpA();
        double bpB = bpB();

        double fraction = Math.sqrt((mixtureTemp - bpA) / (bpB - bpA));
        if (fraction > 0.99) fraction = 1;
        else if (fraction < 0.01) fraction = 0;
        else if (Double.isNaN(fraction)) fraction = 1;

        return fraction;
    }

    public LinkedList<Fluid> getVapor(double evaporateAmount) {
        LinkedList<Fluid> vapor = new LinkedList<>();

        double mixtureBP = getMixtureBP();
        if (FluidHandler.getTemperature(fluids) < mixtureBP) return vapor;
        double vaporFractionA = getVaporAFraction(mixtureBP);

        vapor.add(mostVolatile.split(evaporateAmount * vaporFractionA));
        vapor.add(leastVolatile.split(evaporateAmount * (1 - vaporFractionA)));
        FluidHandler.clearEmptyFluids(vapor);

        return vapor;
    }

    private double getFractionThatBoilsAtTemp() {
        double bpA = bpA();
        double bpB = bpB();

        double tempRequiredToGetVaporWithCurrentComp = bpA + (bpB - bpA) * Math.pow(getFluidAFraction() / 100, 2);
        double fractionThatBoilsAtTemp = 100 * -Math.sqrt((tempRequiredToGetVaporWithCurrentComp - bpB) / (bpA - bpB)) + 100;
        fractionThatBoilsAtTemp /= 100;

        if (fractionThatBoilsAtTemp > 0.99) fractionThatBoilsAtTemp = 1;
        else if (fractionThatBoilsAtTemp < 0.01) fractionThatBoilsAtTemp = 0;
        else if (Double.isNaN(fractionThatBoilsAtTemp)) fractionThatBoilsAtTemp = 1;
        return fractionThatBoilsAtTemp;
    }

    public LinkedList<Fluid> getDownFall(double downFallAmount) {
        double fractionThatBoilsAtTemp = getFractionThatBoilsAtTemp();

        LinkedList<Fluid> downFall = new LinkedList<>();
        downFall.add(mostVolatile.split(downFallAmount * fractionThatBoilsAtTemp));
        downFall.add(leastVolatile.split(downFallAmount * (1 - fractionThatBoilsAtTemp)));
        FluidHandler.clearEmptyFluids(downFall);

        return downFall;
    }
}
