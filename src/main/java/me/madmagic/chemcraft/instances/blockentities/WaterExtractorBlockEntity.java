package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class WaterExtractorBlockEntity extends BaseBlockEntity implements IFluidContainer {
    private boolean isInWater = false;

    public WaterExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.waterExtractor.get(), pos, state);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {

    }

    @Override
    public void saveToNBT(CompoundTag nbt) {

    }

    @Override
    public void tick() {
        BlockPos pos = getBlockPos();
        Level level = getLevel();
        BlockState selfState = getBlockState();

        int found = 0;
        for (Direction dir : ConnectionHandler.horizontalDirections) {
            BlockPos relativePos = pos.relative(dir);
            BlockState state = level.getBlockState(relativePos);
            FluidState fluidState = level.getFluidState(relativePos);

            if (
                    (state.getBlock() instanceof LiquidBlockContainer && state.getValue(BlockStateProperties.WATERLOGGED)) ||
                            (fluidState.is(Fluids.WATER) && fluidState.isSource()))
                found++;
        }

        isInWater = found > 1 && selfState.getValue(BlockStateProperties.WATERLOGGED);
    }


    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, List<Fluid> fluids, double amount) {}

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, List<Fluid> extractTo) {
        if (!isInWater) return 0;

        extractTo.add(new Fluid("water", amount, 37));
        return amount;
    }

    private final MultiFluidStorage fluidStorage = new MultiFluidStorage(1000) {
        @Override
        public double getStored() {
            return isInWater ? 1000 : 0;
        }

        @Override
        public double getSpaceLeft() {
            return 0;
        }
    };

    @Override
    public MultiFluidStorage getFluidStorage() {
        return fluidStorage;
    }
}
