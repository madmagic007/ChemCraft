package me.madmagic.chemcraft.instances.blocks.entity;

import me.madmagic.chemcraft.ChemCraft;
import me.madmagic.chemcraft.instances.blocks.MotorBlock;
import me.madmagic.chemcraft.instances.blocks.PipeBlock;
import me.madmagic.chemcraft.instances.blocks.base.RotatableBlock;
import me.madmagic.chemcraft.instances.blocks.entity.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blocks.entity.base.BlockEntityHandler;
import me.madmagic.chemcraft.instances.menus.CentrifugalPumpMenu;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.pipes.PipelineHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.event.MouseAdapter;
import java.util.Collections;
import java.util.Set;

public class CentrifugalPumpBlockEntity extends BaseBlockEntity implements MenuProvider {

    public static final String name = "pumpblockentity";

    public int pressureSetting = 0;

    public CentrifugalPumpBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandler.pumpBlockEntity.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Centrifugal Pump");
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        pressureSetting = nbt.getInt("pump.pressure");
    }

    @Override
    public void saveToNbt(CompoundTag nbt) {
        nbt.putInt("pump.pressure", pressureSetting);
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CentrifugalPumpMenu(pContainerId, this);
    }

    public Set<BlockPos> canPumpTo() {
        Direction facing = getBlockState().getValue(RotatableBlock.facing);
        BlockPos selfPos = getBlockPos();

        //motor
        Direction motorDir = facing.getOpposite();
        BlockPos motorPos = selfPos.relative(motorDir);
        BlockState motorState = level.getBlockState(motorPos);
        if (!ConnectionHandler.isStateOfType(motorState, MotorBlock.class) || !motorHasPower(motorPos)) return null;

        //pipe
        BlockPos pipePos = selfPos.relative(Direction.UP);
        BlockState pipeState = level.getBlockState(pipePos);
        if (!ConnectionHandler.isStateOfType(pipeState, PipeBlock.class)) return null;

        Set<BlockPos> connectedPipes = PipelineHandler.getPipesWithDevices(pipePos, level);
        return PipelineHandler.getDestinationsIfCanFlow(connectedPipes, level);
    }

    // TODO VERY IMPORTANT, UNCOMMENT WHEN POWER IS ACTUALLY IMPLEMENTED
    private boolean motorHasPower(BlockPos motorPos) {
        return true;

//        BlockEntity bEnt = level.getBlockEntity(motorPos);
//        if (!(bEnt instanceof MotorBlockEntity motorEnt)) return false;
//        return (motorEnt.hasEnoughPower(pressureSetting));
    }

    public static <E extends CentrifugalPumpBlockEntity> void tick(Level level, BlockPos pos, BlockState state, E e) {
        if (level.isClientSide) return;

        Set<BlockPos> destinations = e.canPumpTo();
        if (destinations != null && !destinations.isEmpty()) ChemCraft.info("pump can pump");
    }
}
