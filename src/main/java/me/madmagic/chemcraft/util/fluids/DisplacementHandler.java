package me.madmagic.chemcraft.util.fluids;

import me.madmagic.chemcraft.util.pipes.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class DisplacementHandler {

    public static final double tickFactor = 1. / 60. / 60. / 20.;

    public static double calculateFluidAvailable(PipeLine origin, PipeLine emptyPipeline) {
        double fluidAvailable = 0;
        for (PipeConnectionSet set : origin.sets) {
            double available = set.getStored();
            if (available > 0) emptyPipeline.addSet(set);
            fluidAvailable += available;
        }

        emptyPipeline.sort(PipeConnectionSet::getStored);

        return fluidAvailable;
    }

    public static double calculateSpaceAvailable(PipeLine destination, PipeLine emptyPipeline) {
        double spaceAvailable = 0;
        for (PipeConnectionSet set : destination.sets) {
            double left = set.getSpaceLeft();
            if (left > 0) emptyPipeline.addSet(set);
            spaceAvailable += left;
        }

        emptyPipeline.sort(PipeConnectionSet::getSpaceLeft);

        return spaceAvailable;
    }

    public static LinkedList<Fluid> extract(PipeLine origins, double amount) {
        LinkedList<Fluid> extractedFluids = new LinkedList<>();
        double remainingContainers = origins.sets.size();

        for (PipeConnectionSet set : origins.sets) {
            double avg = amount / remainingContainers;

            LinkedList<Fluid> tankExtract = new LinkedList<>();
            double extracted = set.extract(set.pipePos, set.pipeToBlock.getOpposite(), avg, tankExtract);
            FluidHandler.transferTo(tankExtract, extractedFluids, extracted);

            amount -= extracted;
            remainingContainers --;
        }
        return extractedFluids;
    }

    public static void feed(PipeLine pipeLine, LinkedList<Fluid> fluids) {
        double remainingDestinatingSets = pipeLine.sets.size();
        double totalFluid = FluidHandler.getStored(fluids);

        for (PipeConnectionSet set : pipeLine.sets) {
            double avg = totalFluid / remainingDestinatingSets;
            set.receive(set.pipePos, set.pipeToBlock.getOpposite(), fluids, avg);

            totalFluid -= avg;
            remainingDestinatingSets --;
        }
    }

    public static boolean tryFeed(BlockPos sourcePos, Direction pipeDir, Level level, LinkedList<Fluid> fluids, double amount) {
        BlockPos startPipePos = sourcePos.relative(pipeDir);

        PipeLine pipeLine = PipelineHandler.findPipeline(startPipePos, level, IPipeConnectable.PipeConnectionType.INPUT);
        if (!pipeLine.hasContainers()) return false;

        BlockState pipeState = level.getBlockState(startPipePos);
        if (PipeConnectionHandler.isDirDisconnected(pipeState, pipeDir.getOpposite())) return false;

        PipeLine destinationLine = new PipeLine();
        amount = Math.min(amount, DisplacementHandler.calculateSpaceAvailable(pipeLine, destinationLine));

        if (amount <= 0) return false;

        feed(destinationLine, fluids);
        return true;
    }
}
