package me.madmagic.chemcraft.util.fluids;

import net.minecraft.util.StringRepresentable;

public record FluidType(String name, int boilingPoint,
                        SolubilityType solubilityType) {

    public enum SolubilityType implements StringRepresentable {
        ORGANIC,
        WATER,
        BOTH,
        GAS;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
