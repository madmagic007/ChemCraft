package me.madmagic.chemcraft.util.pipes;

import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

public class PipeShapes {

    private static final Map<Direction, VoxelShape> directionalShapes = new HashMap<>() {{
        put(Direction.NORTH, Block.box(4, 4, 0, 12, 12, 9));
        put(Direction.EAST, Block.box(7, 4, 4, 16, 12, 12));
        put(Direction.SOUTH, Block.box(4, 4, 7, 12, 12, 16));
        put(Direction.WEST, Block.box(0, 4, 4, 9, 12, 12));
        put(Direction.UP, Block.box(4, 7, 4, 12, 16, 12));
        put(Direction.DOWN, Block.box(4, 0, 4, 12, 9, 12));
    }};

    public static VoxelShape get(Direction... directions) {
        List<VoxelShape> shapes = new ArrayList<>();
        Arrays.stream(directions).forEach(dir -> shapes.add(directionalShapes.get(dir)));

        return join(shapes);
    }

    public static VoxelShape get(List<Direction> directions) {
        List<VoxelShape> shapes = new ArrayList<>();
        directions.forEach(dir -> shapes.add(directionalShapes.get(dir)));

        return join(shapes);
    }

    public static VoxelShape join(List<VoxelShape> shapes) {
        VoxelShape master = shapes.get(0);

        for (int i = 1; i < shapes.size(); i++) {
            master = Shapes.join(master, shapes.get(i), BooleanOp.OR);
        }
        return master;
    }

    public static VoxelShape of(BlockState state) {
        List<Direction> connectedDirections = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (PipeConnectionHandler.isDirConnected(state, direction)) connectedDirections.add(direction);
        }

        if (connectedDirections.isEmpty()) {
            switch (state.getValue(IRotateAble.facing)) {
                case WEST:
                case EAST:
                    return get(Direction.NORTH, Direction.SOUTH);
                case NORTH:
                case SOUTH:
                    return get(Direction.WEST, Direction.EAST);
            }
        }

        return get(connectedDirections);
    }
}
