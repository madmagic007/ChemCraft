package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;

public class CrudeOilExtractorBlockEntity extends BaseBlockEntity implements IFluidContainer {

    public CrudeOilExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.crudeOilExtractor.get(), pos, state);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {}

    @Override
    public void saveToNBT(CompoundTag nbt) {}

    @Override
    public void tick() {}

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids) {}

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo) {

        extractTo.add(new Fluid("crude_oil", amount, 25));
        return amount;
    }

    private final MultiFluidStorage fluidStorage = new MultiFluidStorage(1000) {
        @Override
        public double getStored() { return 1000; }

        @Override
        public double getSpaceLeft() {
            return 0;
        }
    };

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return fluidStorage;
    }
}
