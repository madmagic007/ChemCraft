package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyStorageBlockEntity;
import me.madmagic.chemcraft.instances.blocks.PipeBlock;
import me.madmagic.chemcraft.instances.menus.AirCoolerMenu;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.fluids.DisplacementHandler;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import me.madmagic.chemcraft.util.fluids.MultiFluidStorage;
import me.madmagic.chemcraft.util.pipes.IPipeConnectable;
import me.madmagic.chemcraft.util.pipes.PipeConnectionHandler;
import me.madmagic.chemcraft.util.pipes.PipeLine;
import me.madmagic.chemcraft.util.pipes.PipelineHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AirCoolerBlockEntity extends BaseEnergyStorageBlockEntity implements MenuProvider, IFluidContainer {

    private static int maxCoolingSpt = 25;
    private static final int airTemp = 25;
    public static final int powerFactor = 3;

    private boolean active = false;
    private int coolingSpt = 15;

    public AirCoolerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CustomBlockEntities.airCooler.get(), pPos, pBlockState, 1000);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Air cooler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AirCoolerMenu(pContainerId, this, containerData);
    }

    @Override
    protected int getDataValue(int index) {
        if (index == 1) return coolingSpt;
        return super.getDataValue(index);
    }

    @Override
    protected void setDataValue(int index, int value) {
        if (index == 1) coolingSpt = value;
        else super.setDataValue(index, value);
    }

    @Override
    protected int getDataCount() {
        return 2;
    }

    private final MultiFluidStorage fluidStorage = new MultiFluidStorage(500) {};

    @Override
    public void tick() {
        int energyUsage = coolingSpt * powerFactor;

        if (hasEnoughEnergy(energyUsage)) {
            useEnergy(energyUsage);

            if (!active) {
                active = true;
                level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, true));
            }
        } else if (active) {
            active = false;
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, false));
        }
    }

    @Override
    public void receive(BlockPos pipePos, Direction pipeDir, List<Fluid> fluids, double amount) {
        if (active) fluids.forEach(fluid ->
            fluid.temperature = Math.max(airTemp, fluid.temperature - coolingSpt)
        );

        BlockPos coldPipePos = worldPosition.relative(pipeDir.getOpposite());
        BlockState pipeState = level.getBlockState(coldPipePos);

        if (!ConnectionHandler.isStateOfType(pipeState, PipeBlock.class) ||
                PipeConnectionHandler.isDirDisconnected(pipeState, pipeDir.getOpposite())) {
            fluidStorage.add(fluids, amount);
            return;
        }

        PipeLine pipeLine = PipelineHandler.findPipeline(coldPipePos, level, IPipeConnectable.PipeConnectionType.INPUT);

        if (!pipeLine.hasContainers()) {
            fluidStorage.add(fluids, amount);
            return;
        }

        PipeLine destinationLine = new PipeLine();
        amount = Math.min(amount, DisplacementHandler.calculateSpaceAvailable(pipeLine, destinationLine));

        if (amount <= 0) {
            fluidStorage.add(fluids, amount);
            return;
        }


        DisplacementHandler.feed(destinationLine, fluids);
    }

    @Override
    public double extract(BlockPos pipePos, Direction pipeDir, double amount, List<Fluid> extractTo) {
        return fluidStorage.extract(amount, extractTo);
    }

    @Override
    public MultiFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        fluidStorage.saveToNBT(nbt);
        super.saveToNBT(nbt);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        fluidStorage.loadFromNBT(nbt);
        super.loadFromNBT(nbt);
    }
}
