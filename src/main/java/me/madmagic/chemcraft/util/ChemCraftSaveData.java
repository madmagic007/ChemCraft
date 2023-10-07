package me.madmagic.chemcraft.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class ChemCraftSaveData extends SavedData {

    private static final String tag = "chemcraft";
    public boolean isPowerUsageDisabled = false;
    public boolean isFuelUsageDisabled = false;

    public static ChemCraftSaveData getOrCreate(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(ChemCraftSaveData::load, ChemCraftSaveData::create, tag);
    }

    protected ChemCraftSaveData(CompoundTag nbt) {
        isPowerUsageDisabled = nbt.getBoolean("powerUsageDisabled");
        isFuelUsageDisabled = nbt.getBoolean("fuelUsageDisabled");
    }

    protected ChemCraftSaveData() {}

    public static ChemCraftSaveData load(CompoundTag nbt) {
        return new ChemCraftSaveData(nbt);
    }

    public static ChemCraftSaveData create() {
        return new ChemCraftSaveData();
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putBoolean("powerUsageDisabled", isPowerUsageDisabled);
        pCompoundTag.putBoolean("fuelUsageDisabled", isFuelUsageDisabled);
        return pCompoundTag;
    }
}
