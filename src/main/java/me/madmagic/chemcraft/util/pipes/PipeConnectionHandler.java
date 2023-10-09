package me.madmagic.chemcraft.util.pipes;

import me.madmagic.chemcraft.instances.blocks.PipeBlock;
import me.madmagic.chemcraft.util.ConnectionHandler;
import me.madmagic.chemcraft.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.HashMap;
import java.util.Map;

public class PipeConnectionHandler {

    public static final Map<Direction, IntegerProperty> connectionProperties = new HashMap<>() {{
        DirectionUtil.forEach(dir -> {
            String name = dir.getName().toLowerCase();
            put(dir, IntegerProperty.create(name, 0, 2));
        });
    }};

    public static BlockState updateAllConnectionStates(BlockState state, BlockPos pos, Level level) {
        for (Direction direction : DirectionUtil.directions) {
            BlockState thatState = level.getBlockState(pos.relative(direction));
            state = updateConnectionStateAtDir(state, thatState, direction);
        }
        return state;
    }

    public static BlockState updateConnectionStateAtDir(BlockState state, BlockState neighborState, Direction direction) {
        boolean canConnectToDir = canDirConnect(neighborState, direction);
        boolean otherIsDisconnected = isDirDisconnected(neighborState, direction.getOpposite());
        boolean selfDisconnected = isDirDisconnected(state, direction);
        boolean isPipe = ConnectionHandler.isStateOfType(neighborState, PipeBlock.class);

        if (canConnectToDir && otherIsDisconnected || (!isPipe && selfDisconnected)) state = setDisConnected(state, direction);
        else if (canConnectToDir) state = setConnected(state, direction);
        else if (!selfDisconnected) state = setNone(state, direction);

        return state;
    }

    /*
    0 none
    1 connected
    2 disconnected
     */

    public static boolean isDirConnected(BlockState state, Direction direction) {
       IntegerProperty property = connectionProperties.get(direction);
        if (!state.hasProperty(property)) return false;
        return state.getValue(property) == 1;
    }

    public static boolean isDirDisconnected(BlockState state, Direction direction) {
        IntegerProperty property = connectionProperties.get(direction);
        if (!state.hasProperty(property)) return false;
        return state.getValue(property) == 2;
    }

    public static boolean canDirConnect(BlockState state, Direction dir) {
        return getConnectionTypeOfBlock(state, dir).canConnect;
    }

    public static boolean isPipeAt(BlockPos ownPos, Level level, Direction connectTo) {
        BlockState state = level.getBlockState(ownPos.relative(connectTo));
        return ConnectionHandler.isStateOfType(state, PipeBlock.class);
    }

    public static IPipeConnectable.PipeConnectionType getConnectionTypeAtDir(BlockPos ownPos, Direction dir, Level level) {
        BlockState thatState = level.getBlockState(ownPos.relative(dir));
        return getConnectionTypeOfBlock(thatState, dir);
    }

    public static IPipeConnectable.PipeConnectionType getConnectionTypeOfBlock(BlockState thatState, Direction dirToThat) {
        Block thatBlock = thatState.getBlock();
        Direction fromThat = dirToThat.getOpposite();

        if (!(thatBlock instanceof IPipeConnectable connectable)) return IPipeConnectable.PipeConnectionType.NONE;
        return connectable.connectionType(thatState, fromThat);
    }


    public static void updateConnection(BlockState state, BlockPos pos, Direction clickedDir, Level level) {
        BlockState neighborState = level.getBlockState(pos.relative(clickedDir));

        boolean selfDisconnected = isDirDisconnected(state, clickedDir);
        boolean canConnectToDir = canDirConnect(neighborState, clickedDir);
        boolean otherIsDisconnected = isDirDisconnected(neighborState, clickedDir.getOpposite());

        if (otherIsDisconnected || selfDisconnected) state = setConnected(state, clickedDir);
        else if (canConnectToDir) state = setDisConnected(state, clickedDir);

        level.setBlockAndUpdate(pos, state);
    }

    public static BlockState setConnected(BlockState state, Direction direction) {
        IntegerProperty property = connectionProperties.get(direction);
        return state.setValue(property, 1);
    }

    public static BlockState setDisConnected(BlockState state, Direction direction) {
        IntegerProperty property = connectionProperties.get(direction);
        return state.setValue(property, 2);
    }

    public static BlockState setNone(BlockState state, Direction direction) {
        IntegerProperty property = connectionProperties.get(direction);
        return state.setValue(property, 0);
    }
}
