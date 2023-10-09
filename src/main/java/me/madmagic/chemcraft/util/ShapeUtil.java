package me.madmagic.chemcraft.util;


import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeUtil {

    public static VoxelShape rotateUnoptimized(VoxelShape defaultShape, Direction to) {
        if (to == Direction.NORTH) return defaultShape;

        List<AABB> sourceBoxes = defaultShape.toAabbs();
        VoxelShape rotatedShape = Shapes.empty();
        int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (AABB box : sourceBoxes) {
            for (int i = 0; i < times; i++) {
                box = new AABB(1 - box.maxZ, box.minY, box.minX, 1 - box.minZ, box.maxY, box.maxX);
            }
            rotatedShape = orUnoptimized(rotatedShape, Shapes.create(box));
        }

        return rotatedShape;
    }

    public static Map<Direction, VoxelShape> createRotatedShapesMap(VoxelShape defaultShape) {
        Map<Direction, VoxelShape> map = new HashMap<>();
        DirectionUtil.forEach(direction ->
            map.put(direction, rotateUnoptimized(defaultShape, direction).optimize())
        );
        return map;
    }

    public static VoxelShape orUnoptimized(VoxelShape first, VoxelShape second) {
        return Shapes.joinUnoptimized(first, second, BooleanOp.OR);
    }
}
