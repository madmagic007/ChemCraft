package me.madmagic.chemcraft.util.reactions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.FluidHandler;
import net.minecraft.util.StringRepresentable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public record ChemicalReaction(ReactionType type, int minTemp, int maxTemp, List<ReactionCatalyst> catalysts, List<ReactionProduct> inputs, List<ReactionProduct> outputs) {

    private boolean isTemperatureInRange(double temp) {
        return temp >= minTemp && temp <= maxTemp;
    }

    public LinkedList<Fluid> tryReact(LinkedList<Fluid> fluids) {
        double mixtureTemp = FluidHandler.getTemperature(fluids);

        LinkedList<Fluid> output = new LinkedList<>();
        if (!isTemperatureInRange(mixtureTemp)) return output;

        double factorCalc = Double.MAX_VALUE;

        for (ReactionProduct requiredProduct : inputs) {
            boolean found = false;

            for (Fluid fluid : fluids) {
                if (!fluid.name.equals(requiredProduct.name)) continue;

                double factor = fluid.amount / requiredProduct.amount;
                factorCalc = Math.min(factorCalc, factor);
                found = true;
                break;
            }

            if (!found) return output;
        }

        if (factorCalc < 0) return output;

        final double factor = factorCalc;

        inputs.forEach(inputProduct ->
                FluidHandler.removeFrom(inputProduct.name, inputProduct.amount * factor, fluids)
        );

        outputs.forEach(outputProduct ->
                FluidHandler.transferTo(outputProduct.toFluid(mixtureTemp, factor), output)
        );

        return output;
    }

    public record ReactionProduct(String name, double amount) {

        public static final Codec<List<ReactionProduct>> codec = Codec.unboundedMap(Codec.STRING, Codec.INT).flatComapMap(map -> {
            List<ReactionProduct> list = new LinkedList<>();
            map.forEach((name, amt) -> list.add(new ReactionProduct(name, amt)));
            return list;
        }, from -> DataResult.error(() -> ""));

        public Fluid toFluid(double temp, double factor) {
            return new Fluid(name, amount * factor, temp);
        }
    }

    public static class ReactionCatalyst {

        private final String blockName;
        private int amount;

        public ReactionCatalyst(String blockName, int amount) {
            this.blockName = blockName;
            this.amount = amount;
        }

        public void increment() {
            amount++;
        }

        public boolean equals(String name) {
            return this.blockName.equals(name);
        }

        public static final RecordCodecBuilder<ChemicalReaction, List<ReactionCatalyst>> codec = Codec.unboundedMap(Codec.STRING, Codec.INT).flatComapMap(map -> {
            List<ReactionCatalyst> list = new LinkedList<>();
            map.forEach((name, amt) -> list.add(new ReactionCatalyst(name, amt)));
            return list;
        }, from -> DataResult.error(() -> "")).optionalFieldOf("catalyst", Collections.emptyList()).forGetter(ChemicalReaction::catalysts);
    }

    public enum ReactionType implements StringRepresentable {
        ANY,
        REACTOR;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        public static final RecordCodecBuilder<ChemicalReaction, ReactionType> codec = StringRepresentable.fromEnum(ReactionType::values)
                .optionalFieldOf("type", REACTOR)
                .forGetter(ChemicalReaction::type);

    }
}
