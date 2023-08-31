package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import me.madmagic.chemcraft.util.multiblock.MultiBlockHandler;
import me.madmagic.chemcraft.util.multiblock.instances.CrudeOilRefinerMultiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.TreeMap;

public class CrudeOilRefinerBlockEntity extends BaseBlockEntity implements IFluidContainer {

    public final TreeMap<Integer, MultiFluidStorage> fluidStorages = new TreeMap<>();

    public CrudeOilRefinerBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.crudeOilRefiner.get(), pos, state);
    }

    public void createFluidStorages(int bottomY, int height, int size) {
        int capacity = size * 1000;
        int topY = bottomY + height - 1;

        MultiFluidStorage bottomStorage = new MultiFluidStorage(capacity);
        fluidStorages.put(bottomY, bottomStorage);
        fluidStorages.put(++bottomY, bottomStorage);


        for (int i = bottomY + 1; i < topY - 1; i++) {
            fluidStorages.put(i, new MultiFluidStorage(capacity));
        }

        MultiFluidStorage topStorage = new MultiFluidStorage(capacity);
        fluidStorages.put(topY - 1, topStorage);
        fluidStorages.put(topY, topStorage);
        ChemCraft.info();
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
        for (int i = fluidStorages.firstKey() + 1; i < fluidStorages.lastKey() - 1; i++) {
            CompoundTag storageTag = new CompoundTag();
            fluidStorages.get(i).saveToNBT(storageTag);
            masterTag.put(String.valueOf(i), storageTag);
        }

        nbt.put("chemcraft.fluidstorages", masterTag);
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, List<Fluid> fluids, double amount) {
        getFluidStorage(pipePos, pipeDir).add(fluids, amount);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, List<Fluid> extractTo) {
        return getFluidStorage(pipePos, pipeDir).extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return fluidStorages.getOrDefault(pipePos.getY(), new MultiFluidStorage(0));
    }

    @Override
    public void tick() {

    }

    @Override
    public void onLoad() {
        super.onLoad();
        BlockPos pos = getBlockPos();
        if (!level.isClientSide && !MultiBlockHandler.isValidMultiBlock(pos, level))
            new CrudeOilRefinerMultiBlock(pos, level).check(true);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        MultiBlockHandler.remove(getBlockPos(), level, true);
    }
}
