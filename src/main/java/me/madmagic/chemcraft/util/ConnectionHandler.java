package me.madmagic.chemcraft.util;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ConnectionHandler {

    public static final Set<Direction> horizontalDirections = Set.of(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    );

    public static Direction isTouching(BlockPos pos, Level level, Class<? extends Block> targetBlock) {
        for (Direction dir : DirectionUtil.directions) {
            BlockPos relativePos = pos.relative(dir);
            BlockState state = level.getBlockState(relativePos);

            if (isStateOfType(state, targetBlock))
                return dir;
        }

        return null;
    }

    public static boolean isStateOfType(BlockState state, Class<? extends Block> targetBlock) {
        return targetBlock.isInstance(state.getBlock());
    }

    @SafeVarargs
    public static Set<BlockPos> getConnectedWhereType(BlockPos startPos, Level level, Class<? extends Block>... findBlock) {
        Set<BlockPos> foundBlocks = new HashSet<>();
        foundBlocks.add(startPos);

        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();

            DirectionUtil.forEach(direction -> {
                BlockPos neighbor = currentPos.relative(direction);
                BlockState neighborState = level.getBlockState(neighbor);

                boolean isCorrect = false;

                for (Class<? extends Block> desiredBlock : findBlock) {
                    if (isStateOfType(neighborState, desiredBlock) && !foundBlocks.contains(neighbor)) isCorrect = true;
                }

                if (!isCorrect) return;

                queue.add(neighbor);
                foundBlocks.add(neighbor);
            });
        }
        return foundBlocks;
    }
}
