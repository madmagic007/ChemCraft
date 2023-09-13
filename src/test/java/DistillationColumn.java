import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.FluidHandler;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import me.madmagic.chemcraft.util.fluids.VaporHelper;

import java.util.*;

public class DistillationColumn {

    private final int plateCount;
    private final double bottomTemp, topTemp;
    private final LinkedList<MultiFluidStorage> storages = new LinkedList<>();

    private static final double evaporateFactor = 0.4;
    private static final double downFallFactor = 0.3;

    public DistillationColumn(int plateCount, double bottomTemp, double topTemp) {
        this.plateCount = plateCount;
        this.bottomTemp = bottomTemp;
        this.topTemp = topTemp;

        for (int i = 0; i < plateCount; i++) {
            MultiFluidStorage storage = new MultiFluidStorage(4000);
            storages.add(storage);
        }
    }

    public void insert(int atPlate, List<Fluid> fluids) {
        MultiFluidStorage storage = storages.get(atPlate);
        storage.add(fluids);

        double spaceLeft = FluidHandler.getStored(fluids);
        if (spaceLeft > 0 && atPlate != 0) insert(atPlate - 1, fluids);
    }

    public void tick() {
        MultiFluidStorage bottomStorage = storages.getFirst();
        double bottomStored = bottomStorage.getStored();
        if (bottomStored != 0) {
            bottomStorage.setTemperature(bottomTemp);
            double deltaV = bottomStored - 500;
            if (deltaV > 0) {
                List<Fluid> bottomExtract = new ArrayList<>();
                bottomStorage.extract(deltaV, bottomExtract);
            }
        }

        GeneralUtil.forEachIndexed(storages, (storage,  plate) -> {
            double fluidAmount = storage.getStored();
            if (fluidAmount <= 0) return;

            VaporHelper vh = new VaporHelper(storage.fluids);

            //evaporate up
            if (plate != plateCount - 1) {
                double evaporateAmount = Math.min(fluidAmount, storage.capacity) * evaporateFactor;
                List<Fluid> vapor = vh.getVapor(evaporateAmount);
                insert(plate + 1, vapor);
            }

            //condense down
            if (plate == 0) return;
            double downFallAmount = Math.min(storage.getStored(), storage.capacity) * downFallFactor;
            List<Fluid> downStream = vh.getDownFall(downFallAmount);
            insert(plate - 1, downStream);
        });

        MultiFluidStorage topStorage = storages.getLast();
        double topStored = topStorage.getStored();
        if (topStored != 0) {
            double deltaV = topStored - 500;
            if (deltaV > 0) {
                List<Fluid> topExtract = new ArrayList<>();
                topStorage.extract(deltaV, topExtract);
            }

            topStorage.setTemperature(topTemp);
        }
    }

    public double extract(int atPlate, double amount, List<Fluid> extractTo) {
        return storages.get(atPlate).extract(amount, extractTo);
    }
}
