package me.madmagic.chemcraft.util.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;

public interface IFluidContainer {

    void receive(BlockPos pipePos, Direction pipeDir, List<Fluid> fluids, double amount);

    double extract(BlockPos pipePos, Direction pipeDir, double amount, List<Fluid> extractTo);

    MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir);
}
