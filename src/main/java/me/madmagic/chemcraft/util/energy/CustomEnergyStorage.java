package me.madmagic.chemcraft.util.energy;

import me.madmagic.chemcraft.util.ChemCraftSaveData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    public boolean hasEnoughEnergy(int wanted, Level level) {
        return getEnergyStored() >= wanted || (level instanceof ServerLevel sLevel && ChemCraftSaveData.getOrCreate(sLevel).isPowerUsageDisabled);
    }
}
