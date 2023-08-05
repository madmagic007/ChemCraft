package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blocks.MotorBlock;
import me.madmagic.chemcraft.instances.blocks.PipeBlock;
import me.madmagic.chemcraft.instances.blocks.base.RotatableBlock;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.menus.CentrifugalPumpMenu;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.DisplacementHandler;
import me.madmagic.chemcraft.util.fluids.Fluid;
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

import java.util.List;

public class CentrifugalPumpBlockEntity extends BaseBlockEntity implements MenuProvider {

    public int flowRate = 100;

    public CentrifugalPumpBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.centrifugalPump.get(), pos, state);
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        flowRate = nbt.getInt("pump.flowrate");
    }

    @Override
    public void saveToNbt(CompoundTag nbt) {
        nbt.putInt("pump.flowrate", flowRate);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Centrifugal Pump");
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CentrifugalPumpMenu(pContainerId, this);
    }

    public PipeLine getSuckPipeline() {
        Direction facing = getBlockState().getValue(RotatableBlock.facing);
        BlockPos pipePos = getBlockPos().relative(facing);
        BlockState pipeState = level.getBlockState(pipePos);

        if (!ConnectionHandler.isStateOfType(pipeState, PipeBlock.class) ||
                PipeConnectionHandler.isDirDisconnected(pipeState, facing.getOpposite())) return null;

        return PipelineHandler.findPipeline(pipePos, level, IPipeConnectable.PipeConnectionType.OUTPUT);
    }

    public PipeLine getPressPipeline() {
        BlockPos pipePos = getBlockPos().relative(Direction.UP);
        BlockState pipeState = level.getBlockState(pipePos);
        if (!ConnectionHandler.isStateOfType(pipeState, PipeBlock.class) ||
                PipeConnectionHandler.isDirDisconnected(pipeState, Direction.DOWN)) return null;

        return PipelineHandler.findPipeline(pipePos, level, IPipeConnectable.PipeConnectionType.INPUT);
    }

    // TODO VERY IMPORTANT, UNCOMMENT WHEN POWER IS ACTUALLY IMPLEMENTED
    private boolean hasWorkingMotor(Direction motorDir) {
        BlockPos motorPos = getBlockPos().relative(motorDir);
        BlockState motorState = level.getBlockState(motorPos);

        if (!ConnectionHandler.isStateOfType(motorState, MotorBlock.class)) return false;
        return true;

//        BlockEntity bEnt = level.getBlockEntity(motorPos);
//        if (!(bEnt instanceof MotorBlockEntity motorEnt)) return false;
//        return (motorEnt.hasEnoughPower(pressureSetting));
    }

    private void performPump(PipeLine origin, PipeLine destination) {
        PipeLine availableOrigin = new PipeLine();
        double fluidAvailable = DisplacementHandler.calculateFluidAvailable(origin, availableOrigin);

        PipeLine availableDestination = new PipeLine();
        double availableSpace = DisplacementHandler.calculateSpaceAvailable(destination, availableDestination);

        double toExtract = Math.min(flowRate, Math.min(fluidAvailable, availableSpace));

        List<Fluid> extracted = DisplacementHandler.extract(availableOrigin, toExtract);

        DisplacementHandler.feed(availableDestination, extracted);
    }

    @Override
    public void tick() {
        Direction facing = getBlockState().getValue(RotatableBlock.facing);

        if (!hasWorkingMotor(facing.getOpposite())) return;

        PipeLine originLine = getSuckPipeline();
        PipeLine destinationLine = getPressPipeline();

        if (GeneralUtil.anyNull(originLine, destinationLine) ||
            !originLine.hasContainers() || !destinationLine.hasContainers()) return;

        performPump(originLine, destinationLine);
    }
}