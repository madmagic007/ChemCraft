package me.madmagic.chemcraft.util.fluids;

import me.madmagic.chemcraft.util.pipes.PipeConnectionSet;
import me.madmagic.chemcraft.util.pipes.PipeLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplacementHandler {

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

    public static List<Fluid> extract(PipeLine origins, double amount) {
        List<Fluid> extractedFluids = new ArrayList<>();
        double remainingContainers = origins.sets.size();

        for (PipeConnectionSet set : origins.sets) {
            double avg = amount / remainingContainers;

            List<Fluid> tankExtract = new ArrayList<>();
            double extracted = set.extract(set.pipePos, set.pipeToBlock.getOpposite(), avg, tankExtract);
            extractedFluids = mergeLists(extractedFluids, tankExtract);

            amount -= extracted;
            remainingContainers --;
        }
        return extractedFluids;
    }

    public static void feed(PipeLine pipeLine, List<Fluid> fluids) {
        double remainingDestinatingSets = pipeLine.sets.size();
        double totalFluid = MultiFluidStorage.getStored(fluids);

        for (PipeConnectionSet set : pipeLine.sets) {
            double avg = totalFluid / remainingDestinatingSets;
            set.receive(set.pipePos, set.pipeToBlock.getOpposite(), fluids, avg);

            totalFluid -= avg;
            remainingDestinatingSets --;
        }
    }

    private static List<Fluid> mergeLists(List<Fluid> a, List<Fluid> b) {
        Map<String, Fluid> map = new HashMap<>();

        a.forEach(fluid -> map.put(fluid.name, fluid));
        b.forEach(fluid -> {
            Fluid existingFluid = map.get(fluid.name);

            if (existingFluid == null) map.put(fluid.name, fluid);
            else {
                existingFluid.mergeWith(fluid);
                map.put(existingFluid.name, existingFluid);
            }
        });

        List<Fluid> fluids = new ArrayList<>();
        map.forEach((name, fluid) -> fluids.add(fluid));
        return fluids;
    }
}
