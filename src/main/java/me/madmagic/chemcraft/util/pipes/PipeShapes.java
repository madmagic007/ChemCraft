package me.madmagic.chemcraft.util.pipes;

import me.madmagic.chemcraft.util.ShapeUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PipeShapes {

    private static final Map<Direction, VoxelShape> directionalShapes = new HashMap<>() {{
        put(Direction.NORTH, Block.box(4, 4, 0, 12, 12, 9));
        put(Direction.EAST, Block.box(7, 4, 4, 16, 12, 12));
        put(Direction.SOUTH, Block.box(4, 4, 7, 12, 12, 16));
        put(Direction.WEST, Block.box(0, 4, 4, 9, 12, 12));
        put(Direction.UP, Block.box(4, 7, 4, 12, 16, 12));
        put(Direction.DOWN, Block.box(4, 0, 4, 12, 9, 12));
    }};

    private static final Map<Integer, VoxelShape> pipeShapes = new HashMap<>() {{
        possibleShapes().forEach(list -> {
            Stream<VoxelShape> shapeStream = list.stream()
                    .map(directionalShapes::get);

            put(getIdForDirections(list), shapeStream.reduce(ShapeUtil::orUnoptimized).orElseGet(() -> directionalShapes.get(list.get(0))).optimize());
        });
    }};

    private static final VoxelShape disconnected = Block.box(6, 6, 6, 10, 10, 10);

    public static VoxelShape of(BlockState state) {
        List<Direction> connectedDirections = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (PipeConnectionHandler.isDirConnected(state, direction)) connectedDirections.add(direction);
        }

        return pipeShapes.getOrDefault(getIdForDirections(connectedDirections), disconnected);
    }

    private static int getIdForDirections(List<Direction> dirs) {
        int shapeId = 0;

        for (Direction direction : dirs) {
            shapeId |= (1 << direction.get3DDataValue());
        }

        return shapeId;
    }

    private static List<List<Direction>> possibleShapes() {
        List<List<Direction>> result = new ArrayList<>();
        List<Direction> currentCombination = new ArrayList<>();
        possibleShapesHelper(Direction.values(), 0, currentCombination, result);
        return result;
    }

    private static void possibleShapesHelper(Direction[] values, int currentIndex, List<Direction> currentCombination, List<List<Direction>> result) {
        if (currentIndex == values.length) {
            if (!currentCombination.isEmpty())
                result.add(new ArrayList<>(currentCombination));
            return;
        }

        currentCombination.add(values[currentIndex]);
        possibleShapesHelper(values, currentIndex + 1, currentCombination, result);
        currentCombination.remove(currentCombination.size() - 1);
        possibleShapesHelper(values, currentIndex + 1, currentCombination, result);
    }
}
