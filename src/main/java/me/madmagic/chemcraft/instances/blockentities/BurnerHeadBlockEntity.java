package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.fluids.DisplacementHandler;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;

public class BurnerHeadBlockEntity extends BaseBlockEntity implements IFluidContainer {

    private final MultiFluidStorage storage = new MultiFluidStorage(500);
    private final BlockPos firePos = getBlockPos().relative(Direction.UP);

    private static final double burnRate = 200000 * DisplacementHandler.tickFactor;

    public BurnerHeadBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.burnerHead.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        BlockState fireState = level.getBlockState(firePos);
        boolean isFire = ConnectionHandler.isStateOfType(fireState, FireBlock.class);
        boolean isAir = ConnectionHandler.isStateOfType(fireState, AirBlock.class);

        if (!isAir && !isFire) return;

        if ( storage.getStored() > 0) {
            if (!isFire)
                level.setBlockAndUpdate(firePos, Blocks.FIRE.defaultBlockState());

            LinkedList<Fluid> extractList = new LinkedList<>();
            storage.extract(burnRate, extractList);
        } else if (isFire)
            level.setBlockAndUpdate(firePos, Blocks.AIR.defaultBlockState());
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        storage.loadFromNBT(nbt);
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        storage.saveToNBT(nbt);
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, LinkedList<Fluid> fluids) {
        storage.add(fluids);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, LinkedList<Fluid> extractTo) {
        return storage.extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage(BlockPos pipePos, Direction pipeDir) {
        return storage;
    }
}
