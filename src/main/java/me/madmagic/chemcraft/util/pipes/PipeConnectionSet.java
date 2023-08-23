package me.madmagic.chemcraft.util.pipes;

import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PipeConnectionSet implements IFluidContainer {

    public final BlockPos pipePos, containerPos;
    public final Direction pipeToBlock;
    public final Level level;

    private IFluidContainer container;

    public PipeConnectionSet(BlockPos pipePos, BlockPos containerPos, Direction pipeToBlock, IFluidContainer container, Level level) {
        this.pipePos = pipePos;
        this.pipeToBlock = pipeToBlock;
        this.containerPos = containerPos;
        this.container = container;
        this.level = level;
    }

    public BlockState getState() {
        return level.getBlockState(containerPos);
    }

    public double getStored() {
        return container.getFluidStorage().getStored();
    }

    public double getSpaceLeft() {
        return container.getFluidStorage().getSpaceLeft();
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, List<Fluid> fluids, double amount) {
        container.receive(pipePos, pipeDir, fluids, amount);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, List<Fluid> extractTo) {
        return container.extract(pipePos, pipeDir, amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage() {
        return container.getFluidStorage();
    }
}
