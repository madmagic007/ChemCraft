package me.madmagic.chemcraft.util.energy;

import net.minecraftforge.energy.EnergyStorage;

public abstract class CustomEnergyStorage extends EnergyStorage {

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);

        if (energy != 0) energyChanged(energy);

        return energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);

        if (energy != 0) energyChanged(energy);

        return energy;
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    public abstract void energyChanged(int energy);
}
