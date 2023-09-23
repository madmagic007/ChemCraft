package me.madmagic.chemcraft.util.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public record ChemicalRecipe(ReactionType type, int minTemp, int maxTemp, List<ReactionCatalyst> catalysts, List<ReactionProduct> inputs, List<ReactionProduct> outputs) {

    public record ReactionProduct(String name, double amount) {

        public static final Codec<List<ReactionProduct>> codec = Codec.unboundedMap(Codec.STRING, Codec.INT).flatComapMap(map -> {
            List<ReactionProduct> list = new LinkedList<>();
            map.forEach((name, amt) -> list.add(new ReactionProduct(name, amt)));
            return list;
        }, from -> DataResult.error(() -> ""));
    }

    public record ReactionCatalyst(String blockName, double minAmount) {

        public static final RecordCodecBuilder<ChemicalRecipe, List<ReactionCatalyst>> codec = Codec.unboundedMap(Codec.STRING, Codec.INT).flatComapMap(map -> {
            List<ReactionCatalyst> list = new LinkedList<>();
            map.forEach((name, amt) -> list.add(new ReactionCatalyst(name, amt)));
            return list;
        }, from -> DataResult.error(() -> "")).optionalFieldOf("catalyst", Collections.emptyList()).forGetter(ChemicalRecipe::catalysts);
    }

    public enum ReactionType implements StringRepresentable {
        DECOMPOSE,
        REACTION;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        public static final RecordCodecBuilder<ChemicalRecipe, ReactionType> codec = StringRepresentable.fromEnum(ReactionType::values)
                .optionalFieldOf("type", REACTION)
                .forGetter(ChemicalRecipe::type);

    }
}
