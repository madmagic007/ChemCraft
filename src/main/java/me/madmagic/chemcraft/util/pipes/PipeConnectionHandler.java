package me.madmagic.chemcraft.util.pipes;

import me.madmagic.chemcraft.instances.blocks.PipeBlock;
import me.madmagic.chemcraft.util.ConnectionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.*;

public class PipeConnectionHandler {

    public static final Map<Direction, EnumProperty<PipeConnection>> connectionProperties = new HashMap<>();
    static {
        for (Direction dir : Direction.values()) {
            String name = dir.getName().toLowerCase();
            connectionProperties.put(dir, EnumProperty.create(name, PipeConnection.class));
        }
    }

    public static BlockState checkAndUpdateConnectionState(BlockState state, BlockPos pos, Level level) {
        for (Direction direction : Direction.values()) {
            EnumProperty<PipeConnection> property = connectionProperties.get(direction);

            //todo add check if same fluid?
            boolean canConnect = canConnectTo(pos, level, direction);
            boolean recipientNotDisconnected = !isDisconnectedTo(level.getBlockState(pos.relative(direction)), direction.getOpposite());
            boolean curSideIsDisconnected = state.getValue(property).isDisconnected();

            if (canConnect && recipientNotDisconnected)
                state = state.setValue(property, PipeConnection.CONNECTED);
            else if (canConnect)
                state = state.setValue(property, PipeConnection.DISCONNECTED);
            else if (!curSideIsDisconnected)
                state = state.setValue(property, PipeConnection.NONE);
        }
        return state;
    }

    public static boolean isConnectedTo(BlockState state, Direction direction) {
        EnumProperty<PipeConnection> property = connectionProperties.get(direction);
        PipeConnection connection = state.getValue(property);
        return connection.isConnected();
    }

    private static boolean isDisconnectedTo(BlockState state, Direction direction) {
        EnumProperty<PipeConnection> property = connectionProperties.get(direction);
        if (!state.hasProperty(property)) return false;
        return state.getValue(property).isDisconnected();
    }

    private static boolean canConnectTo(BlockPos ownPos, Level level, Direction connectTo) {
        BlockState state = level.getBlockState(ownPos.relative(connectTo));
        Block block = state.getBlock();
        return block instanceof IPipeConnectable && ((IPipeConnectable) block).connectionType(state, connectTo.getOpposite()).canConnect;
    }

    private static boolean isPipeAt(BlockPos ownPos, Level level, Direction connectTo) {
        BlockState state = level.getBlockState(ownPos.relative(connectTo));
        return ConnectionHandler.isStateOfType(state, PipeBlock.class);
    }

    public static boolean isConnectedToDevice(BlockPos pos, Level level, BlockState ownState) {
        for (Direction direction : Direction.values()) {
            if (isConnectedTo(ownState, direction) && !isPipeAt(pos, level, direction)) return true;
        }
        return false;
    }

    public static IPipeConnectable.PipeConnectionType getConnectionType(BlockPos ownPos, Level level, Direction connectionDir) {
        BlockPos thatPos = ownPos.relative(connectionDir);
        BlockState thatState = level.getBlockState(thatPos);
        Block thatBlock = thatState.getBlock();
        Direction relativeDir = connectionDir.getOpposite();

        if (!(thatBlock instanceof IPipeConnectable)) return IPipeConnectable.PipeConnectionType.NONE;
        return ((IPipeConnectable) thatBlock).connectionType(thatState, relativeDir);
    }

    public static void updateNeighbours(BlockPos ownPos, Level level) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = ownPos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (!(neighborState.getBlock() instanceof PipeBlock)) continue;

            neighborState = checkAndUpdateConnectionState(neighborState, neighborPos, level);
            level.setBlockAndUpdate(neighborPos, neighborState);
        }
    }

    public static void updateConnection(BlockState state, BlockPos pos, Direction clickedDir, Level level) {
        EnumProperty<PipeConnection> clickedDirProperty = connectionProperties.get(clickedDir);
        boolean disconnectOther = false;
        BlockPos affectedBlockPos = pos.relative(clickedDir);
        BlockState affectedBlockState = level.getBlockState(affectedBlockPos);

        if (isConnectedTo(state, clickedDir)) {
            state = state.setValue(clickedDirProperty, PipeConnection.DISCONNECTED);
            disconnectOther = true;
        } else if (state.getValue(clickedDirProperty).isDisconnected())
            state = state.setValue(clickedDirProperty, PipeConnection.CONNECTED);

        if (isPipeAt(pos, level, clickedDir))
            level.setBlockAndUpdate(pos, state);

        if (affectedBlockState.getBlock() instanceof PipeBlock) {
            Direction opposite = clickedDir.getOpposite();
            EnumProperty<PipeConnection> affectedProperty = connectionProperties.get(opposite);

            affectedBlockState = affectedBlockState.setValue(affectedProperty, disconnectOther ? PipeConnection.DISCONNECTED : PipeConnection.CONNECTED);
            level.setBlockAndUpdate(affectedBlockPos, affectedBlockState);
        }
    }

    public static List<BlockPos> getAllConnectedPipes(BlockPos selfPos, Level level) {
        Set<BlockPos> connected = new HashSet<>();
        getConnectedPipesRecursive(selfPos, level, connected);
        return new ArrayList<>(connected);
    }

    private static void getConnectedPipesRecursive(BlockPos pos, Level level, Set<BlockPos> connectedPositions) {
        connectedPositions.add(pos);
        BlockState state = level.getBlockState(pos);

        List<Direction> touchingPipes = ConnectionHandler.getTouchingDirectionsWhereType(pos, level, PipeBlock.class);
        for (Direction touchingPipeDir : touchingPipes) {
            BlockPos relativePos = pos.relative(touchingPipeDir);
            if (!isConnectedTo(state, touchingPipeDir) || connectedPositions.contains(relativePos)) continue;
            getConnectedPipesRecursive(relativePos, level, connectedPositions);
        }
    }
}
