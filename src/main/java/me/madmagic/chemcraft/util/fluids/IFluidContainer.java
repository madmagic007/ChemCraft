package me.madmagic.chemcraft.util.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;

public interface IFluidContainer {

    void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids);

    double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo);

    MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir);

    default List<MultiFluidStorage> getFluidStorages() {
        return List.of(getFluidStorage(null, null));
    }
}
