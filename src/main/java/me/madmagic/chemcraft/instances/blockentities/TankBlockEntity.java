package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import me.madmagic.chemcraft.util.multiblock.MultiBlockHandler;
import me.madmagic.chemcraft.util.multiblock.instances.TankMultiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;

public class TankBlockEntity extends BaseBlockEntity implements IFluidContainer {

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.tank.get(), pos, state);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        fluidStorage.loadFromNBT(nbt);
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        fluidStorage.saveToNBT(nbt);
    }

    public static final int capacityPerTank = 1000;
    public final MultiFluidStorage fluidStorage = new MultiFluidStorage(capacityPerTank) {};

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids) {
        fluidStorage.add(fluids);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo) {
        return fluidStorage.extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return fluidStorage;
    }

    @Override
    public void tick() {}

    @Override
    public void onLoad() {
        super.onLoad();
        BlockPos pos = getBlockPos();
        if (!level.isClientSide && !MultiBlockHandler.isValidMultiBlock(pos, level))
            new TankMultiBlock(pos, level).check(true);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        MultiBlockHandler.remove(getBlockPos(), level, true);
    }

    public void setCapacity(int i) {
        fluidStorage.capacity = i;
    }
}
