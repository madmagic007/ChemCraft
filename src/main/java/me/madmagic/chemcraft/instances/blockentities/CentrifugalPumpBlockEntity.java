package me.madmagic.chemcraft.instances.blockentities;

import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blocks.MotorBlock;
import me.madmagic.chemcraft.instances.blocks.PipeBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import me.madmagic.chemcraft.instances.menus.CentrifugalPumpMenu;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.fluids.DisplacementHandler;
import me.madmagic.chemcraft.util.fluids.Fluid;
import me.madmagic.chemcraft.util.networking.INetworkUpdateAble;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CentrifugalPumpBlockEntity extends BaseBlockEntity implements MenuProvider, INetworkUpdateAble, IRotateAble {

    public static final int maxFlowRate = 50000;
    public int flowRate = 25000;
    private final double tickFactor = 1. / 60. / 60. / 20.;
    public static final int powerUsageFactor = 200;
    private final ContainerData data;

    public CentrifugalPumpBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.centrifugalPump.get(), pos, state);

        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                if (pIndex == 0) return flowRate;
                return 0;
            }

            @Override
            public void set(int pIndex, int pValue) {
                if (pIndex == 0) flowRate = pValue;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        flowRate = nbt.getInt("chemcraft.flowrate");
    }

    @Override
    public void saveToNBT(CompoundTag nbt) {
        nbt.putInt("chemcraft.flowrate", flowRate);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Centrifugal Pump");
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CentrifugalPumpMenu(pContainerId, this, data);
    }

    @Override
    public void updateFromNetworking(int... values) {
        flowRate = values[0];
    }

    public PipeLine getSuckPipeline() {
        Direction facing = getFacing(getBlockState());
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

    private BlockPos motorPos() {
        Direction motorDir = getFacing(getBlockState()).getOpposite();
        return worldPosition.relative(motorDir);
    }

    private MotorBlockEntity getMotorEnt() {
        return (MotorBlockEntity) level.getBlockEntity(motorPos());
    }

    public boolean hasMotor() {
        BlockState motorSate = level.getBlockState(motorPos());
        return ConnectionHandler.isStateOfType(motorSate, MotorBlock.class);
    }

    private boolean hasWorkingMotor() {
        if (!hasMotor()) return false;

        MotorBlockEntity motorEnt = getMotorEnt();
        return (motorEnt.hasEnoughEnergy(flowRate / powerUsageFactor) &&
                !motorEnt.isPowered(motorEnt.getBlockState())
        );
    }

    private void performPump(PipeLine origin, PipeLine destination) {
        PipeLine availableOrigin = new PipeLine();
        double fluidAvailable = DisplacementHandler.calculateFluidAvailable(origin, availableOrigin);

        PipeLine availableDestination = new PipeLine();
        double availableSpace = DisplacementHandler.calculateSpaceAvailable(destination, availableDestination);

        double toExtract = Math.min(flowRate * tickFactor, Math.min(fluidAvailable, availableSpace));

        List<Fluid> extracted = DisplacementHandler.extract(availableOrigin, toExtract);

        DisplacementHandler.feed(availableDestination, extracted);
        getMotorEnt().useEnergy(flowRate / powerUsageFactor);
    }

    @Override
    public void tick() {
        if (!hasWorkingMotor()) return;

        PipeLine originLine = getSuckPipeline();
        PipeLine destinationLine = getPressPipeline();

        //todo fix air cooler not detected as set
        if (GeneralUtil.anyNull(originLine, destinationLine) ||
            !originLine.hasContainers() || !destinationLine.hasContainers()) return;

        performPump(originLine, destinationLine);
    }
}