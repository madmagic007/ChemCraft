package me.madmagic.chemcraft.util;


import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;

public class ShapeUtil {

    public static VoxelShape rotate(VoxelShape defaultShape, Direction to) {
        final VoxelShape[] buffer = { defaultShape, Shapes.empty() };

        final int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    public static Map<Direction, VoxelShape> createRotatedShapesMap(VoxelShape defaultShape) {
        Map<Direction, VoxelShape> map = new HashMap<>();
        for (Direction direction : Direction.values())
            map.put(direction, rotate(defaultShape, direction));
        return map;
    }
}
