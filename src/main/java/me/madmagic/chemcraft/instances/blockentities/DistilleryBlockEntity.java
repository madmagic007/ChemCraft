package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.util.fluids.*;
import me.madmagic.chemcraft.util.multiblock.MultiBlockHandler;
import me.madmagic.chemcraft.util.multiblock.instances.DistilleryMultiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class DistilleryBlockEntity extends BaseBlockEntity implements IFluidContainer {

    private double bottomTemp = 200;
    private double topTemp = 160;
    public final TreeMap<Integer, MultiFluidStorage> fluidStorages = new TreeMap<>();

    private static final double evaporateFactor = 0.04;
    private static final double downFallFactor = 0.03;

    public DistilleryBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.distillery.get(), pos, state);
    }

    public void createFluidStorages(int bottomY, int height, int size) {
        int capacity = size * 1000;
        int topY = bottomY + height - 1;

        for (int i = bottomY + 1; i < topY; i++) {
            fluidStorages.put(i, new MultiFluidStorage(capacity));
        }

        ChemCraft.info();
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids, double amount) {
        if (pipePos.getY() - 1 == fluidStorages.lastKey()) pipePos = pipePos.relative(Direction.DOWN);
        insert(pipePos.getY(), fluids);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo) {
        if (pipePos.getY() + 1 == fluidStorages.firstKey()) pipePos = pipePos.relative(Direction.UP);
        return getFluidStorage(pipePos, pipeDir).extract(amount, extractTo);
    }

    private void insert(int atY, LinkedList<Fluid> fluids) {
        fluidStorages.get(atY).add(fluids);

        double spaceLeft = FluidHandler.getStored(fluids);
        if (spaceLeft > 0 && atY != fluidStorages.firstKey()) insert(atY - 1, fluids);
    }

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return fluidStorages.getOrDefault(pipePos.getY(), new MultiFluidStorage(0));
    }

    @Override
    public List<MultiFluidStorage> getFluidStorages() {
        return new ArrayList<>(fluidStorages.values());
    }

    @Override
    public void tick() {
        MultiFluidStorage bottomStorage = fluidStorages.firstEntry().getValue();
        double bottomStored = bottomStorage.getStored();
        if (bottomStored != 0) bottomStorage.setTemperature(bottomTemp);

        fluidStorages.forEach((atY, storage) -> {
            double fluidAmount = storage.getStored();
            if (fluidAmount <= 0) return;

            VaporHelper vh = new VaporHelper(storage.fluids);

            //evaporate up
            if (atY != fluidStorages.lastKey()) {
                double evaporateAmount = Math.min(fluidAmount, storage.capacity) * evaporateFactor;
                LinkedList<Fluid> vapor = vh.getVapor(evaporateAmount);
                if (!vapor.isEmpty()) insert(atY + 1, vapor);
            }

            //condense down
            if (atY == fluidStorages.firstKey()) return;
            double stored = storage.getStored();
            double downFallAmount = Math.min(stored, storage.capacity) * downFallFactor;
            LinkedList<Fluid> downStream = vh.getDownFall(downFallAmount);
            if (!downStream.isEmpty()) insert(atY - 1, downStream);
        });

        MultiFluidStorage topStorage = fluidStorages.lastEntry().getValue();
        double topStored = topStorage.getStored();
        if (topStored != 0) topStorage.setTemperature(topTemp);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        BlockPos pos = getBlockPos();
        if (!level.isClientSide && !MultiBlockHandler.isValidMultiBlock(pos, level))
            new DistilleryMultiBlock(pos, level).check(true);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        MultiBlockHandler.remove(getBlockPos(), level, true);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        CompoundTag masterTag = nbt.getCompound("chemcraft.fluidstorages");

        masterTag.getAllKeys().forEach(key -> {
            try {
                int level = Integer.parseInt(key);
                CompoundTag storageTag = masterTag.getCompound(key);

                MultiFluidStorage storage = new MultiFluidStorage(0);
                storage.loadFromNBT(storageTag);
                fluidStorages.put(level, storage);
            } catch (Exception ignored) {}
        });
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        if (fluidStorages.isEmpty()) return;

        CompoundTag masterTag = new CompoundTag();
        for (int i = fluidStorages.firstKey(); i < fluidStorages.lastKey(); i++) {
            CompoundTag storageTag = new CompoundTag();
            fluidStorages.get(i).saveToNBT(storageTag);
            masterTag.put(String.valueOf(i), storageTag);
        }

        nbt.put("chemcraft.fluidstorages", masterTag);
    }
}
